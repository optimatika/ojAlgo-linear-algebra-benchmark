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

import org.ejml.LinearSolverSafe;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM;
import org.ejml.interfaces.decomposition.EigenDecomposition_F64;
import org.ejml.interfaces.decomposition.SingularValueDecomposition_F64;
import org.ejml.interfaces.linsol.LinearSolverDense;
import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixScalarOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;

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
    public MutatingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationAdd() {
        return (a, b, c) -> CommonOps_DDRM.add(a, b, c);
    }

    @Override
    public ProducingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationEigenvectors(final int dim) {
        final EigenDecomposition_F64<DMatrixRMaj> evd = DecompositionFactory_DDRM.eig(dim, true, true);
        return (input) -> {
            evd.decompose(input);
            return evd.getEigenVector(0); // Only 1 vector - all other libraries return all vectors at once
        };
    }

    @Override
    public DecompositionOperation<DMatrixRMaj, DMatrixRMaj> getOperationEvD(final int dim) {

        final DMatrixRMaj[] ret = new DMatrixRMaj[2];

        final EigenDecomposition_F64<DMatrixRMaj> evd = DecompositionFactory_DDRM.eig(dim, true, true);

        throw new UnsupportedOperationException();

        //        return (matrix) -> {
        //            evd.decompose(matrix);
        //
        //            return ret;
        //        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationFillByMultiplying() {
        return (left, right, product) -> CommonOps_DDRM.mult(left, right, product);
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationEquationSystemSolver(final int numbEquations, final int numbVariables,
            final int numbSolutions) {

        final DMatrixRMaj result = new DMatrixRMaj(numbVariables, numbSolutions);

        if (numbEquations == numbVariables) {
            final LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.linear(numbVariables));
            return (body, rhs) -> {
                if (!solver.setA(body)) {
                    throw new BenchmarkRequirementsException();
                }
                solver.solve(rhs, result);
                return result;
            };
        } else if (numbEquations > numbVariables) {
            final LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.leastSquares(numbEquations, numbVariables));
            return (body, rhs) -> {
                if (!solver.setA(body)) {
                    throw new BenchmarkRequirementsException();
                }
                solver.solve(rhs, result);
                return result;
            };
        } else {
            return null;
        }
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationMultiplyToProduce() {
        return (left, right) -> {
            final DMatrixRMaj product = new DMatrixRMaj(left.getNumRows(), right.getNumCols());
            CommonOps_DDRM.mult(left, right, product);
            return product;
        };
    }

    @Override
    public ProducingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationPseudoinverse(final int dim) {
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
    public MutatingBinaryMatrixScalarOperation<DMatrixRMaj, DMatrixRMaj> getOperationScale() {
        return (a, s, b) -> CommonOps_DDRM.scale(s, a, b);
    }

    @Override
    public DecompositionOperation<DMatrixRMaj, DMatrixRMaj> getOperationSVD(final int dim) {

        final DMatrixRMaj[] ret = new DMatrixRMaj[3];

        final SingularValueDecomposition_F64<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(dim, dim, true, true, true);

        return (matrix) -> {
            svd.decompose(matrix);
            ret[0] = svd.getU(null, false);
            ret[1] = svd.getW(null);
            ret[2] = svd.getV(null, true);
            return ret;
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

    @Override
    protected DMatrixRMaj[] makeArray(final int length) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected DMatrixRMaj multiply(final DMatrixRMaj... factors) {

        DMatrixRMaj retVal = factors[0];

        for (int f = 1; f < factors.length; f++) {
            final DMatrixRMaj prod = new DMatrixRMaj(retVal.numRows, factors[f].numCols);
            CommonOps_DDRM.mult(retVal, factors[f], prod);
            retVal = prod;
        }

        return retVal;
    }

    @Override
    protected double norm(final DMatrixRMaj matrix) {
        return NormOps_DDRM.fastNormF(matrix);
    }

    @Override
    protected DMatrixRMaj subtract(final DMatrixRMaj left, final DMatrixRMaj right) {
        final DMatrixRMaj prod = new DMatrixRMaj(left.numRows, right.numCols);
        CommonOps_DDRM.subtract(left, right, prod);
        return prod;
    }

}
