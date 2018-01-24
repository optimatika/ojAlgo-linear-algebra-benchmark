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
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryOperation;
import org.ojalgo.benchmark.lab.DecomposeEigen;

/**
 * Apache Commons Math
 */
public class ACM extends MatrixBenchmarkLibrary<RealMatrix, RealMatrix> {

    @Override
    public DecomposeEigen.TaskDefinition<RealMatrix> getEigenDecomposer() {
        return new DecomposeEigen.TaskDefinition<RealMatrix>() {

            public RealMatrix decompose(final RealMatrix matrix) {

                final EigenDecomposition tmpEvD = new EigenDecomposition(matrix);

                return tmpEvD.getV();
            }
        };
    }

    @Override
    public MatrixBenchmarkLibrary<RealMatrix, RealMatrix>.GeneralSolver getGeneralSolver() {
        return new GeneralSolver() {

            @Override
            public RealMatrix apply(final RealMatrix body, final RealMatrix rhs) {

                final LUDecomposition tmpLU = new LUDecomposition(body);

                return tmpLU.getSolver().solve(rhs);
            }
        };
    }

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
    public MutatingBinaryOperation<RealMatrix, RealMatrix> getOperationFillByMultiplying() {
        return (ret, arg1, arg2) -> this.copy(arg1.multiply(arg2), ret);
    }

    @Override
    public ProducingBinaryOperation<RealMatrix, RealMatrix> getOperationMultiplyToProduce() {
        return (arg1, arg2) -> arg1.multiply(arg2);
    }

    @Override
    public MatrixBenchmarkLibrary<RealMatrix, RealMatrix>.SingularDecomposer getSingularDecomposer() {
        return new SingularDecomposer() {

            @Override
            public RealMatrix apply(final RealMatrix matrix) {

                final SingularValueDecomposition svd = new SingularValueDecomposition(matrix);

                final RealMatrix U = svd.getU();
                final RealMatrix S = svd.getS();
                final RealMatrix V = svd.getV();

                return svd.getS();
            }

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
    public MutatingBinaryOperation<RealMatrix, RealMatrix> getOperationSolveGeneral(int dim) {
        return (sol, body, rhs) -> {
            final LUDecomposition lu = new LUDecomposition(body);
            final RealMatrix tmp = lu.getSolver().solve(rhs);
            this.copy(tmp, sol);
        };
    }

    @Override
    protected RealMatrix copy(final RealMatrix source, final RealMatrix destination) {
        for (int i = 0; i < source.getRowDimension(); i++) {
            for (int j = 0; j < destination.getColumnDimension(); j++) {
                destination.setEntry(i, j, source.getEntry(i, j));
            }
        }
        return destination;
    }

}
