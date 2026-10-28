/*
 * Copyright 1997-2026 Optimatika (www.optimatika.se)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ojalgo.benchmark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.regex.Pattern;

import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatFactory;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * The forked JVMs grant native access so that ojAlgo and SMILE can use native code. Whether they actually do
 * is decided by the library being benchmarked – "ojAlgo-JVM"/"SMILE-JVM" (pure Java) or
 * "ojAlgo-Native"/"SMILE-Native".
 * <p>
 * The benchmarks (subclasses) inherit the state scope as well as the "dim" and "lib" parameters.
 * <p>
 * Rules that apply to all benchmarks:
 * <ul>
 * <li>What is measured is a single call of the library's implementation of the operation – the lambda
 * returned by {@link MatrixBenchmarkLibrary}. Creating decomposition/solver objects and preallocating results
 * is done in setup, and is not measured.</li>
 * <li>The inputs are the same for every invocation. An implementation must not modify them – if a library's
 * algorithm works in place, copying the input is part of what is measured – and must not reuse anything from
 * a previous invocation. The one exception is RepeatedSolve, that measures solving with a decomposition
 * prepared in setup.</li>
 * <li>After each iteration, outside the measured time, the result of the last invocation is verified,
 * independently of the library. The tolerances are generous – the verification is there to catch wrong
 * results, not to rank accuracy. A library that produces a wrong result is reported as a failure, and gets no
 * score.</li>
 * <li>Each adapter makes the best possible use of the library's features: the most suitable API for the
 * operation, and any switching between alternative implementations (by matrix size, single or
 * multi-threaded…) that the library does itself. The adapter code does no switching of its own – it makes
 * one, static, choice per operation, also when the library has no switching for that operation.</li>
 * </ul>
 */
@Fork(jvmArgsAppend = "--enable-native-access=ALL-UNNAMED")
@State(Scope.Benchmark)
public abstract class MatrixBenchmarkOperation {

    @FunctionalInterface
    public interface DecompositionOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg) {
            try {
                return this.operate((I) arg);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }

        I[] operate(I arg) throws Exception;

    }

    /**
     * The expected value of each element, when verifying.
     */
    @FunctionalInterface
    public interface ElementFunction {

        double invoke(int row, int col);

    }

    /**
     * One step, predict and update, of a Kalman filter. The model – transition [F], process noise [Q],
     * observation [H] and measurement noise [R] – the state [x], its covariance [P] and the measurement [z]
     * are inputs. The updated state and covariance are written to the preallocated results.
     */
    @FunctionalInterface
    public interface KalmanFilterOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default Object execute(final Object transition, final Object processNoise, final Object observation, final Object measurementNoise, final Object state,
                final Object covariance, final Object measurement, final Object updatedState, final Object updatedCovariance) {
            try {
                this.operate((I) transition, (I) processNoise, (I) observation, (I) measurementNoise, (I) state, (I) covariance, (I) measurement,
                        (T) updatedState, (T) updatedCovariance);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
            return updatedCovariance;
        }

        void operate(I transition, I processNoise, I observation, I measurementNoise, I state, I covariance, I measurement, T updatedState, T updatedCovariance)
                throws Exception;

    }

    @FunctionalInterface
    public interface MutatingBinaryMatrixMatrixOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg1, final Object arg2, final Object ret) {
            try {
                this.operate((I) arg1, (I) arg2, (T) ret);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
            return ret;
        }

        void operate(I arg1, I arg2, T ret) throws Exception;

    }

    @FunctionalInterface
    public interface MutatingBinaryMatrixScalarOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg1, final double arg2, final Object ret) {
            try {
                this.operate((I) arg1, arg2, (T) ret);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
            return ret;
        }

        void operate(I arg1, double arg2, T ret) throws Exception;

    }

    @FunctionalInterface
    public interface MutatingUnaryMatrixOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg, final Object ret) {
            try {
                this.operate((I) arg, (T) ret);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
            return ret;
        }

        void operate(I arg, T ret) throws Exception;

    }

    /**
     * Prepares, in setup, the operation that is measured – e.g. decomposes a matrix, and returns an operation
     * that solves using that decomposition. Failing to prepare is an error, not a result to verify.
     */
    @FunctionalInterface
    public interface PreparingOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default ProducingUnaryMatrixOperation<I, T> execute(final Object arg) {
            try {
                return this.operate((I) arg);
            } catch (final Exception exception) {
                throw new IllegalStateException(exception);
            }
        }

        ProducingUnaryMatrixOperation<I, T> operate(I arg) throws Exception;

    }

    @FunctionalInterface
    public interface ProducingBinaryMatrixMatrixOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg1, final Object arg2) {
            try {
                return this.operate((I) arg1, (I) arg2);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }

        I operate(I arg1, I arg2) throws Exception;
    }

    @FunctionalInterface
    public interface ProducingUnaryMatrixOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg) {
            try {
                return this.operate((I) arg);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }

        I operate(I arg) throws Exception;

    }

    @FunctionalInterface
    public interface PropertyOperation<I, T extends I> {

        @SuppressWarnings("unchecked")
        default double execute(final Object arg) {
            try {
                return this.operate((I) arg);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return Double.NaN;
            }
        }

        double operate(I arg) throws Exception;

    }

    /**
     * The three ranges of matrix sizes, each with the settings that suit it – the sizes (the "dim"
     * parameter), the libraries (the "lib" parameter), the time unit, the number of forks, the iteration
     * time, the number of iterations and the timeout. Used with
     * {@link MatrixBenchmarkOperation#run(Class, Size)}, the values override the @Param defaults.
     */
    public enum Size {

        /**
         * 100–10000, the native code libraries with ojAlgo-JVM as a pure Java reference – the other pure Java
         * libraries would take hours per invocation at the largest sizes. A single invocation can take
         * minutes, ops/min, and fewer iterations (each lasts at least one invocation).
         */
        LARGE(new String[] { "100", "128", "200", "256", "500", "512", "1000", "1024", "2000", "2048", "4096", "5000", "8192", "10000" },
                new String[] { "EJML", "ojAlgo-JVM", "ojAlgo-Native", "SMILE-Native" }, TimeUnit.MINUTES, 1, new TimeValue(1L, TimeUnit.MINUTES), 2, 3,
                new TimeValue(10L, TimeUnit.MINUTES)),

        /**
         * 10–1000, all libraries. The slowest operations at 1000 take seconds, ops/s.
         */
        MEDIUM(new String[] { "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000" },
                new String[] { "EJML", "Hipparchus", "ojAlgo-JVM", "SMILE-JVM", "ojAlgo-Native", "SMILE-Native" }, TimeUnit.SECONDS, 1,
                new TimeValue(1L, TimeUnit.SECONDS), 4, 3, new TimeValue(100L, TimeUnit.SECONDS)),

        /**
         * 1–100, all libraries. Everything is fast, ops/ms. The results vary between JVMs (JIT compilation,
         * memory layout, the state of a native library), so there are 3 forks, with shorter and fewer
         * iterations each.
         */
        SMALL(new String[] { "1", "2", "3", "4", "5", "8", "10", "16", "20", "32", "50", "64", "100" },
                new String[] { "EJML", "Hipparchus", "ojAlgo-JVM", "SMILE-JVM", "ojAlgo-Native", "SMILE-Native" }, TimeUnit.MILLISECONDS, 3,
                new TimeValue(500L, TimeUnit.MILLISECONDS), 4, 3, new TimeValue(10L, TimeUnit.SECONDS));

        final String[] dims;
        final int forks;
        /**
         * How long each warmup and measurement iteration lasts – at least one invocation, however long that
         * takes.
         */
        final TimeValue iterationTime;
        final String[] libs;
        final int measurementIterations;
        final TimeValue timeout;
        final TimeUnit timeUnit;
        final int warmupIterations;

        /**
         * The timeout counts from the start of an iteration, so it must exceed the iteration time plus one
         * invocation.
         */
        Size(final String[] dims, final String[] libs, final TimeUnit timeUnit, final int forks, final TimeValue iterationTime, final int warmupIterations,
                final int measurementIterations, final TimeValue timeout) {
            this.dims = dims;
            this.libs = libs;
            this.timeUnit = timeUnit;
            this.forks = forks;
            this.iterationTime = iterationTime;
            this.warmupIterations = warmupIterations;
            this.measurementIterations = measurementIterations;
            this.timeout = timeout;
        }

    }

    /**
     * The directory where the result files are written – for benchmarks run individually, and the default for
     * suites (a suite can redeclare {@link BenchmarkSuite#RESULTS}).
     */
    public static final Path RESULTS = Path.of("results");

    /**
     * The size range the benchmarks' main methods run.
     */
    protected static final Size SIZE = Size.MEDIUM;

    /**
     * Allowed (normwise relative) error, per dimension, when verifying. Generous – the verification is there
     * to catch wrong results, not to rank accuracy.
     */
    static final double TOLERANCE = 16.0 * Math.ulp(1.0);

    /**
     * Settings for results to publish, that still run reasonably fast: 1 fork, throughput. The heap is large
     * enough for decompositions of 10000x10000 matrices. The number of forks, the iteration time, the number
     * of iterations and the time unit are set per {@link Size}. No result file – asking for a result format,
     * or a result file, makes JMH create the file before the run (empty) – {@link #run(Class, Size, Path)}
     * writes it when the run is complete.
     */
    public static ChainedOptionsBuilder options() {
        return new OptionsBuilder().forks(1).warmupIterations(3).measurementIterations(5).mode(Mode.Throughput).timeUnit(TimeUnit.MINUTES)
                .timeout(new TimeValue(10L, TimeUnit.MINUTES)).jvmArgs("-Xmx16g");
    }

    /**
     * The name of the CSV file with the results of running the benchmark class for the size range, e.g.
     * "FillByMultiplying-MEDIUM.csv".
     */
    public static String resultFileName(final Class<?> clazz, final Size size) {
        return clazz.getSimpleName() + "-" + size.name() + ".csv";
    }

    /**
     * Runs only the given benchmark class – the include pattern is its full name, so that e.g. Transpose
     * doesn't also run MultiplyTransposedLeft/Right.
     */
    public static Collection<RunResult> run(final ChainedOptionsBuilder options, final Class<?> clazz) throws RunnerException {
        return new Runner(options.include(Pattern.quote(clazz.getName() + ".")).build()).run();
    }

    /**
     * Runs the benchmark class with {@link #options()}, configured for the given size range – its sizes,
     * libraries, time unit, forks, iteration time, iterations and timeout. The results are written to a CSV
     * file in {@link #RESULTS}, named after the class and the size range, e.g.
     * "results/FillByMultiplying-MEDIUM.csv".
     */
    public static void run(final Class<?> clazz, final Size size) throws RunnerException {
        MatrixBenchmarkOperation.run(clazz, size, RESULTS);
    }

    /**
     * As {@link #run(Class, Size)}, but writes the result file to the given directory. The file is written
     * (CSV) from the results JMH returns, after the run, and only if every dim/lib combination has a result –
     * JMH completes a run also when some forks fail (a wrong result, an exception, a timeout…), skipping
     * those combinations. An interrupted, or partly failed, run leaves no result file.
     *
     * @return true if every dim/lib combination produced a result, and the result file was written
     */
    public static boolean run(final Class<?> clazz, final Size size, final Path directory) throws RunnerException {

        Path file = directory.resolve(MatrixBenchmarkOperation.resultFileName(clazz, size));

        ChainedOptionsBuilder options = MatrixBenchmarkOperation.options().param("dim", size.dims).param("lib", size.libs).timeUnit(size.timeUnit)
                .forks(size.forks).warmupTime(size.iterationTime).warmupIterations(size.warmupIterations).measurementTime(size.iterationTime)
                .measurementIterations(size.measurementIterations).timeout(size.timeout);

        Collection<RunResult> results = MatrixBenchmarkOperation.run(options, clazz);

        Set<String> missing = MatrixBenchmarkOperation.missing(results, size);
        if (!missing.isEmpty()) {
            System.out.printf("%s is incomplete, no results for (dim/lib) %s – not written%n", file.getFileName(), String.join(", ", missing));
            return false;
        }

        // Formatted in memory first, so writing the file is quick
        ByteArrayOutputStream csv = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(csv, true, StandardCharsets.UTF_8);
        ResultFormatFactory.getInstance(ResultFormatType.CSV, printer).writeOut(results);
        printer.flush();

        try {
            Files.createDirectories(directory.toAbsolutePath());
            Files.write(file, csv.toByteArray());
        } catch (IOException cause) {
            throw new UncheckedIOException(cause);
        }

        return true;
    }

    private static double[] add(final double[] left, final double[] right) {
        double[] retVal = new double[left.length];
        for (int i = 0; i < left.length; i++) {
            retVal[i] = left[i] + right[i];
        }
        return retVal;
    }

    private static void check(final String what, final double error, final double scale) throws BenchmarkRequirementsException {
        double limit = TOLERANCE * scale;
        // NaN must fail – "error > limit" alone would let it pass
        if (Double.isNaN(error) || Double.isNaN(limit) || error > limit) {
            throw new BenchmarkRequirementsException(what + " is wrong! Error " + error + " > " + limit + ", the relative error is " + error / scale);
        }
    }

    /**
     * The dim/lib combinations of the size range, as "dim/lib", that have no result.
     */
    private static Set<String> missing(final Collection<RunResult> results, final Size size) {

        Set<String> retVal = new LinkedHashSet<>();
        for (String dim : size.dims) {
            for (String lib : size.libs) {
                retVal.add(dim + "/" + lib);
            }
        }

        for (RunResult result : results) {
            retVal.remove(result.getParams().getParam("dim") + "/" + result.getParams().getParam("lib"));
        }

        return retVal;
    }

    private static double norm(final double[] vector) {
        double retVal = 0.0;
        for (int i = 0; i < vector.length; i++) {
            retVal += vector[i] * vector[i];
        }
        return Math.sqrt(retVal);
    }

    private static double[] random(final int length) {
        double[] retVal = new double[length];
        for (int i = 0; i < length; i++) {
            retVal[i] = Math.random() - 0.5;
        }
        return retVal;
    }

    private static double[] subtract(final double[] left, final double[] right) {
        double[] retVal = new double[left.length];
        for (int i = 0; i < left.length; i++) {
            retVal[i] = left[i] - right[i];
        }
        return retVal;
    }

    /**
     * Uniform in [-1, 1)
     */
    private static double uniform() {
        return 2.0 * Math.random() - 1.0;
    }

    /**
     * [a][a]<sup>T</sup>/size + [I], with [a] uniform in [-1, 1) – symmetric positive definite and well
     * conditioned, all eigenvalues in [1, 2).
     */
    protected final static Object makeCovariance(final int size, final MatrixBenchmarkLibrary<?, ?> contestant) {

        final double[] random = new double[size];

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(size, size);

        for (int i = 0; i < size; i++) {

            random[i] = MatrixBenchmarkOperation.uniform();

            for (int j = 0; j < i; j++) {
                builder.set(i, j, random[i] * random[j] / size);
                builder.set(j, i, random[j] * random[i] / size);
            }
            builder.set(i, i, random[i] * random[i] / size + 1.0);
        }

        return builder.get();
    }

    /**
     * Diagonal, the diagonal elements uniform in [1, 2).
     */
    protected final static Object makeDiagonal(final int size, final MatrixBenchmarkLibrary<?, ?> contestant) {
        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(size, size);
        for (int i = 0; i < size; i++) {
            builder.set(i, i, 1.0 + Math.random());
        }
        return builder.get();
    }

    /**
     * The KKT matrix of an equality constrained quadratic program, [H A<sup>T</sup>; A 0] – symmetric but
     * indefinite. [H], nbVariables x nbVariables, is [a][a]<sup>T</sup> + [I] as {@link #makeSPD}, and [A],
     * nbConstraints x nbVariables, uniform in [-1, 1).
     */
    protected final static Object makeKKT(final int nbVariables, final int nbConstraints, final MatrixBenchmarkLibrary<?, ?> contestant) {

        final double[] random = new double[nbVariables];

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(nbVariables + nbConstraints, nbVariables + nbConstraints);

        for (int i = 0; i < nbVariables; i++) {

            random[i] = MatrixBenchmarkOperation.uniform();

            for (int j = 0; j < i; j++) {
                builder.set(i, j, random[i] * random[j]);
                builder.set(j, i, random[j] * random[i]);
            }
            builder.set(i, i, random[i] * random[i] + 1.0);
        }

        for (int i = nbVariables; i < nbVariables + nbConstraints; i++) {
            for (int j = 0; j < nbVariables; j++) {
                double value = MatrixBenchmarkOperation.uniform();
                builder.set(i, j, value);
                builder.set(j, i, value);
            }
        }

        return builder.get();
    }

    /**
     * Uniform in [-1, 1), as in the Java Matrix Benchmark.
     */
    protected final static Object makeRandom(final int numberOfRows, final int numberOfColumns, final MatrixBenchmarkLibrary<?, ?> contestant) {
        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder tmpSupplier = contestant.getMatrixBuilder(numberOfRows, numberOfColumns);
        for (int j = 0; j < numberOfColumns; j++) {
            for (int i = 0; i < numberOfRows; i++) {
                tmpSupplier.set(i, j, MatrixBenchmarkOperation.uniform());
            }
        }
        return tmpSupplier.get();
    }

    /**
     * Uniform in [-scale, scale).
     */
    protected final static Object makeRandom(final int numberOfRows, final int numberOfColumns, final double scale,
            final MatrixBenchmarkLibrary<?, ?> contestant) {
        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(numberOfRows, numberOfColumns);
        for (int j = 0; j < numberOfColumns; j++) {
            for (int i = 0; i < numberOfRows; i++) {
                builder.set(i, j, scale * MatrixBenchmarkOperation.uniform());
            }
        }
        return builder.get();
    }

    /**
     * Rank deficient: the first rank = multipliers.length columns uniform in [-1, 1), and the remaining
     * multipliers[0].length columns linear combinations of those – column rank + j is the sum over k of
     * multipliers[k][j] times column k. The null space is spanned by the columns of [-M; I].
     */
    protected final static Object makeRankDeficient(final int numberOfRows, final double[][] multipliers, final MatrixBenchmarkLibrary<?, ?> contestant) {

        int rank = multipliers.length;
        int nullity = multipliers[0].length;

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(numberOfRows, rank + nullity);

        double[] independent = new double[rank];
        double[] dependent = new double[nullity];

        for (int i = 0; i < numberOfRows; i++) {

            for (int k = 0; k < rank; k++) {
                independent[k] = MatrixBenchmarkOperation.uniform();
                builder.set(i, k, independent[k]);
            }

            Arrays.fill(dependent, 0.0);
            for (int k = 0; k < rank; k++) {
                double factor = independent[k];
                double[] row = multipliers[k];
                for (int j = 0; j < nullity; j++) {
                    dependent[j] += factor * row[j];
                }
            }
            for (int j = 0; j < nullity; j++) {
                builder.set(i, rank + j, dependent[j]);
            }
        }

        return builder.get();
    }

    /**
     * [a][a]<sup>T</sup> + [I], as in the Java Matrix Benchmark, but with [a] uniform in [-1, 1) rather than
     * [0, 1). All eigenvalues are at least 1.
     */
    protected final static Object makeSPD(final int size, final MatrixBenchmarkLibrary<?, ?> contestant) {

        final double[] random = new double[size];

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(size, size);

        for (int i = 0; i < size; i++) {

            random[i] = MatrixBenchmarkOperation.uniform();

            for (int j = 0; j < i; j++) {
                builder.set(i, j, random[i] * random[j]);
                builder.set(j, i, random[j] * random[i]);
            }
            builder.set(i, i, random[i] * random[i] + 1.0);
        }

        return builder.get();
    }

    /**
     * Uniform in [-1, 1), mirrored – symmetric but indefinite, as in the Java Matrix Benchmark.
     */
    protected final static Object makeSymmetric(final int size, final MatrixBenchmarkLibrary<?, ?> contestant) {

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(size, size);

        for (int j = 0; j < size; j++) {
            for (int i = j; i < size; i++) {
                double value = MatrixBenchmarkOperation.uniform();
                builder.set(i, j, value);
                builder.set(j, i, value);
            }
        }

        return builder.get();
    }

    /**
     * Uniform in [-1, 1), as plain arrays – data that is not a matrix of the library being benchmarked.
     */
    protected final static double[][] makeUniform(final int numberOfRows, final int numberOfColumns) {
        double[][] retVal = new double[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                retVal[i][j] = MatrixBenchmarkOperation.uniform();
            }
        }
        return retVal;
    }

    protected final static Object makeZero(final int numberOfRows, final int numberOfColumns, final MatrixBenchmarkLibrary<?, ?> contestant) {
        return contestant.getMatrixBuilder(numberOfRows, numberOfColumns).get();
    }

    /**
     * The base matrix size.
     */
    @Param({ "1", "2", "3", "4", "5", "8", "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000", "1024", "2000", "2048", "4096",
            "5000", "8192", "10000" })
    public int dim;

    /**
     * The library, by its name in {@link MatrixBenchmarkLibrary}
     */
    @Param({ "EJML", "Hipparchus", "ojAlgo-JVM", "SMILE-JVM", "ojAlgo-Native", "SMILE-Native" })
    public String lib;

    protected MatrixBenchmarkLibrary<?, ?> library;

    /**
     * The (plain Java) determinant of {@link #myReferenceMatrix} – the sign and the logarithm of the absolute
     * value. Calculated once, when first needed – the inputs are the same for every invocation.
     */
    private double[] myReferenceDeterminant = null;
    private Object myReferenceMatrix = null;

    protected MatrixBenchmarkOperation() {
        super();
    }

    public abstract Object execute();

    public abstract void setup();

    /**
     * Verify that the tested library/functionality conforms with the benchmark specifications. Annotate the
     * implementation with <code>@TearDown(Level.Iteration)</code>
     */
    public abstract void verify() throws BenchmarkRequirementsException;

    private double[] column(final Object matrix, final int col) {
        double[] retVal = new double[library.getRowDim(matrix)];
        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = library.get(matrix, i, col);
        }
        return retVal;
    }

    /**
     * The determinant of a square matrix, calculated with a plain Java LU decomposition with partial
     * pivoting, as its sign and the logarithm of its absolute value – the determinant itself overflows for
     * large matrices.
     */
    private double[] determinant(final Object matrix) {

        int dim = library.getRowDim(matrix);

        double[][] lu = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                lu[i][j] = library.get(matrix, i, j);
            }
        }

        double sign = 1.0;
        double logAbs = 0.0;
        for (int k = 0; k < dim; k++) {
            int pivot = k;
            for (int i = k + 1; i < dim; i++) {
                if (Math.abs(lu[i][k]) > Math.abs(lu[pivot][k])) {
                    pivot = i;
                }
            }
            if (pivot != k) {
                double[] row = lu[pivot];
                lu[pivot] = lu[k];
                lu[k] = row;
                sign = -sign;
            }
            double diagonal = lu[k][k];
            sign = diagonal < 0.0 ? -sign : sign;
            logAbs += Math.log(Math.abs(diagonal));
            for (int i = k + 1; i < dim; i++) {
                double factor = lu[i][k] / diagonal;
                for (int j = k + 1; j < dim; j++) {
                    lu[i][j] -= factor * lu[k][j];
                }
            }
        }

        return new double[] { sign, logAbs };
    }

    /**
     * Lower (or upper) triangular – all elements above (below) the diagonal exactly zero.
     */
    private boolean isTriangular(final Object matrix, final boolean lower) {
        for (int j = 0, nbCols = library.getColDim(matrix); j < nbCols; j++) {
            for (int i = 0, nbRows = library.getRowDim(matrix); i < nbRows; i++) {
                // NaN must fail – it is not zero
                if ((lower ? i < j : i > j) && library.get(matrix, i, j) != 0.0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * [matrix][vector] or, if transpose, [matrix]<sup>T</sup>[vector]
     */
    private double[] multiply(final Object matrix, final boolean transpose, final double[] vector) {

        int nbRows = library.getRowDim(matrix);
        int nbCols = library.getColDim(matrix);

        double[] retVal = new double[transpose ? nbCols : nbRows];

        for (int j = 0; j < nbCols; j++) {
            for (int i = 0; i < nbRows; i++) {
                double value = library.get(matrix, i, j);
                if (transpose) {
                    retVal[j] += value * vector[i];
                } else {
                    retVal[i] += value * vector[j];
                }
            }
        }

        return retVal;
    }

    /**
     * Frobenius norm
     */
    private double norm(final Object matrix) {
        double retVal = 0.0;
        for (int j = 0, nbCols = library.getColDim(matrix); j < nbCols; j++) {
            for (int i = 0, nbRows = library.getRowDim(matrix); i < nbRows; i++) {
                double value = library.get(matrix, i, j);
                retVal += value * value;
            }
        }
        return Math.sqrt(retVal);
    }

    /**
     * {@link #determinant(Object)}, calculated once per matrix.
     */
    private double[] referenceDeterminant(final Object matrix) {
        if (myReferenceMatrix != matrix) {
            myReferenceDeterminant = this.determinant(matrix);
            myReferenceMatrix = matrix;
        }
        return myReferenceDeterminant;
    }

    /**
     * A square permutation matrix – every element 0 or 1, and exactly one 1 in each row and in each column.
     */
    private void verifyPermutation(final String what, final Object matrix) throws BenchmarkRequirementsException {

        int dim = library.getRowDim(matrix);

        int[] rowCounts = new int[dim];
        int[] colCounts = new int[dim];

        for (int j = 0; j < dim; j++) {
            for (int i = 0; i < dim; i++) {
                double value = library.get(matrix, i, j);
                if (value == 1.0) {
                    rowCounts[i]++;
                    colCounts[j]++;
                } else if (value != 0.0) {
                    throw new BenchmarkRequirementsException(what + " is not a permutation matrix! Element (" + i + "," + j + ") is " + value);
                }
            }
        }

        for (int k = 0; k < dim; k++) {
            if (rowCounts[k] != 1 || colCounts[k] != 1) {
                throw new BenchmarkRequirementsException(
                        what + " is not a permutation matrix! Row " + k + " has " + rowCounts[k] + " ones, and column " + k + " has " + colCounts[k]);
            }
        }
    }

    private void verifyShape(final String what, final Object matrix, final int nbRows, final int nbCols) throws BenchmarkRequirementsException {
        if (matrix == null) {
            throw new BenchmarkRequirementsException(what + " is missing!");
        }
        int rows = library.getRowDim(matrix);
        int cols = library.getColDim(matrix);
        if (rows != nbRows || cols != nbCols) {
            throw new BenchmarkRequirementsException(what + " is " + rows + "x" + cols + " but should be " + nbRows + "x" + nbCols);
        }
    }

    /**
     * Element access, for verification, independent of the operations being verified.
     */
    protected double doubleValue(final Object matrix, final int row, final int col) {
        return library.get(matrix, row, col);
    }

    /**
     * Checks a Cholesky decomposition, [A] = [L][L]<sup>T</sup> with [L] lower triangular – or, equivalently,
     * [A] = [R]<sup>T</sup>[R] with [R] = [L]<sup>T</sup> upper triangular, either is accepted – and with a
     * positive diagonal. The reconstruction is checked by multiplying with a random vector.
     */
    protected void verifyCholeskyDecomposition(final Object original, final Object factor) throws BenchmarkRequirementsException {

        int dim = library.getRowDim(original);

        this.verifyShape("Cholesky factor", factor, dim, dim);

        boolean lower = this.isTriangular(factor, true);
        if (!lower && !this.isTriangular(factor, false)) {
            throw new BenchmarkRequirementsException("Cholesky factor is not triangular!");
        }

        for (int ij = 0; ij < dim; ij++) {
            double value = library.get(factor, ij, ij);
            if (!(value > 0.0)) {
                throw new BenchmarkRequirementsException("Cholesky factor is wrong! Diagonal element " + ij + " is " + value);
            }
        }

        // [L][L]^T[x] or [R]^T[R][x]
        double[] vector = MatrixBenchmarkOperation.random(dim);
        double[] reconstructed = this.multiply(factor, !lower, this.multiply(factor, lower, vector));

        double normFactor = this.norm(factor);
        MatrixBenchmarkOperation.check("Cholesky decomposition",
                MatrixBenchmarkOperation.norm(MatrixBenchmarkOperation.subtract(reconstructed, this.multiply(original, false, vector))),
                dim * normFactor * normFactor * MatrixBenchmarkOperation.norm(vector));
    }

    /**
     * Compared with a plain Java (LU decomposition with partial pivoting) calculation. Large determinants
     * overflow – then the result should be infinite with the correct sign.
     */
    protected void verifyDeterminant(final Object matrix, final double determinant) throws BenchmarkRequirementsException {

        int dim = library.getRowDim(matrix);

        double[] reference = this.determinant(matrix);
        double sign = reference[0];
        double logAbs = reference[1];

        if (Math.signum(determinant) != sign) {
            throw new BenchmarkRequirementsException("Determinant is wrong! " + determinant + " has the wrong sign.");
        }
        if (Double.isInfinite(determinant)) {
            if (logAbs < Math.log(Double.MAX_VALUE)) {
                throw new BenchmarkRequirementsException("Determinant is wrong! It's infinite, but should be " + sign * Math.exp(logAbs));
            }
        } else {
            // Determinants are sensitive to rounding errors – compare the logarithms with a tolerance scaled by dim²
            MatrixBenchmarkOperation.check("Determinant", Math.abs(Math.log(Math.abs(determinant)) - logAbs), dim * dim);
        }
    }

    /**
     * Checks that [original][V] = [V][D] and that [V] is orthonormal, [V]<sup>T</sup>[V] = [I], by
     * multiplying with a random vector.
     */
    protected void verifyEigenpairs(final Object original, final Object eigenvectors, final Object eigenvalues) throws BenchmarkRequirementsException {

        int dim = library.getRowDim(original);

        this.verifyShape("Eigenvectors", eigenvectors, dim, dim);
        this.verifyShape("Eigenvalues", eigenvalues, dim, dim);

        double[] vector = MatrixBenchmarkOperation.random(dim);
        double[] image = this.multiply(eigenvectors, false, vector);

        double[] left = this.multiply(original, false, image);
        double[] right = this.multiply(eigenvectors, false, this.multiply(eigenvalues, false, vector));

        double normEigenvectors = this.norm(eigenvectors);
        MatrixBenchmarkOperation.check("Eigenpairs", MatrixBenchmarkOperation.norm(MatrixBenchmarkOperation.subtract(left, right)),
                dim * this.norm(original) * normEigenvectors * MatrixBenchmarkOperation.norm(vector));

        double[] identity = this.multiply(eigenvectors, true, image);
        MatrixBenchmarkOperation.check("Eigenvectors", MatrixBenchmarkOperation.norm(MatrixBenchmarkOperation.subtract(identity, vector)),
                dim * normEigenvectors * normEigenvectors * MatrixBenchmarkOperation.norm(vector));
    }

    /**
     * Checks the eigenvalues, a dim x 1 matrix, of a symmetric matrix by invariants: their sum is the trace,
     * the sum of their squares is the square of the Frobenius norm, and their product is the determinant
     * (compared as in {@link #verifyDeterminant(Object, double)}, with the logarithms).
     */
    protected void verifyEigenvalues(final Object original, final Object eigenvalues) throws BenchmarkRequirementsException {

        int dim = library.getRowDim(original);

        this.verifyShape("Eigenvalues", eigenvalues, dim, 1);

        double trace = 0.0;
        double sum = 0.0;
        double sumOfSquares = 0.0;
        double sign = 1.0;
        double logAbs = 0.0;
        for (int i = 0; i < dim; i++) {
            double value = library.get(eigenvalues, i, 0);
            trace += library.get(original, i, i);
            sum += value;
            sumOfSquares += value * value;
            sign = value < 0.0 ? -sign : sign;
            logAbs += Math.log(Math.abs(value));
        }

        double normOriginal = this.norm(original);
        MatrixBenchmarkOperation.check("Eigenvalues (trace)", Math.abs(sum - trace), dim * normOriginal);
        MatrixBenchmarkOperation.check("Eigenvalues (Frobenius norm)", Math.abs(sumOfSquares - normOriginal * normOriginal), dim * normOriginal * normOriginal);

        double[] reference = this.referenceDeterminant(original);
        if (sign != reference[0]) {
            throw new BenchmarkRequirementsException("Eigenvalues are wrong! Their product has the wrong sign.");
        }
        MatrixBenchmarkOperation.check("Eigenvalues (determinant)", Math.abs(logAbs - reference[1]), dim * dim);
    }

    /**
     * Element by element comparison with the expected values.
     */
    protected void verifyElements(final Object actual, final int nbRows, final int nbCols, final ElementFunction expected)
            throws BenchmarkRequirementsException {

        this.verifyShape("Result", actual, nbRows, nbCols);

        for (int j = 0; j < nbCols; j++) {
            for (int i = 0; i < nbRows; i++) {
                double value = library.get(actual, i, j);
                double reference = expected.invoke(i, j);
                double difference = Math.abs(value - reference);
                // NaN must fail – "difference > limit" alone would let it pass
                if (Double.isNaN(difference) || difference > TOLERANCE * Math.abs(reference)) {
                    throw new BenchmarkRequirementsException("Result is wrong! Element (" + i + "," + j + ") is " + value + " but should be " + reference);
                }
            }
        }
    }

    /**
     * Checks that [original][inverse] = [I] by multiplying with a random vector.
     */
    protected void verifyInverse(final Object original, final Object inverse) throws BenchmarkRequirementsException {

        int dim = library.getRowDim(original);

        this.verifyShape("Inverse", inverse, dim, dim);

        double[] vector = MatrixBenchmarkOperation.random(dim);
        double[] residual = MatrixBenchmarkOperation.subtract(this.multiply(original, false, this.multiply(inverse, false, vector)), vector);

        MatrixBenchmarkOperation.check("Inverse", MatrixBenchmarkOperation.norm(residual),
                dim * this.norm(original) * this.norm(inverse) * MatrixBenchmarkOperation.norm(vector));
    }

    /**
     * One step of a Kalman filter: predict, [x]<sup>-</sup> = [F][x] and [P]<sup>-</sup> =
     * [F][P][F]<sup>T</sup> + [Q], then update with the measurement [z]. With the measurement noise [R]
     * diagonal, the update can be checked without inverting anything: the updated covariance is
     * [P]<sup>+</sup> = (([P]<sup>-</sup>)<sup>-1</sup> + [H]<sup>T</sup>[R]<sup>-1</sup>[H])<sup>-1</sup>,
     * so [P]<sup>-</sup>[v] = [P]<sup>+</sup>([v] + [H]<sup>T</sup>[R]<sup>-1</sup>[H][P]<sup>-</sup>[v]) for
     * a random vector [v], and the updated state is [x]<sup>+</sup> = [x]<sup>-</sup> +
     * [P]<sup>+</sup>[H]<sup>T</sup>[R]<sup>-1</sup>([z] - [H][x]<sup>-</sup>).
     */
    protected void verifyKalmanUpdate(final Object transition, final Object processNoise, final Object observation, final Object measurementNoise,
            final Object state, final Object covariance, final Object measurement, final Object updatedState, final Object updatedCovariance)
            throws BenchmarkRequirementsException {

        int dim = library.getRowDim(transition);
        int nbMeasurements = library.getRowDim(observation);

        this.verifyShape("Updated state", updatedState, dim, 1);
        this.verifyShape("Updated covariance", updatedCovariance, dim, dim);

        double[] noise = new double[nbMeasurements];
        for (int i = 0; i < nbMeasurements; i++) {
            noise[i] = library.get(measurementNoise, i, i);
        }

        // An upper bound on the norm of the predicted covariance
        double normTransition = this.norm(transition);
        double normPredicted = normTransition * normTransition * this.norm(covariance) + this.norm(processNoise);
        double normUpdated = this.norm(updatedCovariance);

        double[] vector = MatrixBenchmarkOperation.random(dim);
        double[] predicted = MatrixBenchmarkOperation.add(
                this.multiply(transition, false, this.multiply(covariance, false, this.multiply(transition, true, vector))),
                this.multiply(processNoise, false, vector));
        double[] weighted = this.multiply(observation, false, predicted);
        for (int i = 0; i < nbMeasurements; i++) {
            weighted[i] /= noise[i];
        }
        double[] information = this.multiply(observation, true, weighted);

        double[] residual = MatrixBenchmarkOperation.subtract(predicted,
                this.multiply(updatedCovariance, false, MatrixBenchmarkOperation.add(vector, information)));
        MatrixBenchmarkOperation.check("Updated covariance", MatrixBenchmarkOperation.norm(residual),
                dim * (normPredicted + normUpdated) * (MatrixBenchmarkOperation.norm(vector) + MatrixBenchmarkOperation.norm(information)));

        double[] predictedState = this.multiply(transition, false, this.column(state, 0));
        double[] innovation = MatrixBenchmarkOperation.subtract(this.column(measurement, 0), this.multiply(observation, false, predictedState));
        for (int i = 0; i < nbMeasurements; i++) {
            innovation[i] /= noise[i];
        }
        double[] gain = this.multiply(observation, true, innovation);

        double[] expected = MatrixBenchmarkOperation.add(predictedState, this.multiply(updatedCovariance, false, gain));
        MatrixBenchmarkOperation.check("Updated state",
                MatrixBenchmarkOperation.norm(MatrixBenchmarkOperation.subtract(this.column(updatedState, 0), expected)),
                dim * (MatrixBenchmarkOperation.norm(predictedState) + (normPredicted + normUpdated) * MatrixBenchmarkOperation.norm(gain)));
    }

    /**
     * Checks an LU decomposition with row pivoting, [P][A] = [L][U]: [P] a permutation matrix, [L] lower
     * triangular and [U] upper triangular, all dim x dim. The reconstruction is checked by multiplying with a
     * random vector.
     */
    protected void verifyLUDecomposition(final Object original, final Object permutation, final Object lower, final Object upper)
            throws BenchmarkRequirementsException {

        int dim = library.getRowDim(original);

        this.verifyShape("P", permutation, dim, dim);
        this.verifyShape("L", lower, dim, dim);
        this.verifyShape("U", upper, dim, dim);

        this.verifyPermutation("P", permutation);
        if (!this.isTriangular(lower, true)) {
            throw new BenchmarkRequirementsException("L is not lower triangular!");
        }
        if (!this.isTriangular(upper, false)) {
            throw new BenchmarkRequirementsException("U is not upper triangular!");
        }

        double[] vector = MatrixBenchmarkOperation.random(dim);
        double[] left = this.multiply(permutation, false, this.multiply(original, false, vector));
        double[] right = this.multiply(lower, false, this.multiply(upper, false, vector));

        MatrixBenchmarkOperation.check("LU decomposition", MatrixBenchmarkOperation.norm(MatrixBenchmarkOperation.subtract(left, right)),
                dim * this.norm(lower) * this.norm(upper) * MatrixBenchmarkOperation.norm(vector));
    }

    /**
     * Rank deficient least squares, with the body as
     * {@link #makeRankDeficient(int, double[][], MatrixBenchmarkLibrary)}: the solution should satisfy the
     * normal equations, [A]<sup>T</sup>([A][x] - [b]) = 0, and be orthogonal to the null space of [A] – the
     * minimum norm solution. The null space is spanned by the columns of [-M; I], so x[rank + j] should be
     * the sum over k of multipliers[k][j] times x[k].
     */
    protected void verifyMinimumNormSolution(final Object body, final Object rhs, final Object solution, final double[][] multipliers)
            throws BenchmarkRequirementsException {

        int nbEquations = library.getRowDim(body);
        int nbVariables = library.getColDim(body);
        int rank = multipliers.length;

        this.verifyShape("Solution", solution, nbVariables, 1);

        double[] x = this.column(solution, 0);
        double[] b = this.column(rhs, 0);

        double normBody = this.norm(body);
        double[] residual = MatrixBenchmarkOperation.subtract(this.multiply(body, false, x), b);
        MatrixBenchmarkOperation.check("Least squares solution", MatrixBenchmarkOperation.norm(this.multiply(body, true, residual)),
                nbEquations * normBody * (normBody * MatrixBenchmarkOperation.norm(x) + MatrixBenchmarkOperation.norm(b)));

        double normMultipliers = 0.0;
        double[] difference = new double[nbVariables - rank];
        for (int j = 0; j < difference.length; j++) {
            double value = x[rank + j];
            for (int k = 0; k < rank; k++) {
                value -= multipliers[k][j] * x[k];
                normMultipliers += multipliers[k][j] * multipliers[k][j];
            }
            difference[j] = value;
        }
        MatrixBenchmarkOperation.check("Minimum norm solution", MatrixBenchmarkOperation.norm(difference),
                nbVariables * (Math.sqrt(normMultipliers) + 1.0) * MatrixBenchmarkOperation.norm(x));
    }

    /**
     * Checks that op[left] op[right] = [product] by multiplying with a random vector (Freivalds' algorithm).
     */
    protected void verifyProduct(final Object left, final boolean transpL, final Object right, final boolean transpR, final Object product)
            throws BenchmarkRequirementsException {

        int nbRows = transpL ? library.getColDim(left) : library.getRowDim(left);
        int complexity = transpL ? library.getRowDim(left) : library.getColDim(left);
        int nbCols = transpR ? library.getRowDim(right) : library.getColDim(right);

        this.verifyShape("Product", product, nbRows, nbCols);

        double[] vector = MatrixBenchmarkOperation.random(nbCols);
        double[] expected = this.multiply(left, transpL, this.multiply(right, transpR, vector));
        double[] actual = this.multiply(product, false, vector);

        MatrixBenchmarkOperation.check("Product", MatrixBenchmarkOperation.norm(MatrixBenchmarkOperation.subtract(actual, expected)),
                complexity * this.norm(left) * this.norm(right) * MatrixBenchmarkOperation.norm(vector));
    }

    /**
     * Checks that [original][pseudoinverse][original] = [original] by multiplying with a random vector.
     */
    protected void verifyPseudoinverse(final Object original, final Object pseudoinverse) throws BenchmarkRequirementsException {

        int nbRows = library.getRowDim(original);
        int nbCols = library.getColDim(original);

        this.verifyShape("Pseudoinverse", pseudoinverse, nbCols, nbRows);

        double[] vector = MatrixBenchmarkOperation.random(nbCols);
        double[] image = this.multiply(original, false, vector);
        double[] residual = MatrixBenchmarkOperation.subtract(this.multiply(original, false, this.multiply(pseudoinverse, false, image)), image);

        double normOriginal = this.norm(original);
        MatrixBenchmarkOperation.check("Pseudoinverse", MatrixBenchmarkOperation.norm(residual),
                Math.max(nbRows, nbCols) * normOriginal * this.norm(pseudoinverse) * normOriginal * MatrixBenchmarkOperation.norm(vector));
    }

    /**
     * Checks a QR decomposition, [A] = [Q][R] with [A] m x n and k = min(m, n): [Q] m x k with orthonormal
     * columns and [R] k x n upper triangular. The reconstruction and the orthonormality are checked by
     * multiplying with random vectors.
     */
    protected void verifyQRDecomposition(final Object original, final Object orthogonal, final Object triangular) throws BenchmarkRequirementsException {

        int nbRows = library.getRowDim(original);
        int nbCols = library.getColDim(original);
        int rank = Math.min(nbRows, nbCols);

        this.verifyShape("Q", orthogonal, nbRows, rank);
        this.verifyShape("R", triangular, rank, nbCols);

        if (!this.isTriangular(triangular, false)) {
            throw new BenchmarkRequirementsException("R is not upper triangular!");
        }

        double[] vector = MatrixBenchmarkOperation.random(nbCols);
        double[] reconstructed = this.multiply(orthogonal, false, this.multiply(triangular, false, vector));
        MatrixBenchmarkOperation.check("QR decomposition",
                MatrixBenchmarkOperation.norm(MatrixBenchmarkOperation.subtract(reconstructed, this.multiply(original, false, vector))),
                Math.max(nbRows, nbCols) * this.norm(original) * MatrixBenchmarkOperation.norm(vector));

        double[] coordinates = MatrixBenchmarkOperation.random(rank);
        double normOrthogonal = this.norm(orthogonal);
        MatrixBenchmarkOperation.check("Q",
                MatrixBenchmarkOperation.norm(
                        MatrixBenchmarkOperation.subtract(this.multiply(orthogonal, true, this.multiply(orthogonal, false, coordinates)), coordinates)),
                nbRows * normOrthogonal * normOrthogonal * MatrixBenchmarkOperation.norm(coordinates));
    }

    /**
     * Ridge regression, minimise |[X][&beta;] - [y]|<sup>2</sup> + &lambda;|[&beta;]|<sup>2</sup>: the
     * solution should satisfy [X]<sup>T</sup>([X][&beta;] - [y]) + &lambda;[&beta;] = 0.
     */
    protected void verifyRidgeSolution(final Object design, final Object observations, final double lambda, final Object solution)
            throws BenchmarkRequirementsException {

        int nbEquations = library.getRowDim(design);
        int nbVariables = library.getColDim(design);

        this.verifyShape("Solution", solution, nbVariables, 1);

        double[] beta = this.column(solution, 0);
        double[] y = this.column(observations, 0);

        double[] residual = this.multiply(design, true, MatrixBenchmarkOperation.subtract(this.multiply(design, false, beta), y));
        for (int j = 0; j < nbVariables; j++) {
            residual[j] += lambda * beta[j];
        }

        double normDesign = this.norm(design);
        double normBeta = MatrixBenchmarkOperation.norm(beta);
        MatrixBenchmarkOperation.check("Ridge regression solution", MatrixBenchmarkOperation.norm(residual),
                nbEquations * (normDesign * (normDesign * normBeta + MatrixBenchmarkOperation.norm(y)) + lambda * normBeta));
    }

    /**
     * Checks a thin singular value decomposition, [A] = [U][S][V]<sup>T</sup> with [A] m x n and k = min(m,
     * n): [U] m x k with orthonormal columns, [S] k x k diagonal and non-negative, [V]<sup>T</sup> k x n with
     * orthonormal rows. The reconstruction and the orthonormality are checked by multiplying with random
     * vectors.
     */
    protected void verifySingularValueDecomposition(final Object original, final Object left, final Object singular, final Object right)
            throws BenchmarkRequirementsException {

        int nbRows = library.getRowDim(original);
        int nbCols = library.getColDim(original);
        int rank = Math.min(nbRows, nbCols);

        this.verifyShape("U", left, nbRows, rank);
        this.verifyShape("S", singular, rank, rank);
        this.verifyShape("V^T", right, rank, nbCols);

        for (int j = 0; j < rank; j++) {
            for (int i = 0; i < rank; i++) {
                double value = library.get(singular, i, j);
                if (i == j ? !(value >= 0.0) : value != 0.0) {
                    throw new BenchmarkRequirementsException("S is wrong! Element (" + i + "," + j + ") is " + value);
                }
            }
        }

        double[] vector = MatrixBenchmarkOperation.random(nbCols);
        double[] reconstructed = this.multiply(left, false, this.multiply(singular, false, this.multiply(right, false, vector)));
        MatrixBenchmarkOperation.check("Singular value decomposition",
                MatrixBenchmarkOperation.norm(MatrixBenchmarkOperation.subtract(reconstructed, this.multiply(original, false, vector))),
                Math.max(nbRows, nbCols) * this.norm(original) * MatrixBenchmarkOperation.norm(vector));

        double[] coordinates = MatrixBenchmarkOperation.random(rank);
        double normLeft = this.norm(left);
        MatrixBenchmarkOperation.check("U",
                MatrixBenchmarkOperation
                        .norm(MatrixBenchmarkOperation.subtract(this.multiply(left, true, this.multiply(left, false, coordinates)), coordinates)),
                nbRows * normLeft * normLeft * MatrixBenchmarkOperation.norm(coordinates));
        double normRight = this.norm(right);
        MatrixBenchmarkOperation.check("V",
                MatrixBenchmarkOperation
                        .norm(MatrixBenchmarkOperation.subtract(this.multiply(right, false, this.multiply(right, true, coordinates)), coordinates)),
                nbCols * normRight * normRight * MatrixBenchmarkOperation.norm(coordinates));
    }

    /**
     * Checks the singular values, a dim x 1 matrix, of a square matrix: they should be non-negative, the sum
     * of their squares is the square of the Frobenius norm, and their product is the absolute value of the
     * determinant (compared as in {@link #verifyDeterminant(Object, double)}, with the logarithms).
     */
    protected void verifySingularValues(final Object original, final Object singularValues) throws BenchmarkRequirementsException {

        int dim = library.getRowDim(original);

        this.verifyShape("Singular values", singularValues, dim, 1);

        double sumOfSquares = 0.0;
        double logAbs = 0.0;
        for (int i = 0; i < dim; i++) {
            double value = library.get(singularValues, i, 0);
            if (!(value >= 0.0)) {
                throw new BenchmarkRequirementsException("Singular values are wrong! " + value + " is not non-negative.");
            }
            sumOfSquares += value * value;
            logAbs += Math.log(value);
        }

        double normOriginal = this.norm(original);
        MatrixBenchmarkOperation.check("Singular values (Frobenius norm)", Math.abs(sumOfSquares - normOriginal * normOriginal),
                dim * normOriginal * normOriginal);
        MatrixBenchmarkOperation.check("Singular values (determinant)", Math.abs(logAbs - this.referenceDeterminant(original)[1]), dim * dim);
    }

    /**
     * Square systems: the residual should be small. Least squares (more equations than variables): the
     * residual should be orthogonal to the columns of the body.
     */
    protected void verifySolution(final Object body, final Object rhs, final Object solution) throws BenchmarkRequirementsException {

        int nbEquations = library.getRowDim(body);
        int nbVariables = library.getColDim(body);
        int nbSolutions = library.getColDim(rhs);

        this.verifyShape("Solution", solution, nbVariables, nbSolutions);

        double normBody = this.norm(body);

        for (int s = 0; s < nbSolutions; s++) {

            double[] x = this.column(solution, s);
            double[] b = this.column(rhs, s);
            double[] residual = MatrixBenchmarkOperation.subtract(this.multiply(body, false, x), b);
            double scale = normBody * MatrixBenchmarkOperation.norm(x) + MatrixBenchmarkOperation.norm(b);

            if (nbEquations == nbVariables) {
                MatrixBenchmarkOperation.check("Solution", MatrixBenchmarkOperation.norm(residual), nbVariables * scale);
            } else {
                MatrixBenchmarkOperation.check("Least squares solution", MatrixBenchmarkOperation.norm(this.multiply(body, true, residual)),
                        nbEquations * normBody * scale);
            }
        }
    }

    protected void verifyStateless(final Class<?> clazz) throws BenchmarkRequirementsException {

        for (final Field tmpField : clazz.getDeclaredFields()) {
            if (!"this$0".equals(tmpField.getName())) {
                throw new BenchmarkRequirementsException(tmpField.toString());
            }
        }

        final Class<?> tmpSuperclazz = clazz.getSuperclass();
        if (tmpSuperclazz != null) {
            this.verifyStateless(tmpSuperclazz);
        }

        for (final Class<?> tmpInterface : clazz.getInterfaces()) {
            this.verifyStateless(tmpInterface);
        }
    }

}
