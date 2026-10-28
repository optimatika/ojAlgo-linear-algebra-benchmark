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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.ojalgo.benchmark.MatrixBenchmarkOperation.*;
import org.ojalgo.benchmark.library.EJML;
import org.ojalgo.benchmark.library.Hipparchus;
import org.ojalgo.benchmark.library.ojAlgo;
import org.ojalgo.benchmark.library.SMILE;
import org.ojalgo.netio.BasicLogger;

/**
 * The adapter of one library to the benchmarks. Each operation is a lambda calling the library, written by
 * the rules in {@link MatrixBenchmarkOperation}: make the best possible use of the library's features, but no
 * switching between alternative implementations in the adapter code – one, static, choice per operation.
 * <p>
 * The type paramater I is intended to be some high level interface that defines the shape of the matrix as
 * well as how to get individual elements (as a minimum).
 * </p>
 * <p>
 * The type paramater T should be set to some specific, fully mutable, implementation.
 * </p>
 *
 * @author apete
 */
public abstract class MatrixBenchmarkLibrary<I, T extends I> {

    public abstract class ElementsExtractor {

        public abstract double get(int row, int col);

    }

    public abstract class MatrixBuilder implements Supplier<I> {

        public abstract MatrixBuilder set(int row, int col, double value);

    }

    /**
     * Libraries are instantiated on demand, and only the one being benchmarked, since some of them configure
     * the JVM (ojAlgo's native code is switched on or off when the library is instantiated).
     */
    private static final Map<String, Supplier<MatrixBenchmarkLibrary<?, ?>>> LIBRARIES = new HashMap<>();

    static {
        // The names are the values of the benchmarks' "lib" parameter
        LIBRARIES.put("EJML", EJML::new);
        LIBRARIES.put("Hipparchus", Hipparchus::new);
        LIBRARIES.put("ojAlgo-JVM", ojAlgo.JVM::new);
        LIBRARIES.put("ojAlgo-Native", ojAlgo.Native::new);
        LIBRARIES.put("SMILE-JVM", SMILE.JVM::new);
        LIBRARIES.put("SMILE-Native", SMILE.Native::new);
    }

    public static MatrixBenchmarkLibrary<?, ?> make(final String name) {
        Supplier<MatrixBenchmarkLibrary<?, ?>> constructor = LIBRARIES.get(name);
        if (constructor == null) {
            throw new IllegalArgumentException("Unknown library: " + name + ", available are " + LIBRARIES.keySet());
        }
        return constructor.get();
    }

    public MatrixBenchmarkLibrary() {
        super();
    }

    /**
     * Arrays (of decomposition factors) are multiplied together before comparing. The allowed (normwise
     * relative) error is the same as for the other verifications, {@link MatrixBenchmarkOperation#TOLERANCE}
     * per dimension, relative to the norm of the expected matrix.
     */
    @SuppressWarnings("unchecked")
    public boolean equals(final Object expected, final int dim, final Object actual) {

        I left;
        if (expected.getClass().isArray()) {
            left = this.multiply((I[]) expected);
        } else {
            left = (I) expected;
        }

        I right;
        if (actual.getClass().isArray()) {
            right = this.multiply((I[]) actual);
        } else {
            right = (I) actual;
        }

        final I diff = this.subtract(left, right);

        final double error = this.norm(diff);
        final double scale = this.norm(left);

        final boolean retVal = (error <= MatrixBenchmarkOperation.TOLERANCE * dim * scale); // Also catches NaN

        if (!retVal) {
            BasicLogger.debug("Error {} too large for {}, the relative error is {}", error, dim, error / scale);
        }

        return retVal;
    }

    public abstract MatrixBuilder getMatrixBuilder(int numberOfRows, int numberOfColumns);

    public abstract MutatingBinaryMatrixMatrixOperation<I, T> getOperationAdd();

    /**
     * The Cholesky decomposition of a symmetric positive definite dim x dim matrix: its triangular factor,
     * dim x dim – [L] lower triangular with [A] = [L][L]<sup>T</sup>, or [R] = [L]<sup>T</sup> upper
     * triangular, whichever the library provides.
     */
    public abstract DecompositionOperation<I, I> getOperationCholesky(int dim);

    public abstract PropertyOperation<I, T> getOperationDeterminant(int dim);

    /**
     * The eigenvalues, only, of a symmetric matrix – a dim x 1 matrix, in any order.
     */
    public abstract ProducingUnaryMatrixOperation<I, T> getOperationEigenvalues(int dim);

    public abstract ProducingBinaryMatrixMatrixOperation<I, T> getOperationEquationSystemSolver(int numbEquations, int numbVariables, int numbSolutions,
            boolean spd);

    public abstract DecompositionOperation<I, I> getOperationEvD(int dim);

    /**
     * <pre>
     * arg1 == left input matrix
     * arg2 == right input matrix
     * ret == product - the results of matrix multiplication [left]x[right] should end up in that matrix
     * </pre>
     *
     * @param transpL TODO
     * @param transpR TODO
     */
    public abstract MutatingBinaryMatrixMatrixOperation<I, T> getOperationFillByMultiplying(boolean transpL, boolean transpR);

    /**
     * [A]<sup>T</sup>[A] into the preallocated result – the library may exploit that it is symmetric.
     */
    public abstract MutatingUnaryMatrixOperation<I, T> getOperationGram();

    public abstract MutatingUnaryMatrixOperation<I, T> getOperationInvert(int dim, boolean spd);

    /**
     * One step, predict and update, of a Kalman filter with dim states and nbMeasurements measurements. Any
     * workspace is preallocated – only the results are written to the matrices passed in.
     */
    public abstract KalmanFilterOperation<I, T> getOperationKalmanFilter(int dim, int nbMeasurements);

    /**
     * The LU decomposition, with row pivoting, of a dim x dim matrix: [P], [L] and [U], all dim x dim, with
     * [P][A] = [L][U] – [P] a permutation matrix, [L] lower and [U] upper triangular.
     */
    public abstract DecompositionOperation<I, I> getOperationLU(int dim);

    /**
     * The minimum norm least squares solution, of a possibly rank deficient system, for a single right hand
     * side.
     */
    public abstract ProducingBinaryMatrixMatrixOperation<I, T> getOperationMinimumNormSolver(int numbEquations, int numbVariables);

    /**
     * [matrix][vector], or if transpose [matrix]<sup>T</sup>[vector], into the preallocated result – the
     * vectors are single column matrices.
     */
    public abstract MutatingBinaryMatrixMatrixOperation<I, T> getOperationMultiplyMatrixVector(boolean transpose);

    public abstract ProducingBinaryMatrixMatrixOperation<I, I> getOperationMultiplyToProduce();

    public abstract ProducingUnaryMatrixOperation<I, T> getOperationPseudoinverse(int dim);

    /**
     * The QR decomposition of a dim x dim matrix: [Q] orthogonal and [R] upper triangular, both dim x dim.
     */
    public abstract DecompositionOperation<I, I> getOperationQR(int dim);

    /**
     * Prepares (decomposes) a general square body, in setup, and returns the operation that solves for a
     * single right hand side using that decomposition.
     */
    public abstract PreparingOperation<I, T> getOperationRepeatedSolver(int dim);

    /**
     * Solves (X<sup>T</sup>X + &lambda;I)&beta; = X<sup>T</sup>y for &beta;. Any workspace is preallocated.
     */
    public abstract ProducingBinaryMatrixMatrixOperation<I, T> getOperationRidgeRegression(int numbEquations, int numbVariables, double lambda);

    public abstract MutatingBinaryMatrixScalarOperation<I, T> getOperationScale();

    /**
     * The singular values, only, of a square matrix – a dim x 1 matrix, in any order.
     */
    public abstract ProducingUnaryMatrixOperation<I, T> getOperationSingularValues(int dim);

    public abstract DecompositionOperation<I, I> getOperationSVD(int dim);

    /**
     * Solves a symmetric indefinite system for a single right hand side – the library may exploit the
     * symmetry, if it can.
     */
    public abstract ProducingBinaryMatrixMatrixOperation<I, T> getOperationSymmetricIndefiniteSolver(int dim);

    /**
     * The thin singular value decomposition of a numbRows x numbCols matrix, numbRows &ge; numbCols: [U]
     * numbRows x numbCols, [S] and [V]<sup>T</sup> numbCols x numbCols.
     */
    public abstract DecompositionOperation<I, I> getOperationThinSVD(int numbRows, int numbCols);

    public abstract MutatingUnaryMatrixOperation<I, T> getOperationTranspose();

    protected abstract T copy(I source, T destination);

    protected abstract int countColumns(I matrix);

    protected abstract int countRows(I matrix);

    protected abstract double doubleValue(I matrix, int row, int col);

    protected abstract I[] makeArray(int length);

    @SuppressWarnings("unchecked")
    protected abstract I multiply(I... factors);

    protected abstract double norm(I matrix);

    protected abstract I subtract(I left, I right);

    /**
     * Element access, of matrices of any of the library's types, for verification – independent of the
     * operations being verified.
     */
    @SuppressWarnings("unchecked")
    final double get(final Object matrix, final int row, final int col) {
        return this.doubleValue((I) matrix, row, col);
    }

    @SuppressWarnings("unchecked")
    final int getColDim(final Object matrix) {
        return this.countColumns((I) matrix);
    }

    @SuppressWarnings("unchecked")
    final int getRowDim(final Object matrix) {
        return this.countRows((I) matrix);
    }

}
