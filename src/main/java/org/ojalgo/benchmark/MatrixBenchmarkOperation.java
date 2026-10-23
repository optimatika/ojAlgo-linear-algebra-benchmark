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

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
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
 * a previous invocation.</li>
 * <li>After each iteration, outside the measured time, the result of the last invocation is verified,
 * independently of the library. The tolerances are generous – the verification is there to catch wrong
 * results, not to rank accuracy. A library that produces a wrong result is reported as a failure, and gets no
 * score.</li>
 * <li>Any switching between alternative implementations (by matrix size, single or multi-threaded…) must be
 * done by the library itself – the adapters make one, static, choice per operation.</li>
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
     * The size range the benchmarks' main methods run.
     */
    protected static final Size SIZE = Size.MEDIUM;

    /**
     * Allowed (normwise relative) error, per dimension, when verifying. Generous – the verification is there
     * to catch wrong results, not to rank accuracy.
     */
    static final double TOLERANCE = 16.0 * Math.ulp(1.0);

    /**
     * Settings for results to publish, that still run reasonably fast: 1 fork, throughput, CSV results. The
     * heap is large enough for decompositions of 10000x10000 matrices. The number of forks, the iteration
     * time, the number of iterations and the time unit are set per {@link Size}.
     */
    public static ChainedOptionsBuilder options() {
        return new OptionsBuilder().forks(1).warmupIterations(3).measurementIterations(5).mode(Mode.Throughput).timeUnit(TimeUnit.MINUTES)
                .timeout(new TimeValue(10L, TimeUnit.MINUTES)).jvmArgs("-Xmx16g").resultFormat(ResultFormatType.CSV);
    }

    /**
     * Runs only the given benchmark class – the include pattern is its full name, so that e.g. Transpose
     * doesn't also run MultiplyTransposedLeft/Right.
     */
    public static void run(final ChainedOptionsBuilder options, final Class<?> clazz) throws RunnerException {
        new Runner(options.include(Pattern.quote(clazz.getName() + ".")).build()).run();
    }

    /**
     * Runs the benchmark class with {@link #options()}, configured for the given size range – its sizes,
     * libraries, time unit, forks, iteration time, iterations and timeout. The results are written to a CSV
     * file named after the class and the size range, e.g. "FillByMultiplying-MEDIUM.csv".
     */
    public static void run(final Class<?> clazz, final Size size) throws RunnerException {
        ChainedOptionsBuilder options = MatrixBenchmarkOperation.options().param("dim", size.dims).param("lib", size.libs).timeUnit(size.timeUnit)
                .forks(size.forks).warmupTime(size.iterationTime).warmupIterations(size.warmupIterations).measurementTime(size.iterationTime)
                .measurementIterations(size.measurementIterations).timeout(size.timeout).result(clazz.getSimpleName() + "-" + size.name() + ".csv");
        MatrixBenchmarkOperation.run(options, clazz);
    }

    private static void check(final String what, final double error, final double scale) throws BenchmarkRequirementsException {
        double limit = TOLERANCE * scale;
        // NaN must fail – "error > limit" alone would let it pass
        if (Double.isNaN(error) || Double.isNaN(limit) || error > limit) {
            throw new BenchmarkRequirementsException(what + " is wrong! Error " + error + " > " + limit + ", the relative error is " + error / scale);
        }
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
     * Compared with a plain Java (LU decomposition with partial pivoting) calculation. Large determinants
     * overflow – then the result should be infinite with the correct sign.
     */
    protected void verifyDeterminant(final Object matrix, final double determinant) throws BenchmarkRequirementsException {

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
