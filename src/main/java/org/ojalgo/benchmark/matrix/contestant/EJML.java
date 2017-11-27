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
package org.ojalgo.benchmark.matrix.contestant;

import org.ejml.alg.dense.linsol.LinearSolverSafe;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.LinearSolverFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;
import org.ejml.interfaces.linsol.LinearSolver;
import org.ejml.ops.CommonOps;
import org.ejml.ops.EigenOps;
import org.ojalgo.benchmark.matrix.MatrixBenchmarkContestant;
import org.ojalgo.benchmark.matrix.suite.DecomposeEigen;
import org.ojalgo.benchmark.matrix.suite.FillByMultiplying;
import org.ojalgo.benchmark.matrix.suite.Square3Multiply;

/**
 * Efficient Java Matrix Library
 */
public class EJML extends MatrixBenchmarkContestant<DenseMatrix64F> {

    @Override
    public DecomposeEigen.TaskDefinition<DenseMatrix64F> getEigenDecomposer() {
        return new DecomposeEigen.TaskDefinition<DenseMatrix64F>() {

            @Override
            public DenseMatrix64F decompose(final DenseMatrix64F matrix) {

                final EigenDecomposition<DenseMatrix64F> eig = DecompositionFactory.eig(matrix.numCols, true, true);

                if (!DecompositionFactory.decomposeSafe(eig, matrix)) {
                    throw new RuntimeException("Decomposition failed");
                }

                eig.getEigenvalue(0);
                eig.getEigenVector(0);

                return EigenOps.createMatrixV(eig);

            }

        };
    }

    @Override
    public GeneralSolver getGeneralSolver() {
        return new GeneralSolver() {

            @Override
            public DenseMatrix64F apply(final DenseMatrix64F body, final DenseMatrix64F rhs) {

                final DenseMatrix64F result = new DenseMatrix64F(body.numCols, rhs.numCols);

                LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.linear(body.numRows);
                // make sure the input is not modified
                solver = new LinearSolverSafe<DenseMatrix64F>(solver);

                if (!solver.setA(body)) {
                    throw new IllegalArgumentException("Bad A");
                }

                solver.solve(rhs, result);

                return result;
            }

        };
    }

    @Override
    public HermitianSolver getHermitianSolver() {
        return new HermitianSolver() {

            @Override
            public DenseMatrix64F apply(final DenseMatrix64F body, final DenseMatrix64F rhs) {

                final DenseMatrix64F result = new DenseMatrix64F(body.numCols, rhs.numCols);

                LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.linear(body.numRows);

                solver = new LinearSolverSafe<DenseMatrix64F>(solver);

                if (!solver.setA(body)) {
                    throw new IllegalArgumentException("Bad A");
                }

                solver.solve(rhs, result);

                return result;
            }

        };
    }

    @Override
    public LeastSquaresSolver getLeastSquaresSolver() {
        return new LeastSquaresSolver() {

            @Override
            public DenseMatrix64F apply(final DenseMatrix64F body, final DenseMatrix64F rhs) {

                final DenseMatrix64F result = new DenseMatrix64F(body.numCols, rhs.numCols);

                LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.leastSquares(body.numRows, body.numCols);

                solver = new LinearSolverSafe<DenseMatrix64F>(solver);

                if (!solver.setA(body)) {
                    throw new IllegalArgumentException("Bad A");
                }

                solver.solve(rhs, result);

                return result;
            }

        };
    }

    @Override
    public LeftTransposedMultiplier getLeftTransposedMultiplier() {
        return new LeftTransposedMultiplier() {

            @Override
            public DenseMatrix64F apply(final DenseMatrix64F left, final DenseMatrix64F right) {

                final DenseMatrix64F result = new DenseMatrix64F(left.getNumRows(), right.getNumCols());

                CommonOps.multTransA(left, right, result);

                return result;
            }

        };
    }

    @Override
    public MatrixBenchmarkContestant<DenseMatrix64F>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final DenseMatrix64F myMatrix = new DenseMatrix64F(numberOfRows, numberOfColumns);

            public DenseMatrix64F get() {
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
    public Square3Multiply.TaskDefinition<DenseMatrix64F> getMatrixMultiplier() {
        return new Square3Multiply.TaskDefinition<DenseMatrix64F>() {

            @Override
            public DenseMatrix64F multiply(final DenseMatrix64F left, final DenseMatrix64F right) {

                final DenseMatrix64F result = new DenseMatrix64F(left.getNumRows(), right.getNumCols());

                CommonOps.mult(left, right, result);

                return result;
            }

        };
    }

    @Override
    public MatrixBenchmarkContestant<DenseMatrix64F>.RightTransposedMultiplier getRightTransposedMultiplier() {
        return new RightTransposedMultiplier() {

            @Override
            public DenseMatrix64F apply(final DenseMatrix64F left, final DenseMatrix64F right) {

                final DenseMatrix64F result = new DenseMatrix64F(left.getNumRows(), right.getNumCols());

                CommonOps.multTransB(left, right, result);

                return result;
            }

        };
    }

    @Override
    public MatrixBenchmarkContestant<DenseMatrix64F>.SingularDecomposer getSingularDecomposer() {
        return new SingularDecomposer() {

            @Override
            public DenseMatrix64F apply(final DenseMatrix64F matrix) {

                final SingularValueDecomposition<DenseMatrix64F> svd = DecompositionFactory.svd(matrix.numRows, matrix.numCols, true, true, false);

                if (!DecompositionFactory.decomposeSafe(svd, matrix)) {
                    throw new RuntimeException("Decomposition failed");
                }

                svd.getU(null, false);
                svd.getW(null);
                return svd.getV(null, false);
            }

        };
    }

    @Override
    protected double[][] convertFrom(final DenseMatrix64F matrix) {
        final double[][] retVal = new double[matrix.numRows][matrix.numCols];
        for (int i = 0; i < retVal.length; i++) {
            final double[] tmpRow = retVal[i];
            for (int j = 0; j < tmpRow.length; j++) {
                tmpRow[j] = matrix.get(i, j);
            }
        }
        return retVal;
    }

    @Override
    protected DenseMatrix64F convertTo(final double[][] raw) {
        return new DenseMatrix64F(raw);
    }

    @Override
    public FillByMultiplying.TaskDefinition<DenseMatrix64F> getMatrixMultiplier2() {
        return new FillByMultiplying.TaskDefinition<DenseMatrix64F>() {

            public DenseMatrix64F multiply(final DenseMatrix64F product, final DenseMatrix64F left, final DenseMatrix64F right) {
                CommonOps.multAdd(left, right, product);
                return product;
            }

        };
    }

}
