/*
 * Copyright 1997-2021 Optimatika (www.optimatika.se)
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
import org.ejml.dense.row.CovarianceOps_DDRM;
import org.ejml.dense.row.EigenOps_DDRM;
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
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.PropertyOperation;

/**
 * Efficient Java Matrix Library
 */
public class EJML extends MatrixBenchmarkLibrary<DMatrixRMaj, DMatrixRMaj> {

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
    public PropertyOperation<DMatrixRMaj, DMatrixRMaj> getOperationDeterminant(final int dim) {
        return (matA) -> CommonOps_DDRM.det(matA);
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationEquationSystemSolver(final int numbEquations, final int numbVariables,
            final int numbSolutions, final boolean spd) {

        final DMatrixRMaj result = new DMatrixRMaj(numbVariables, numbSolutions);

        if (numbEquations == numbVariables) {
            if (spd) {
                final LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.symmPosDef(numbVariables));
                return (body, rhs) -> {
                    if (!solver.setA(body)) {
                        throw new BenchmarkRequirementsException();
                    }
                    solver.solve(rhs, result);
                    return result;
                };
            } else {
                final LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.linear(numbVariables));
                return (body, rhs) -> {
                    if (!solver.setA(body)) {
                        throw new BenchmarkRequirementsException();
                    }
                    solver.solve(rhs, result);
                    return result;
                };
            }
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
    public DecompositionOperation<DMatrixRMaj, DMatrixRMaj> getOperationEvD(final int dim) {

        final DMatrixRMaj[] ret = this.makeArray(3);
        final EigenDecomposition_F64<DMatrixRMaj> evd = DecompositionFactory_DDRM.eig(dim, true, true);
        final DMatrixRMaj vt = new DMatrixRMaj(dim, dim);

        return input -> {

            if (!DecompositionFactory_DDRM.decomposeSafe(evd, input)) {
                throw new BenchmarkRequirementsException("Decomposition failed");
            }

            ret[0] = EigenOps_DDRM.createMatrixV(evd);
            ret[1] = EigenOps_DDRM.createMatrixD(evd);
            ret[2] = CommonOps_DDRM.transpose(ret[0], vt);

            return ret;
        };

    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationFillByMultiplying(final boolean transpL, final boolean transpR) {
        if (transpL) {
            if (transpR) {
                return (left, right, product) -> CommonOps_DDRM.multTransAB(left, right, product);
            } else {
                return (left, right, product) -> CommonOps_DDRM.multTransA(left, right, product);
            }
        } else {
            if (transpR) {
                return (left, right, product) -> CommonOps_DDRM.multTransB(left, right, product);
            } else {
                return (left, right, product) -> CommonOps_DDRM.mult(left, right, product);
            }
        }
    }

    @Override
    public MutatingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationInvert(final int dim, final boolean spd) {
        if (spd) {
            return (matA, result) -> CovarianceOps_DDRM.invert(matA, result);
        } else {
            return (matA, result) -> CommonOps_DDRM.invert(matA, result);
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

        final DMatrixRMaj[] ret = this.makeArray(3);
        final SingularValueDecomposition_F64<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(dim, dim, true, true, true);

        final DMatrixRMaj mtrxU = new DMatrixRMaj(dim, dim);
        final DMatrixRMaj mtrxW = new DMatrixRMaj(dim, dim);
        final DMatrixRMaj mtrxV = new DMatrixRMaj(dim, dim);

        return (matrix) -> {
            svd.decompose(matrix);
            ret[0] = svd.getU(mtrxU, false);
            ret[1] = svd.getW(mtrxW);
            ret[2] = svd.getV(mtrxV, true);
            return ret;
        };
    }

    @Override
    public MutatingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationTranspose() {
        return (matA, result) -> CommonOps_DDRM.transpose(matA, result);
    }

    @Override
    protected DMatrixRMaj copy(final DMatrixRMaj source, final DMatrixRMaj destination) {
        System.arraycopy(source.data, 0, destination.data, 0, source.data.length);
        return destination;
    }

    @Override
    protected DMatrixRMaj[] makeArray(final int length) {
        return new DMatrixRMaj[length];
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
