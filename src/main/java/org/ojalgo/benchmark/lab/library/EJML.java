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

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM;
import org.ejml.interfaces.decomposition.EigenDecomposition_F64;
import org.ejml.interfaces.linsol.LinearSolverDense;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryOperation;

/**
 * Efficient Java Matrix Library
 */
public class EJML extends MatrixBenchmarkLibrary<DMatrixRMaj, DMatrixRMaj> {

    @Override
    public HermitianSolver getHermitianSolver() {
        return new HermitianSolver() {

            @Override
            public DMatrixRMaj apply(final DMatrixRMaj body, final DMatrixRMaj rhs) {
                // TODO Auto-generated method stub
                return null;
            }

        };
    }

    @Override
    public LeastSquaresSolver getLeastSquaresSolver() {
        return new LeastSquaresSolver() {

            @Override
            public DMatrixRMaj apply(final DMatrixRMaj body, final DMatrixRMaj rhs) {
                // TODO Auto-generated method stub
                return null;
            }

        };
    }

    @Override
    public MatrixBenchmarkLibrary<DMatrixRMaj, DMatrixRMaj>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final DMatrixRMaj myMatrix = new DMatrixRMaj(numberOfRows, numberOfColumns);

            public DMatrixRMaj get() {
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
    public ProducingUnaryOperation<DMatrixRMaj, DMatrixRMaj> getOperationEigenvectors(final int dim) {
        final EigenDecomposition_F64<DMatrixRMaj> evd = DecompositionFactory_DDRM.eig(dim, true, true);
        return (input) -> {
            evd.decompose(input);
            return evd.getEigenVector(0); // Only 1 vector - all other libraries return all vectors at once
        };
    }

    @Override
    public MutatingBinaryOperation<DMatrixRMaj, DMatrixRMaj> getOperationFillByMultiplying() {
        return (arg1, arg2, ret) -> CommonOps_DDRM.mult(arg1, arg2, ret);
    }

    @Override
    public ProducingBinaryOperation<DMatrixRMaj, DMatrixRMaj> getOperationMultiplyToProduce() {
        return (arg1, arg2) -> {
            final DMatrixRMaj ret = new DMatrixRMaj(arg1.getNumRows(), arg2.getNumCols());
            CommonOps_DDRM.mult(arg1, arg2, ret);
            return ret;
        };
    }

    @Override
    public ProducingUnaryOperation<DMatrixRMaj, DMatrixRMaj> getOperationPseudoinverse(final int dim) {
        final LinearSolverDense<DMatrixRMaj> solver = LinearSolverFactory_DDRM.pseudoInverse(true);
        final DMatrixRMaj ret = new DMatrixRMaj(dim, dim);
        return (matrix) -> {
            if (!solver.setA(matrix)) {
                throw new RuntimeException();
            }
            solver.invert(ret);
            return ret;
        };
    }

    @Override
    public MutatingBinaryOperation<DMatrixRMaj, DMatrixRMaj> getOperationSolveGeneral(final int dim) {
        final LinearSolverDense<DMatrixRMaj> solver = LinearSolverFactory_DDRM.linear(dim);
        return (body, rhs, sol) -> {
            if (!solver.setA(body)) {
                throw new RuntimeException();
            }
            solver.solve(rhs, sol);
        };
    }

    @Override
    protected double[][] convertFrom(final DMatrixRMaj matrix) {
        final double[][] retVal = new double[matrix.getNumRows()][matrix.getNumCols()];
        for (int i = 0; i < retVal.length; i++) {
            final double[] tmpRow = retVal[i];
            for (int j = 0; j < tmpRow.length; j++) {
                tmpRow[j] = matrix.get(i, j);
            }
        }
        return retVal;
    }

    @Override
    protected DMatrixRMaj convertTo(final double[][] raw) {
        return new DMatrixRMaj(raw);
    }

    @Override
    protected DMatrixRMaj copy(final DMatrixRMaj source, final DMatrixRMaj destination) {
        System.arraycopy(source.data, 0, destination.data, 0, source.data.length);
        return destination;
    }

}