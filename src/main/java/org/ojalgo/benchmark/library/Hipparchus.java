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

import org.hipparchus.linear.BlockRealMatrix;
import org.hipparchus.linear.CholeskyDecomposition;
import org.hipparchus.linear.DecompositionSolver;
import org.hipparchus.linear.EigenDecompositionSymmetric;
import org.hipparchus.linear.LUDecomposition;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.QRDecomposition;
import org.hipparchus.linear.RealMatrix;
import org.hipparchus.linear.SingularValueDecomposition;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixScalarOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.PropertyOperation;

/**
 * Hipparchus (a fork of Apache Commons Math). BlockRealMatrix throughout – measured faster on 10-1000 than
 * {@link MatrixUtils#createRealMatrix(int, int)}, which would give Array2DRowRealMatrix up to 4096 elements.
 */
public class Hipparchus extends MatrixBenchmarkLibrary<RealMatrix, RealMatrix> {

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
    public PropertyOperation<RealMatrix, RealMatrix> getOperationDeterminant(final int dim) {
        return matA -> {
            LUDecomposition lu = new LUDecomposition(matA);
            return lu.getDeterminant();
        };
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
    public ProducingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationMultiplyToProduce() {
        return RealMatrix::multiply;
    }

    @Override
    public ProducingUnaryMatrixOperation<RealMatrix, RealMatrix> getOperationPseudoinverse(final int dim) {
        return matrix -> new SingularValueDecomposition(matrix).getSolver().getInverse();
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<RealMatrix, RealMatrix> getOperationScale() {
        return (a, s, b) -> this.copy(a.scalarMultiply(s), b);
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
