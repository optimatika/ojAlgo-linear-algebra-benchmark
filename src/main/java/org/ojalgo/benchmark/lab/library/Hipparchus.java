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
package org.ojalgo.benchmark.lab.library;

import org.hipparchus.linear.Array2DRowRealMatrix;
import org.hipparchus.linear.CholeskyDecomposition;
import org.hipparchus.linear.EigenDecompositionSymmetric;
import org.hipparchus.linear.LUDecomposition;
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
 * Hipparchus (a fork of Apache Commons Math)
 */
public class Hipparchus extends MatrixBenchmarkLibrary<RealMatrix, Array2DRowRealMatrix> {

    @Override
    public MatrixBenchmarkLibrary<RealMatrix, Array2DRowRealMatrix>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final Array2DRowRealMatrix myMatrix = new Array2DRowRealMatrix(numberOfRows, numberOfColumns);

            public Array2DRowRealMatrix get() {
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
    public MutatingBinaryMatrixMatrixOperation<RealMatrix, Array2DRowRealMatrix> getOperationAdd() {
        return (a, b, c) -> this.copy(a.add(b), c);
    }

    @Override
    public PropertyOperation<RealMatrix, Array2DRowRealMatrix> getOperationDeterminant(final int dim) {
        return matA -> {
            LUDecomposition lu = new LUDecomposition(matA);
            return lu.getDeterminant();
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<RealMatrix, Array2DRowRealMatrix> getOperationEquationSystemSolver(final int numbEquations,
            final int numbVariables, final int numbSolutions, final boolean spd) {
        if (numbEquations == numbVariables) {
            if (spd) {
                return (body, rhs) -> {
                    CholeskyDecomposition cholesky = new CholeskyDecomposition(body);
                    return cholesky.getSolver().solve(rhs);
                };
            }
            return (body, rhs) -> {
                LUDecomposition lu = new LUDecomposition(body);
                return lu.getSolver().solve(rhs);
            };
        }
        if (numbEquations <= numbVariables) {
            return null;
        }
        return (body, rhs) -> {
            QRDecomposition qr = new QRDecomposition(body);
            return qr.getSolver().solve(rhs);
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
    public MutatingBinaryMatrixMatrixOperation<RealMatrix, Array2DRowRealMatrix> getOperationFillByMultiplying(final boolean transpL, final boolean transpR) {
        return (left, right, product) -> this.copy((transpL ? left.transpose() : left).multiply(transpR ? right.transpose() : right), product);
    }

    @Override
    public MutatingUnaryMatrixOperation<RealMatrix, Array2DRowRealMatrix> getOperationInvert(final int dim, final boolean spd) {
        if (spd) {
            return (matA, result) -> {
                CholeskyDecomposition chol = new CholeskyDecomposition(matA);
                this.copy(chol.getSolver().getInverse(), result);
            };
        }
        return (matA, result) -> {
            LUDecomposition lu = new LUDecomposition(matA);
            this.copy(lu.getSolver().getInverse(), result);
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<RealMatrix, RealMatrix> getOperationMultiplyToProduce() {
        return RealMatrix::multiply;
    }

    @Override
    public ProducingUnaryMatrixOperation<RealMatrix, Array2DRowRealMatrix> getOperationPseudoinverse(final int dim) {
        return matrix -> new SingularValueDecomposition(matrix).getSolver().getInverse();
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<RealMatrix, Array2DRowRealMatrix> getOperationScale() {
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
    public MutatingUnaryMatrixOperation<RealMatrix, Array2DRowRealMatrix> getOperationTranspose() {
        return (matA, result) -> this.copy(matA.transpose(), result);
    }

    @Override
    protected Array2DRowRealMatrix copy(final RealMatrix source, final Array2DRowRealMatrix destination) {
        for (int i = 0, rlim = source.getRowDimension(); i < rlim; i++) {
            for (int j = 0, clim = destination.getColumnDimension(); j < clim; j++) {
                destination.setEntry(i, j, source.getEntry(i, j));
            }
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
