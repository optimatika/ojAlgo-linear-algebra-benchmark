/*
 * Copyright 1997-2017 Optimatika (www.optimatika.se)
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

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryOperation;

/**
 * Apache Commons Math
 */
public class ACM extends MatrixBenchmarkLibrary<RealMatrix, RealMatrix> {

    @Override
    public MatrixBenchmarkLibrary<RealMatrix, RealMatrix>.HermitianSolver getHermitianSolver() {
        return new HermitianSolver() {

            @Override
            public RealMatrix apply(final RealMatrix body, final RealMatrix rhs) {

                final CholeskyDecomposition tmpCholesky = new CholeskyDecomposition(body);

                return tmpCholesky.getSolver().solve(rhs);
            }

        };
    }

    @Override
    public MatrixBenchmarkLibrary<RealMatrix, RealMatrix>.LeastSquaresSolver getLeastSquaresSolver() {
        return new LeastSquaresSolver() {

            @Override
            public RealMatrix apply(final RealMatrix body, final RealMatrix rhs) {

                final QRDecomposition tmpQR = new QRDecomposition(body);

                return tmpQR.getSolver().solve(rhs);
            }

        };
    }

    @Override
    public MatrixBenchmarkLibrary<RealMatrix, RealMatrix>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final Array2DRowRealMatrix myMatrix = new Array2DRowRealMatrix(numberOfRows, numberOfColumns);

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
    public ProducingUnaryOperation<RealMatrix, RealMatrix> getOperationEigenvectors(final int dim) {
        return (input) -> {
            final EigenDecomposition evd = new EigenDecomposition(input);
            return evd.getV();
        };
    }

    @Override
    public DecompositionOperation<RealMatrix, RealMatrix> getOperationEvD(final int dim) {

        final RealMatrix[] ret = new RealMatrix[2];

        return (matrix) -> {
            final EigenDecomposition svd = new EigenDecomposition(matrix);
            ret[0] = svd.getD();
            ret[1] = svd.getV();
            return ret;
        };
    }

    @Override
    public MutatingBinaryOperation<RealMatrix, RealMatrix> getOperationFillByMultiplying() {
        return (left, right, product) -> this.copy(left.multiply(right), product);
    }

    @Override
    public ProducingBinaryOperation<RealMatrix, RealMatrix> getOperationMultiplyToProduce() {
        return (left, right) -> left.multiply(right);
    }

    @Override
    public ProducingUnaryOperation<RealMatrix, RealMatrix> getOperationPseudoinverse(final int dim) {
        return (matrix) -> new SingularValueDecomposition(matrix).getSolver().getInverse();
    }

    @Override
    public MutatingBinaryOperation<RealMatrix, RealMatrix> getOperationSolveGeneral(final int dim) {
        return (body, rhs, sol) -> {
            final LUDecomposition lu = new LUDecomposition(body);
            final RealMatrix tmp = lu.getSolver().solve(rhs);
            this.copy(tmp, sol);
        };
    }

    @Override
    public DecompositionOperation<RealMatrix, RealMatrix> getOperationSVD(final int dim) {

        final RealMatrix[] ret = new RealMatrix[3];

        return (matrix) -> {
            final SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
            ret[0] = svd.getU();
            ret[1] = svd.getS();
            ret[2] = svd.getV();
            return ret;
        };
    }

    @Override
    protected double[][] convertFrom(final RealMatrix matrix) {
        return matrix.getData();
    }

    @Override
    protected RealMatrix convertTo(final double[][] raw) {
        return new Array2DRowRealMatrix(raw);
    }

    @Override
    protected RealMatrix copy(final RealMatrix source, final RealMatrix destination) {
        for (int i = 0, rlim = source.getRowDimension(); i < rlim; i++) {
            for (int j = 0, clim = destination.getColumnDimension(); j < clim; j++) {
                destination.setEntry(i, j, source.getEntry(i, j));
            }
        }
        return destination;
    }

}
