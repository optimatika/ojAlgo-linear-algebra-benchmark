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

import org.ejml.LinearSolverSafe;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.CommonOps_MT_DDRM;
import org.ejml.dense.row.EigenOps_DDRM;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM;
import org.ejml.interfaces.decomposition.EigenDecomposition_F64;
import org.ejml.interfaces.decomposition.LUDecomposition_F64;
import org.ejml.interfaces.decomposition.SingularValueDecomposition_F64;
import org.ejml.interfaces.linsol.LinearSolverDense;
import org.ejml.simple.ops.SimpleOperations_DDRM;
import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.KalmanFilterOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixScalarOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.PreparingOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.PropertyOperation;

/**
 * Efficient Java Matrix Library
 */
public class EJML extends MatrixBenchmarkLibrary<DMatrixRMaj, DMatrixRMaj> {

    /**
     * What SimpleMatrix uses – EJML's own switching to the concurrent implementations (CommonOps_MT_DDRM) for
     * large matrices. Only covers mult, multTransA and transpose.
     */
    private static final SimpleOperations_DDRM OPERATIONS = new SimpleOperations_DDRM();

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
        return CommonOps_DDRM::add;
    }

    @Override
    public PropertyOperation<DMatrixRMaj, DMatrixRMaj> getOperationDeterminant(final int dim) {
        // Reused, rather than CommonOps_DDRM.det(...) creating a new decomposition every time
        LUDecomposition_F64<DMatrixRMaj> lu = DecompositionFactory_DDRM.lu(dim, dim);
        return a -> lu.decompose(a) ? lu.computeDeterminant().real : 0.0;
    }

    @Override
    public ProducingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationEigenvalues(final int dim) {

        EigenDecomposition_F64<DMatrixRMaj> evd = DecompositionFactory_DDRM.eig(dim, false, true);
        // The decomposition works in place – copy into a preallocated matrix
        DMatrixRMaj work = new DMatrixRMaj(dim, dim);
        DMatrixRMaj values = new DMatrixRMaj(dim, 1);

        return input -> {

            work.setTo(input);
            if (!evd.decompose(work)) {
                throw new BenchmarkRequirementsException("Decomposition failed");
            }

            for (int i = 0; i < dim; i++) {
                values.data[i] = evd.getEigenvalue(i).real;
            }

            return values;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationEquationSystemSolver(final int numbEquations, final int numbVariables,
            final int numbSolutions, final boolean spd) {

        DMatrixRMaj result = new DMatrixRMaj(numbVariables, numbSolutions);

        if (numbEquations == numbVariables) {
            if (spd) {
                LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.symmPosDef(numbVariables));
                return (body, rhs) -> {
                    if (!solver.setA(body)) {
                        throw new BenchmarkRequirementsException();
                    }
                    solver.solve(rhs, result);
                    return result;
                };
            }
            LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.linear(numbVariables));
            return (body, rhs) -> {
                if (!solver.setA(body)) {
                    throw new BenchmarkRequirementsException();
                }
                solver.solve(rhs, result);
                return result;
            };
        }
        if (numbEquations <= numbVariables) {
            return null;
        }
        LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.leastSquares(numbEquations, numbVariables));
        return (body, rhs) -> {
            if (!solver.setA(body)) {
                throw new BenchmarkRequirementsException();
            }
            solver.solve(rhs, result);
            return result;
        };
    }

    @Override
    public DecompositionOperation<DMatrixRMaj, DMatrixRMaj> getOperationEvD(final int dim) {

        DMatrixRMaj[] ret = this.makeArray(2);
        EigenDecomposition_F64<DMatrixRMaj> evd = DecompositionFactory_DDRM.eig(dim, true, true);
        // The decomposition works in place – copy into a preallocated matrix, rather than decomposeSafe(...) allocating a copy every time
        DMatrixRMaj work = new DMatrixRMaj(dim, dim);

        return input -> {

            work.setTo(input);
            if (!evd.decompose(work)) {
                throw new BenchmarkRequirementsException("Decomposition failed");
            }

            ret[0] = EigenOps_DDRM.createMatrixV(evd);
            ret[1] = EigenOps_DDRM.createMatrixD(evd);

            return ret;
        };

    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationFillByMultiplying(final boolean transpL, final boolean transpR) {
        // multTransB and multTransAB have no variant that switches to the concurrent implementation, so the choice is
        // static: multTransB always concurrent, multTransAB always single-threaded. The concurrent multTransB pays for
        // the threading at every size – about 12 µs per call at 10x10, where mult takes 0.3 µs
        if (transpL) {
            if (transpR) {
                return CommonOps_DDRM::multTransAB;
            }
            return OPERATIONS::multTransA;
        }
        if (transpR) {
            return CommonOps_MT_DDRM::multTransB;
        }
        return OPERATIONS::mult;
    }

    @Override
    public MutatingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationGram() {
        // Measured faster on 10-1000 than CommonOps_DDRM.multInner, and than multTransA (no switching variant for multInner)
        return CommonOps_MT_DDRM::multInner;
    }

    @Override
    public MutatingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationInvert(final int dim, final boolean spd) {
        // Reused, rather than CommonOps_DDRM.invert(...) / CovarianceOps_DDRM.invert(...) creating a new solver every time
        LinearSolverDense<DMatrixRMaj> solver = spd ? new LinearSolverSafe<>(LinearSolverFactory_DDRM.symmPosDef(dim)) : LinearSolverFactory_DDRM.linear(dim);
        return (a, inv) -> {
            if (!solver.setA(a)) {
                throw new BenchmarkRequirementsException();
            }
            solver.invert(inv);
        };
    }

    @Override
    public KalmanFilterOperation<DMatrixRMaj, DMatrixRMaj> getOperationKalmanFilter(final int dim, final int nbMeasurements) {

        DMatrixRMaj predictedState = new DMatrixRMaj(dim, 1);
        DMatrixRMaj transformed = new DMatrixRMaj(dim, dim);
        DMatrixRMaj predicted = new DMatrixRMaj(dim, dim);
        DMatrixRMaj projected = new DMatrixRMaj(nbMeasurements, dim);
        DMatrixRMaj innovationCovariance = new DMatrixRMaj(nbMeasurements, nbMeasurements);
        DMatrixRMaj innovation = new DMatrixRMaj(nbMeasurements, 1);
        DMatrixRMaj gainTransposed = new DMatrixRMaj(nbMeasurements, dim);
        DMatrixRMaj correction = new DMatrixRMaj(dim, dim);

        // The innovation covariance is workspace – may be modified, no LinearSolverSafe
        LinearSolverDense<DMatrixRMaj> solver = LinearSolverFactory_DDRM.symmPosDef(nbMeasurements);

        // The multiplications as in getOperationFillByMultiplying, and the matrix-vector ones as in getOperationMultiplyMatrixVector
        return (transition, processNoise, observation, measurementNoise, state, covariance, measurement, updatedState, updatedCovariance) -> {

            CommonOps_DDRM.mult(transition, state, predictedState);
            OPERATIONS.mult(transition, covariance, transformed);
            CommonOps_MT_DDRM.multTransB(transformed, transition, predicted);
            CommonOps_DDRM.addEquals(predicted, processNoise);

            OPERATIONS.mult(observation, predicted, projected);
            CommonOps_MT_DDRM.multTransB(projected, observation, innovationCovariance);
            CommonOps_DDRM.addEquals(innovationCovariance, measurementNoise);
            CommonOps_DDRM.mult(observation, predictedState, innovation);
            CommonOps_DDRM.subtract(measurement, innovation, innovation);

            // [K]^T = [S]^-1[H][P]-
            if (!solver.setA(innovationCovariance)) {
                throw new BenchmarkRequirementsException();
            }
            solver.solve(projected, gainTransposed);

            CommonOps_DDRM.multTransA(gainTransposed, innovation, updatedState);
            CommonOps_DDRM.addEquals(updatedState, predictedState);
            OPERATIONS.multTransA(gainTransposed, projected, correction);
            CommonOps_DDRM.subtract(predicted, correction, updatedCovariance);
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationMinimumNormSolver(final int numbEquations, final int numbVariables) {

        // SVD – QR with column pivoting, leastSquaresQrPivot(true, false), misjudges the rank: its threshold, machine epsilon times the largest
        // element, is not scaled by the size, and rounding errors are taken for (huge) contributions to the solution
        LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.pseudoInverse(true));
        DMatrixRMaj result = new DMatrixRMaj(numbVariables, 1);

        return (body, rhs) -> {
            if (!solver.setA(body)) {
                throw new BenchmarkRequirementsException();
            }
            solver.solve(rhs, result);
            return result;
        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationMultiplyMatrixVector(final boolean transpose) {
        // CommonOps_DDRM has dedicated matrix-vector implementations, used when the right factor is a vector
        if (transpose) {
            return CommonOps_DDRM::multTransA;
        }
        return CommonOps_DDRM::mult;
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationMultiplyToProduce() {
        return (left, right) -> {
            DMatrixRMaj product = new DMatrixRMaj(left.getNumRows(), right.getNumCols());
            OPERATIONS.mult(left, right, product);
            return product;
        };
    }

    @Override
    public ProducingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationPseudoinverse(final int dim) {
        LinearSolverDense<DMatrixRMaj> solver = LinearSolverFactory_DDRM.pseudoInverse(true);
        DMatrixRMaj ret = new DMatrixRMaj(dim, dim);
        return matrix -> {
            if (!solver.setA(matrix)) {
                throw new RuntimeException();
            }
            solver.invert(ret);
            return ret;
        };
    }

    @Override
    public PreparingOperation<DMatrixRMaj, DMatrixRMaj> getOperationRepeatedSolver(final int dim) {
        return body -> {

            LinearSolverDense<DMatrixRMaj> solver = new LinearSolverSafe<>(LinearSolverFactory_DDRM.linear(dim));
            if (!solver.setA(body)) {
                throw new BenchmarkRequirementsException();
            }

            DMatrixRMaj result = new DMatrixRMaj(dim, 1);

            return rhs -> {
                solver.solve(rhs, result);
                return result;
            };
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationRidgeRegression(final int numbEquations, final int numbVariables,
            final double lambda) {

        DMatrixRMaj gram = new DMatrixRMaj(numbVariables, numbVariables);
        DMatrixRMaj moment = new DMatrixRMaj(numbVariables, 1);
        DMatrixRMaj result = new DMatrixRMaj(numbVariables, 1);

        // The Gram matrix is workspace – may be modified, no LinearSolverSafe
        LinearSolverDense<DMatrixRMaj> solver = LinearSolverFactory_DDRM.symmPosDef(numbVariables);

        MutatingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> multiplication = this.getOperationGram();

        return (design, observations) -> {

            multiplication.operate(design, gram);
            for (int i = 0; i < numbVariables; i++) {
                gram.add(i, i, lambda);
            }
            CommonOps_DDRM.multTransA(design, observations, moment);

            if (!solver.setA(gram)) {
                throw new BenchmarkRequirementsException();
            }
            solver.solve(moment, result);
            return result;
        };
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<DMatrixRMaj, DMatrixRMaj> getOperationScale() {
        return (a, s, b) -> CommonOps_DDRM.scale(s, a, b);
    }

    @Override
    public ProducingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationSingularValues(final int dim) {

        SingularValueDecomposition_F64<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(dim, dim, false, false, true);
        DMatrixRMaj values = new DMatrixRMaj(dim, 1);

        return matrix -> {
            if (!svd.decompose(matrix)) {
                throw new BenchmarkRequirementsException("Decomposition failed");
            }
            System.arraycopy(svd.getSingularValues(), 0, values.data, 0, dim);
            return values;
        };
    }

    @Override
    public DecompositionOperation<DMatrixRMaj, DMatrixRMaj> getOperationSVD(final int dim) {

        DMatrixRMaj[] ret = this.makeArray(3);
        SingularValueDecomposition_F64<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(dim, dim, true, true, true);

        DMatrixRMaj mtrxU = new DMatrixRMaj(dim, dim);
        DMatrixRMaj mtrxW = new DMatrixRMaj(dim, dim);
        DMatrixRMaj mtrxV = new DMatrixRMaj(dim, dim);

        return matrix -> {
            svd.decompose(matrix);
            ret[0] = svd.getU(mtrxU, false);
            ret[1] = svd.getW(mtrxW);
            ret[2] = svd.getV(mtrxV, true);
            return ret;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationSymmetricIndefiniteSolver(final int dim) {
        // EJML's LDL requires positive definite – no symmetric indefinite solver, so LU
        return this.getOperationEquationSystemSolver(dim, dim, 1, false);
    }

    @Override
    public DecompositionOperation<DMatrixRMaj, DMatrixRMaj> getOperationThinSVD(final int numbRows, final int numbCols) {

        DMatrixRMaj[] ret = this.makeArray(3);
        SingularValueDecomposition_F64<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(numbRows, numbCols, true, true, true);

        DMatrixRMaj mtrxU = new DMatrixRMaj(numbRows, numbCols);
        DMatrixRMaj mtrxW = new DMatrixRMaj(numbCols, numbCols);
        DMatrixRMaj mtrxV = new DMatrixRMaj(numbCols, numbCols);

        return matrix -> {
            if (!svd.decompose(matrix)) {
                throw new BenchmarkRequirementsException("Decomposition failed");
            }
            ret[0] = svd.getU(mtrxU, false);
            ret[1] = svd.getW(mtrxW);
            ret[2] = svd.getV(mtrxV, true);
            return ret;
        };
    }

    @Override
    public MutatingUnaryMatrixOperation<DMatrixRMaj, DMatrixRMaj> getOperationTranspose() {
        return OPERATIONS::transpose;
    }

    @Override
    protected DMatrixRMaj copy(final DMatrixRMaj source, final DMatrixRMaj destination) {
        System.arraycopy(source.data, 0, destination.data, 0, source.data.length);
        return destination;
    }

    @Override
    protected int countColumns(final DMatrixRMaj matrix) {
        return matrix.getNumCols();
    }

    @Override
    protected int countRows(final DMatrixRMaj matrix) {
        return matrix.getNumRows();
    }

    @Override
    protected double doubleValue(final DMatrixRMaj matrix, final int row, final int col) {
        return matrix.get(row, col);
    }

    @Override
    protected DMatrixRMaj[] makeArray(final int length) {
        return new DMatrixRMaj[length];
    }

    @Override
    protected DMatrixRMaj multiply(final DMatrixRMaj... factors) {

        DMatrixRMaj retVal = factors[0];

        for (int f = 1; f < factors.length; f++) {
            DMatrixRMaj prod = new DMatrixRMaj(retVal.numRows, factors[f].numCols);
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
        DMatrixRMaj prod = new DMatrixRMaj(left.numRows, right.numCols);
        CommonOps_DDRM.subtract(left, right, prod);
        return prod;
    }

}
