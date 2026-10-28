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

import org.hipparchus.linear.*;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.*;

/**
 * Hipparchus (a fork of Apache Commons Math). BlockRealMatrix throughout – measured faster on 10-1000 than
 * {@link MatrixUtils#createRealMatrix(int, int)}, which would give Array2DRowRealMatrix up to 4096 elements.
 */
public final class Hipparchus extends MatrixBenchmarkLibrary<RealMatrix, RealMatrix> {

    /**
     * Products that are symmetric in exact arithmetic, like [H][P][H]<sup>T</sup>, are not exactly symmetric
     * in floating point – the default threshold (1e-15) rejects them.
     */
    private static final double RELATIVE_SYMMETRY_THRESHOLD = 1.0E-10;

    /**
     * The benchmarks solve for a single right hand side – solved as a vector, Hipparchus' matrix solve has
     * overhead for one column.
     */
    private static RealMatrix solve(final DecompositionSolver solver, final RealMatrix rhs) {
        return MatrixUtils.createColumnRealMatrix(solver.solve(rhs.getColumnVector(0)).toArray());
    }

    @Override
    public MatrixBenchmarkLibrary<RealMatrix, RealMatrix>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final RealMatrix myMatrix = new BlockRealMatrix(numberOfRows, numberOfColumns);

            @Override
            public RealMatrix get() {
                return myMatrix;
            }

            @Override
            public MatrixBuilder set(final int row, final int col, final double value) {
                myMatrix.setEntry(row, col, value);
                return this;
            }

        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationAdd() {
        return (a, b, c) -> this.copy(a.add(b), c);
    }

    @Override
    public DecompositionOperation<RealMatrix, RealMatrix> getOperationCholesky(final int dim) {

        RealMatrix[] ret = this.makeArray(1);

        // [L]^T – what the decomposition calculates, getL() is its transpose
        return matrix -> {
            CholeskyDecomposition cholesky = new CholeskyDecomposition(matrix);
            ret[0] = cholesky.getLT();
            return ret;
        };
    }

    @Override
    public PropertyOperation<RealMatrix, RealMatrix> getOperationDeterminant(final int dim) {
        return matA -> {
            LUDecomposition lu = new LUDecomposition(matA);
            return lu.getDeterminant();
        };
    }

    @Override
    public ProducingUnaryMatrixOperation<RealMatrix, RealMatrix> getOperationEigenvalues(final int dim) {
        return matrix -> MatrixUtils
                .createColumnRealMatrix(new EigenDecompositionSymmetric(matrix, EigenDecompositionSymmetric.DEFAULT_EPSILON, false).getEigenvalues());
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationEquationSystemSolver(final int numbEquations, final int numbVariables,
            final int numbSolutions, final boolean spd) {
        if (numbSolutions != 1) {
            return null; // Only implemented for a single right hand side
        }
        if (numbEquations == numbVariables) {
            if (spd) {
                return (body, rhs) -> {
                    CholeskyDecomposition cholesky = new CholeskyDecomposition(body);
                    return Hipparchus.solve(cholesky.getSolver(), rhs);
                };
            }
            return (body, rhs) -> {
                LUDecomposition lu = new LUDecomposition(body);
                return Hipparchus.solve(lu.getSolver(), rhs);
            };
        }
        if (numbEquations <= numbVariables) {
            return null;
        }
        return (body, rhs) -> {
            QRDecomposition qr = new QRDecomposition(body);
            return Hipparchus.solve(qr.getSolver(), rhs);
        };
    }

    @Override
    public DecompositionOperation<RealMatrix, RealMatrix> getOperationEvD(final int dim) {

        RealMatrix[] ret = this.makeArray(2);

        return matrix -> {
            EigenDecompositionSymmetric evd = new EigenDecompositionSymmetric(matrix);
            ret[0] = evd.getV();
            ret[1] = evd.getD();
            return ret;
        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationFillByMultiplying(final boolean transpL, final boolean transpR) {
        // The transposed variants have their own implementations – no explicit transpose
        if (transpL) {
            if (transpR) {
                return (left, right, product) -> this.copy(left.transpose().multiplyTransposed(right), product);
            }
            return (left, right, product) -> this.copy(left.transposeMultiply(right), product);
        }
        if (transpR) {
            return (left, right, product) -> this.copy(left.multiplyTransposed(right), product);
        }
        return (left, right, product) -> this.copy(left.multiply(right), product);
    }

    @Override
    public MutatingUnaryMatrixOperation<RealMatrix, RealMatrix> getOperationGram() {
        return (matrix, product) -> this.copy(matrix.transposeMultiply(matrix), product);
    }

    @Override
    public MutatingUnaryMatrixOperation<RealMatrix, RealMatrix> getOperationInvert(final int dim, final boolean spd) {
        // What getInverse() does, without creating a new identity every time
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(dim);
        if (spd) {
            return (matA, result) -> {
                CholeskyDecomposition chol = new CholeskyDecomposition(matA);
                this.copy(chol.getSolver().solve(identity), result);
            };
        }
        return (matA, result) -> {
            LUDecomposition lu = new LUDecomposition(matA);
            this.copy(lu.getSolver().solve(identity), result);
        };
    }

    @Override
    public KalmanFilterOperation<RealMatrix, RealMatrix> getOperationKalmanFilter(final int dim, final int nbMeasurements) {
        return (transition, processNoise, observation, measurementNoise, state, covariance, measurement, updatedState, updatedCovariance) -> {

            double[] predictedState = transition.operate(state.getColumn(0));
            RealMatrix predicted = transition.multiply(covariance).multiplyTransposed(transition).add(processNoise);

            RealMatrix projected = observation.multiply(predicted);
            RealMatrix innovationCovariance = projected.multiplyTransposed(observation).add(measurementNoise);
            double[] innovation = observation.operate(predictedState);
            for (int i = 0; i < nbMeasurements; i++) {
                innovation[i] = measurement.getEntry(i, 0) - innovation[i];
            }

            // [K]^T = [S]^-1[H][P]-
            CholeskyDecomposition cholesky = new CholeskyDecomposition(innovationCovariance, RELATIVE_SYMMETRY_THRESHOLD,
                    CholeskyDecomposition.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
            RealMatrix gainTransposed = cholesky.getSolver().solve(projected);

            double[] correction = gainTransposed.preMultiply(innovation);
            for (int i = 0; i < dim; i++) {
                correction[i] += predictedState[i];
            }
            updatedState.setColumn(0, correction);
            this.copy(predicted.subtract(gainTransposed.transposeMultiply(projected)), updatedCovariance);
        };
    }

    @Override
    public DecompositionOperation<RealMatrix, RealMatrix> getOperationLU(final int dim) {

        RealMatrix[] ret = this.makeArray(3);

        return matrix -> {
            LUDecomposition lu = new LUDecomposition(matrix);
            ret[0] = lu.getP();
            ret[1] = lu.getL();
            ret[2] = lu.getU();
            return ret;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationMinimumNormSolver(final int numbEquations, final int numbVariables) {
        // The SVD solver solves with the pseudoinverse – the minimum norm solution
        return (body, rhs) -> Hipparchus.solve(new SingularValueDecomposition(body).getSolver(), rhs);
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationMultiplyMatrixVector(final boolean transpose) {
        // Hipparchus' matrix-vector multiplications work with arrays
        if (transpose) {
            return (matrix, vector, product) -> product.setColumn(0, matrix.preMultiply(vector.getColumn(0)));
        }
        return (matrix, vector, product) -> product.setColumn(0, matrix.operate(vector.getColumn(0)));
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationMultiplyToProduce() {
        return RealMatrix::multiply;
    }

    @Override
    public ProducingUnaryMatrixOperation<RealMatrix, RealMatrix> getOperationPseudoinverse(final int dim) {
        return matrix -> new SingularValueDecomposition(matrix).getSolver().getInverse();
    }

    @Override
    public DecompositionOperation<RealMatrix, RealMatrix> getOperationQR(final int dim) {

        RealMatrix[] ret = this.makeArray(2);

        return matrix -> {
            QRDecomposition qr = new QRDecomposition(matrix);
            ret[0] = qr.getQ();
            ret[1] = qr.getR();
            return ret;
        };
    }

    @Override
    public PreparingOperation<RealMatrix, RealMatrix> getOperationRepeatedSolver(final int dim) {
        return body -> {
            DecompositionSolver solver = new LUDecomposition(body).getSolver();
            return rhs -> Hipparchus.solve(solver, rhs);
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationRidgeRegression(final int numbEquations, final int numbVariables,
            final double lambda) {
        return (design, observations) -> {

            RealMatrix gram = design.transposeMultiply(design);
            for (int i = 0; i < numbVariables; i++) {
                gram.addToEntry(i, i, lambda);
            }
            double[] moment = design.preMultiply(observations.getColumn(0));

            CholeskyDecomposition cholesky = new CholeskyDecomposition(gram, RELATIVE_SYMMETRY_THRESHOLD,
                    CholeskyDecomposition.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
            return MatrixUtils.createColumnRealMatrix(cholesky.getSolver().solve(new ArrayRealVector(moment, false)).toArray());
        };
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<RealMatrix, RealMatrix> getOperationScale() {
        return (a, s, b) -> this.copy(a.scalarMultiply(s), b);
    }

    @Override
    public ProducingUnaryMatrixOperation<RealMatrix, RealMatrix> getOperationSingularValues(final int dim) {
        // There is no values only option – the singular vectors are always calculated
        return matrix -> MatrixUtils.createColumnRealMatrix(new SingularValueDecomposition(matrix).getSingularValues());
    }

    @Override
    public DecompositionOperation<RealMatrix, RealMatrix> getOperationSVD(final int dim) {

        RealMatrix[] ret = this.makeArray(3);

        return matrix -> {
            SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
            ret[0] = svd.getU();
            ret[1] = svd.getS();
            ret[2] = svd.getVT();
            return ret;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationSymmetricIndefiniteSolver(final int dim) {
        // No symmetric indefinite solver, so LU
        return this.getOperationEquationSystemSolver(dim, dim, 1, false);
    }

    @Override
    public DecompositionOperation<RealMatrix, RealMatrix> getOperationThinSVD(final int numbRows, final int numbCols) {

        RealMatrix[] ret = this.makeArray(3);

        // The decomposition is compact – [U] is numbRows x numbCols
        return matrix -> {
            SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
            ret[0] = svd.getU();
            ret[1] = svd.getS();
            ret[2] = svd.getVT();
            return ret;
        };
    }

    @Override
    public MutatingUnaryMatrixOperation<RealMatrix, RealMatrix> getOperationTranspose() {
        return (matA, result) -> this.copy(matA.transpose(), result);
    }

    @Override
    protected RealMatrix copy(final RealMatrix source, final RealMatrix destination) {
        // Row by row – element by element is (much) slower, in particular for BlockRealMatrix
        for (int i = 0, rlim = source.getRowDimension(); i < rlim; i++) {
            destination.setRow(i, source.getRow(i));
        }
        return destination;
    }

    @Override
    protected int countColumns(final RealMatrix matrix) {
        return matrix.getColumnDimension();
    }

    @Override
    protected int countRows(final RealMatrix matrix) {
        return matrix.getRowDimension();
    }

    @Override
    protected double doubleValue(final RealMatrix matrix, final int row, final int col) {
        return matrix.getEntry(row, col);
    }

    @Override
    protected RealMatrix[] makeArray(final int length) {
        return new RealMatrix[length];
    }

    @Override
    protected RealMatrix multiply(final RealMatrix... factors) {

        RealMatrix retVal = factors[0];

        for (int f = 1; f < factors.length; f++) {
            retVal = retVal.multiply(factors[f]);
        }

        return retVal;
    }

    @Override
    protected double norm(final RealMatrix matrix) {
        return matrix.getFrobeniusNorm();
    }

    @Override
    protected RealMatrix subtract(final RealMatrix left, final RealMatrix right) {
        return left.subtract(right);
    }

}
