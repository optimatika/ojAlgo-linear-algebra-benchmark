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

import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * The forked JVMs grant native access so that ojAlgo can use native code. Whether it actually does is
 * decided by the library being benchmarked – "ojAlgo" (pure Java) or "ojAlgoNative".
 */
@Fork(jvmArgsAppend = "--enable-native-access=ALL-UNNAMED")
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

    /*
     * Reference sets of sizes (the "dim" parameter). Annotation values can't refer to these, they're here to
     * copy from when temporarily changing the @Param values.
     */
    private static final String[] _10_1000 = { "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000" };
    private static final String[] FULL = { "1", "2", "3", "4", "5", "8", "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000",
            "1024", "2000", "2048", "4096", "5000", "8192", "10000" };
    private static final String[] JVM = { "100", "150", "200", "350", "500", "750", "1000" };

    /**
     * Allowed (normwise relative) error, per dimension, when verifying. Generous – the verification is there
     * to catch wrong results, not to rank accuracy.
     */
    private static final double TOLERANCE = 16.0 * Math.ulp(1.0);

    static final TimeValue ITERATION_TIME = new TimeValue(10L, TimeUnit.SECONDS);
    static final TimeValue TIMEOUT = new TimeValue(1L, TimeUnit.MINUTES);

    public static ChainedOptionsBuilder options() {
        return new OptionsBuilder().forks(1).warmupIterations(2).measurementIterations(3).mode(Mode.Throughput).timeUnit(TimeUnit.MINUTES)
                .warmupTime(ITERATION_TIME).measurementTime(ITERATION_TIME).timeout(TIMEOUT).jvmArgs("-Xmx8g").resultFormat(ResultFormatType.CSV);
    }

    public static void run(final ChainedOptionsBuilder options, final Class<?> clazz) throws RunnerException {
        new Runner(options.include(clazz.getSimpleName()).build()).run();
    }

    public static void run(final Class<?> clazz) throws RunnerException {
        MatrixBenchmarkOperation.run(MatrixBenchmarkOperation.options(), clazz);
    }

    public static void run(final Class<?> clazz, final TimeUnit timeUnit) throws RunnerException {
        MatrixBenchmarkOperation.run(MatrixBenchmarkOperation.options().timeUnit(timeUnit), clazz);
    }

    protected final static Object makeRandom(final int numberOfRows, final int numberOfColumns, final MatrixBenchmarkLibrary<?, ?> contestant) {
        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder tmpSupplier = contestant.getMatrixBuilder(numberOfRows, numberOfColumns);
        for (int j = 0; j < numberOfColumns; j++) {
            for (int i = 0; i < numberOfRows; i++) {
                tmpSupplier.set(i, j, Math.random());
            }
        }
        return tmpSupplier.get();
    }

    protected final static Object makeSPD(final int size, final MatrixBenchmarkLibrary<?, ?> contestant) {

        final double[] random = new double[size];

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(size, size);

        for (int i = 0; i < size; i++) {

            random[i] = Math.random();

            for (int j = 0; j < i; j++) {
                builder.set(i, j, random[i] * random[j]);
                builder.set(j, i, random[j] * random[i]);
            }
            builder.set(i, i, random[i] + 1.0);
        }

        return builder.get();
    }

    private static void check(final String what, final double error, final double scale) throws BenchmarkRequirementsException {
        if (!(error <= TOLERANCE * scale)) { // Also catches NaN
            throw new BenchmarkRequirementsException(what + " is wrong! Error " + error + " > " + TOLERANCE * scale + ", the relative error is " + error / scale);
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

    protected final static Object makeZero(final int numberOfRows, final int numberOfColumns, final MatrixBenchmarkLibrary<?, ?> contestant) {
        return contestant.getMatrixBuilder(numberOfRows, numberOfColumns).get();
    }

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
     * Element by element comparison with the expected values.
     */
    protected void verifyElements(final Object actual, final int nbRows, final int nbCols, final ElementFunction expected)
            throws BenchmarkRequirementsException {

        this.verifyShape("Result", actual, nbRows, nbCols);

        for (int j = 0; j < nbCols; j++) {
            for (int i = 0; i < nbRows; i++) {
                double value = library.get(actual, i, j);
                double reference = expected.invoke(i, j);
                if (!(Math.abs(value - reference) <= TOLERANCE * Math.abs(reference))) {
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

}
