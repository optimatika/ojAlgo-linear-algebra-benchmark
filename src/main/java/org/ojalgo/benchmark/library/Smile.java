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
package org.ojalgo.benchmark.library;

import static smile.linalg.Transpose.NO_TRANSPOSE;
import static smile.linalg.Transpose.TRANSPOSE;

import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.*;

import smile.linalg.NativeLibrary;
import smile.linalg.UPLO;
import smile.tensor.DenseMatrix;
import smile.tensor.EVD;
import smile.tensor.LU;
import smile.tensor.QR;
import smile.tensor.SVD;
import smile.tensor.ScalarType;

/**
 * SMILE (Statistical Machine Intelligence and Learning Engine) – the operations, common to {@link JVM} and
 * {@link Native}. Those only differ in SMILE's linear algebra backend, "java" or "native".
 * <p>
 * SMILE reads the backend system property every time it chooses between native and Java code, so the
 * subclasses' constructors only have to set it before the first operation.
 * <p>
 * The decompositions overwrite the matrix they're called on, so the input is copied first. That is part of
 * what is measured, as it is for the other libraries that don't decompose in place.
 */
public abstract class SMILE extends MatrixBenchmarkLibrary<DenseMatrix, DenseMatrix> {

    /**
     * SMILE – pure Java. Its linear algebra backend is set to "java", even if there is a native BLAS/LAPACK
     * to use.
     *
     * @see Native
     */
    public static final class JVM extends SMILE {

        public JVM() {

            super();

            System.setProperty(NativeLibrary.BACKEND_PROPERTY, "java");

            if (NativeLibrary.isBlasAvailable() || NativeLibrary.isLapackAvailable()) {
                throw new IllegalStateException("Not able to switch off SMILE's native backend!");
            }
        }

    }

    /**
     * SMILE – using native code (BLAS/LAPACK through the FFM API). On macOS that is the system's
     * libblas/liblapack (Accelerate, its legacy interface), otherwise OpenBLAS. SMILE's own system properties
     * choose another library – see <code>smile.linalg.NativeLibrary</code>.
     *
     * @see JVM
     */
    public static final class Native extends SMILE {

        public Native() {

            super();

            System.setProperty(NativeLibrary.BACKEND_PROPERTY, "native");

            // With the "native" backend these throw if there is no native library – fail now rather than in the benchmark
            if (!NativeLibrary.isBlasAvailable() || !NativeLibrary.isLapackAvailable()) {
                throw new IllegalStateException("No native BLAS/LAPACK available to SMILE!");
            }
        }

    }

    SMILE() {
        super();
    }

    @Override
    public MatrixBenchmarkLibrary<DenseMatrix, DenseMatrix>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final DenseMatrix myMatrix = DenseMatrix.zeros(ScalarType.Float64, numberOfRows, numberOfColumns);

            @Override
            public DenseMatrix get() {
                return myMatrix;
            }

            @Override
            public MatrixBuilder set(final int row, final int col, final double value) {
                myMatrix.set(row, col, value);
                return this;
            }

        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationAdd() {
        return (a, b, c) -> c.add(1.0, a, 1.0, b);
    }

    @Override
    public DecompositionOperation<DenseMatrix, DenseMatrix> getOperationCholesky(final int dim) {

        DenseMatrix[] ret = this.makeArray(1);

        return matrix -> {
            // UPPER – as in getOperationEquationSystemSolver, SMILE's pure Java Cholesky then runs down columns
            DenseMatrix factor = matrix.copy().withUplo(UPLO.UPPER).cholesky().lu();
            // [R] is written to the upper triangle – below the diagonal is what was copied
            for (int j = 0; j < dim; j++) {
                for (int i = j + 1; i < dim; i++) {
                    factor.set(i, j, 0.0);
                }
            }
            ret[0] = factor;
            return ret;
        };
    }

    @Override
    public PropertyOperation<DenseMatrix, DenseMatrix> getOperationDeterminant(final int dim) {
        return matA -> matA.copy().lu().det();
    }

    @Override
    public ProducingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationEigenvalues(final int dim) {
        // Flagged as symmetric, to get the symmetric algorithm
        return matrix -> matrix.copy().withUplo(UPLO.LOWER).eigen(false, false).wr();
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationEquationSystemSolver(final int numbEquations, final int numbVariables,
            final int numbSolutions, final boolean spd) {
        if (numbEquations == numbVariables) {
            if (spd) {
                return (body, rhs) -> {
                    DenseMatrix solution = rhs.copy();
                    // UPPER – SMILE's pure Java Cholesky then runs down columns (contiguous), LOWER is strided
                    body.copy().withUplo(UPLO.UPPER).cholesky().solve(solution);
                    return solution;
                };
            }
            return (body, rhs) -> {
                DenseMatrix solution = rhs.copy();
                body.copy().lu().solve(solution);
                return solution;
            };
        }
        if (numbEquations <= numbVariables) {
            return null;
        }
        return (body, rhs) -> {
            DenseMatrix solution = rhs.copy();
            body.copy().qr().solve(solution);
            // The solution is in the first numbVariables rows
            return solution.submatrix(0, 0, numbVariables, numbSolutions);
        };
    }

    @Override
    public DecompositionOperation<DenseMatrix, DenseMatrix> getOperationEvD(final int dim) {

        DenseMatrix[] ret = this.makeArray(2);

        return matrix -> {
            // Flagged as symmetric, to get the symmetric algorithm
            EVD evd = matrix.copy().withUplo(UPLO.LOWER).eigen(false, true);
            ret[0] = evd.Vr();
            ret[1] = evd.diag();
            return ret;
        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationFillByMultiplying(final boolean transpL, final boolean transpR) {
        return (left, right, product) -> DenseMatrix.mm(1.0, transpL ? TRANSPOSE : NO_TRANSPOSE, left, transpR ? TRANSPOSE : NO_TRANSPOSE, right, 0.0, product);
    }

    @Override
    public MutatingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationGram() {
        // What ata() does, but into the preallocated product
        return (matrix, product) -> DenseMatrix.mm(1.0, TRANSPOSE, matrix, NO_TRANSPOSE, matrix, 0.0, product);
    }

    @Override
    public MutatingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationInvert(final int dim, final boolean spd) {
        // Solve against the identity, directly in result – what inverse() does, without allocating the identity and the inverse
        DenseMatrix identity = DenseMatrix.eye(ScalarType.Float64, dim);
        if (spd) {
            return (matA, result) -> matA.copy().withUplo(UPLO.UPPER).cholesky().solve(this.copy(identity, result));
        }
        return (matA, result) -> matA.copy().lu().solve(this.copy(identity, result));
    }

    @Override
    public KalmanFilterOperation<DenseMatrix, DenseMatrix> getOperationKalmanFilter(final int dim, final int nbMeasurements) {

        DenseMatrix predictedState = DenseMatrix.zeros(ScalarType.Float64, dim, 1);
        DenseMatrix transformed = DenseMatrix.zeros(ScalarType.Float64, dim, dim);
        DenseMatrix predicted = DenseMatrix.zeros(ScalarType.Float64, dim, dim);
        DenseMatrix projected = DenseMatrix.zeros(ScalarType.Float64, nbMeasurements, dim);
        // UPPER – as in getOperationEquationSystemSolver, SMILE's pure Java Cholesky then runs down columns
        DenseMatrix innovationCovariance = DenseMatrix.zeros(ScalarType.Float64, nbMeasurements, nbMeasurements).withUplo(UPLO.UPPER);
        DenseMatrix innovation = DenseMatrix.zeros(ScalarType.Float64, nbMeasurements, 1);
        DenseMatrix gainTransposed = DenseMatrix.zeros(ScalarType.Float64, nbMeasurements, dim);

        return (transition, processNoise, observation, measurementNoise, state, covariance, measurement, updatedState, updatedCovariance) -> {

            transition.mv(NO_TRANSPOSE, 1.0, state.column(0), 0.0, predictedState.column(0));
            DenseMatrix.mm(1.0, NO_TRANSPOSE, transition, NO_TRANSPOSE, covariance, 0.0, transformed);
            this.copy(processNoise, predicted);
            DenseMatrix.mm(1.0, NO_TRANSPOSE, transformed, TRANSPOSE, transition, 1.0, predicted);

            DenseMatrix.mm(1.0, NO_TRANSPOSE, observation, NO_TRANSPOSE, predicted, 0.0, projected);
            this.copy(measurementNoise, innovationCovariance);
            DenseMatrix.mm(1.0, NO_TRANSPOSE, projected, TRANSPOSE, observation, 1.0, innovationCovariance);
            this.copy(measurement, innovation);
            observation.mv(NO_TRANSPOSE, -1.0, predictedState.column(0), 1.0, innovation.column(0));

            // [K]^T = [S]^-1[H][P]- – the Cholesky decomposition overwrites the innovation covariance, and the solve the right hand side
            this.copy(projected, gainTransposed);
            innovationCovariance.cholesky().solve(gainTransposed);

            this.copy(predictedState, updatedState);
            gainTransposed.mv(TRANSPOSE, 1.0, innovation.column(0), 1.0, updatedState.column(0));
            this.copy(predicted, updatedCovariance);
            DenseMatrix.mm(-1.0, TRANSPOSE, gainTransposed, NO_TRANSPOSE, projected, 1.0, updatedCovariance);
        };
    }

    @Override
    public DecompositionOperation<DenseMatrix, DenseMatrix> getOperationLU(final int dim) {

        DenseMatrix[] ret = this.makeArray(3);

        DenseMatrix permutation = DenseMatrix.zeros(ScalarType.Float64, dim, dim);
        // Only the diagonal and below are written – above it stays zero
        DenseMatrix lower = DenseMatrix.zeros(ScalarType.Float64, dim, dim);
        int[] order = new int[dim];

        // There are no getters for the factors – [L] (unit diagonal) and [U] are packed in one matrix, and [P] is a sequence of row interchanges
        return matrix -> {

            LU lu = matrix.copy().lu();
            DenseMatrix upper = lu.lu();

            for (int j = 0; j < dim; j++) {
                lower.set(j, j, 1.0);
                for (int i = j + 1; i < dim; i++) {
                    lower.set(i, j, upper.get(i, j));
                    upper.set(i, j, 0.0);
                }
            }

            // Row i was interchanged with row ipiv[i] - 1 (1-based, as LAPACK), in turn
            int[] ipiv = lu.ipiv();
            for (int i = 0; i < dim; i++) {
                order[i] = i;
            }
            for (int i = 0; i < dim; i++) {
                int k = ipiv[i] - 1;
                int row = order[i];
                order[i] = order[k];
                order[k] = row;
            }
            permutation.fill(0.0);
            for (int i = 0; i < dim; i++) {
                permutation.set(i, order[i], 1.0);
            }

            ret[0] = permutation;
            ret[1] = lower;
            ret[2] = upper;
            return ret;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationMinimumNormSolver(final int numbEquations, final int numbVariables) {
        // The SVD solve only uses the singular values above the rank threshold – the minimum norm solution
        return (body, rhs) -> body.copy().svd(true).solve(rhs.column(0));
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationMultiplyMatrixVector(final boolean transpose) {
        // The vectors as Vector views of the single column matrices
        return (matrix, vector, product) -> matrix.mv(transpose ? TRANSPOSE : NO_TRANSPOSE, 1.0, vector.column(0), 0.0, product.column(0));
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationMultiplyToProduce() {
        return DenseMatrix::mm;
    }

    @Override
    public ProducingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationPseudoinverse(final int dim) {
        return matrix -> matrix.copy().svd(true).pinv();
    }

    @Override
    public DecompositionOperation<DenseMatrix, DenseMatrix> getOperationQR(final int dim) {

        DenseMatrix[] ret = this.makeArray(2);

        return matrix -> {
            QR qr = matrix.copy().qr();
            ret[0] = qr.Q();
            ret[1] = qr.R();
            return ret;
        };
    }

    @Override
    public PreparingOperation<DenseMatrix, DenseMatrix> getOperationRepeatedSolver(final int dim) {
        return body -> {

            LU lu = body.copy().lu();
            DenseMatrix solution = DenseMatrix.zeros(ScalarType.Float64, dim, 1);

            // The solve overwrites the right hand side
            return rhs -> {
                lu.solve(this.copy(rhs, solution));
                return solution;
            };
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationRidgeRegression(final int numbEquations, final int numbVariables,
            final double lambda) {

        DenseMatrix gram = DenseMatrix.zeros(ScalarType.Float64, numbVariables, numbVariables).withUplo(UPLO.UPPER);
        DenseMatrix solution = DenseMatrix.zeros(ScalarType.Float64, numbVariables, 1);

        return (design, observations) -> {

            DenseMatrix.mm(1.0, TRANSPOSE, design, NO_TRANSPOSE, design, 0.0, gram);
            for (int i = 0; i < numbVariables; i++) {
                gram.add(i, i, lambda);
            }
            design.mv(TRANSPOSE, 1.0, observations.column(0), 0.0, solution.column(0));

            // The Cholesky decomposition overwrites the Gram matrix, and the solve the right hand side
            gram.cholesky().solve(solution);
            return solution;
        };
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<DenseMatrix, DenseMatrix> getOperationScale() {
        return (a, s, b) -> this.copy(a, b).scale(s);
    }

    @Override
    public ProducingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationSingularValues(final int dim) {
        return matrix -> matrix.copy().svd(false).s();
    }

    @Override
    public DecompositionOperation<DenseMatrix, DenseMatrix> getOperationSVD(final int dim) {

        DenseMatrix[] ret = this.makeArray(3);

        return matrix -> {
            // Release the previous results first – at large sizes they don't fit in memory alongside the workspace
            ret[0] = ret[1] = ret[2] = null;
            SVD svd = matrix.copy().svd(true);
            ret[0] = svd.U();
            ret[1] = svd.diag();
            ret[2] = svd.Vt();
            return ret;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationSymmetricIndefiniteSolver(final int dim) {
        // No symmetric indefinite solver, so LU
        return this.getOperationEquationSystemSolver(dim, dim, 1, false);
    }

    @Override
    public DecompositionOperation<DenseMatrix, DenseMatrix> getOperationThinSVD(final int numbRows, final int numbCols) {

        DenseMatrix[] ret = this.makeArray(3);

        // The decomposition is compact – [U] is numbRows x numbCols
        return matrix -> {
            // Release the previous results first – at large sizes they don't fit in memory alongside the workspace
            ret[0] = ret[1] = ret[2] = null;
            SVD svd = matrix.copy().svd(true);
            ret[0] = svd.U();
            // Not diag() – it is numbRows x numbCols
            ret[1] = DenseMatrix.diagflat(svd.s().toArray(new double[numbCols]));
            ret[2] = svd.Vt();
            return ret;
        };
    }

    @Override
    public MutatingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationTranspose() {
        return (matA, result) -> this.copy(matA.transpose(), result);
    }

    /**
     * Matrices of the same shape have the same (padded) memory layout.
     */
    @Override
    protected DenseMatrix copy(final DenseMatrix source, final DenseMatrix destination) {
        destination.memory().copyFrom(source.memory());
        return destination;
    }

    @Override
    protected int countColumns(final DenseMatrix matrix) {
        return matrix.ncol();
    }

    @Override
    protected int countRows(final DenseMatrix matrix) {
        return matrix.nrow();
    }

    @Override
    protected double doubleValue(final DenseMatrix matrix, final int row, final int col) {
        return matrix.get(row, col);
    }

    @Override
    protected DenseMatrix[] makeArray(final int length) {
        return new DenseMatrix[length];
    }

    @Override
    protected DenseMatrix multiply(final DenseMatrix... factors) {

        DenseMatrix retVal = factors[0];

        for (int f = 1; f < factors.length; f++) {
            retVal = retVal.mm(factors[f]);
        }

        return retVal;
    }

    @Override
    protected double norm(final DenseMatrix matrix) {
        return matrix.norm();
    }

    @Override
    protected DenseMatrix subtract(final DenseMatrix left, final DenseMatrix right) {
        return left.copy().sub(right);
    }

}
