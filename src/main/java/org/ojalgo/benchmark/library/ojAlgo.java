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

import java.util.Optional;

import org.ojalgo.array.operation.NativeLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.*;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.function.constant.PrimitiveMath;
import org.ojalgo.matrix.decomposition.Cholesky;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.decomposition.LU;
import org.ojalgo.matrix.decomposition.QR;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.R064Store;
import org.ojalgo.matrix.task.DeterminantTask;
import org.ojalgo.matrix.task.InverterTask;
import org.ojalgo.matrix.task.SolverTask;

/**
 * oj! Algorithms – the operations, common to {@link JVM} and {@link Native}. Those only differ in whether
 * ojAlgo is allowed to use native code.
 * <p>
 * ojAlgo decides whether to use native code once, when {@link org.ojalgo.array.operation.NativeLibrary} is
 * initialised, so the subclasses' constructors must run before ojAlgo performs any operation that could be
 * delegated to native code. Each JMH fork benchmarks a single library, so that is satisfied as long as forks
 * are used.
 */
public abstract class ojAlgo extends MatrixBenchmarkLibrary<MatrixStore<Double>, R064Store> {

    /**
     * oj! Algorithms – pure Java. Native code is switched off, even if the JVM grants native access.
     *
     * @see Native
     */
    public static final class JVM extends ojAlgo {

        public JVM() {

            super();

            System.setProperty(NativeLibrary.PROPERTY_LIBRARY, NativeLibrary.NONE);

            if (NativeLibrary.isAvailable()) {
                throw new IllegalStateException("Not able to switch off ojAlgo's native code – it was already initialised. Are you running without forks?");
            }
        }

    }

    /**
     * oj! Algorithms – using native code (BLAS/LAPACK through the FFM API) where available. On macOS that is
     * Accelerate, otherwise OpenBLAS, MKL… A specific library can be chosen with the system property
     * <code>ojalgo.native.library</code>.
     *
     * @see JVM
     */
    public static final class Native extends ojAlgo {

        public Native() {

            super();

            if (!NativeLibrary.isAvailable()) {
                throw new IllegalStateException(
                        "No native BLAS available to ojAlgo! The (forked) JVM must be started with --enable-native-access=ALL-UNNAMED, and a native library must be found.");
            }
        }

    }

    ojAlgo() {
        super();
    }

    @Override
    public MatrixBenchmarkLibrary<MatrixStore<Double>, R064Store>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final R064Store myMatrix = R064Store.FACTORY.make(numberOfRows, numberOfColumns);

            @Override
            public MatrixStore<Double> get() {
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
    public MutatingBinaryMatrixMatrixOperation<MatrixStore<Double>, R064Store> getOperationAdd() {
        return (a, b, c) -> c.fillMatching(a, PrimitiveMath.ADD, b);
    }

    @Override
    public DecompositionOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationCholesky(final int dim) {

        MatrixStore<Double>[] ret = this.makeArray(1);

        Cholesky<Double> cholesky = Cholesky.R064.make(dim, dim);

        return matrix -> {
            cholesky.decompose(matrix);
            ret[0] = cholesky.getL();
            return ret;
        };
    }

    @Override
    public PropertyOperation<MatrixStore<Double>, R064Store> getOperationDeterminant(final int dim) {
        DeterminantTask<Double> task = DeterminantTask.R064.make(dim, false);
        return matA -> task.calculateDeterminant(matA);
    }

    @Override
    public ProducingUnaryMatrixOperation<MatrixStore<Double>, R064Store> getOperationEigenvalues(final int dim) {

        Eigenvalue<Double> evd = Eigenvalue.R064.make(dim, true);
        R064Store values = R064Store.FACTORY.make(dim, 1);

        return matrix -> {
            evd.computeValuesOnly(matrix);
            evd.getEigenvalues(values.data, Optional.empty());
            return values;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<MatrixStore<Double>, R064Store> getOperationEquationSystemSolver(final int numbEquations,
            final int numbVariables, final int numbSolutions, final boolean spd) {

        SolverTask<Double> task = SolverTask.R064.make(numbEquations, numbVariables, numbSolutions, spd, spd);

        PhysicalStore<Double> preallocated = task.preallocate(numbEquations, numbVariables, numbSolutions);

        return (body, rhs) -> task.solve(body, rhs, preallocated);
    }

    @Override
    public DecompositionOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationEvD(final int dim) {

        MatrixStore<Double>[] ret = this.makeArray(2);

        Eigenvalue<Double> evd = Eigenvalue.R064.make(dim, true);

        return matrix -> {
            evd.decompose(matrix);
            ret[0] = evd.getV();
            ret[1] = evd.getD();
            return ret;
        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<MatrixStore<Double>, R064Store> getOperationFillByMultiplying(final boolean transpL, final boolean transpR) {
        return (left, right, product) -> product.fillByMultiplying(transpL ? left.transpose() : left, transpR ? right.transpose() : right);
    }

    @Override
    public MutatingUnaryMatrixOperation<MatrixStore<Double>, R064Store> getOperationGram() {
        return (matrix, product) -> product.fillByMultiplying(matrix.transpose(), matrix);
    }

    @Override
    public MutatingUnaryMatrixOperation<MatrixStore<Double>, R064Store> getOperationInvert(final int dim, final boolean spd) {
        InverterTask<Double> task = InverterTask.R064.make(dim, spd);
        return task::invert; // Calculated in place – the returned inverse is result (the full matrix, also when SPD)
    }

    @Override
    public KalmanFilterOperation<MatrixStore<Double>, R064Store> getOperationKalmanFilter(final int dim, final int nbMeasurements) {

        R064Store predictedState = R064Store.FACTORY.make(dim, 1);
        R064Store transformed = R064Store.FACTORY.make(dim, dim);
        R064Store predicted = R064Store.FACTORY.make(dim, dim);
        R064Store projected = R064Store.FACTORY.make(nbMeasurements, dim);
        R064Store innovationCovariance = R064Store.FACTORY.make(nbMeasurements, nbMeasurements);
        R064Store innovation = R064Store.FACTORY.make(nbMeasurements, 1);
        R064Store correction = R064Store.FACTORY.make(dim, dim);

        Cholesky<Double> cholesky = Cholesky.R064.make(nbMeasurements, nbMeasurements);
        PhysicalStore<Double> preallocated = cholesky.preallocate(nbMeasurements, nbMeasurements, dim);

        return (transition, processNoise, observation, measurementNoise, state, covariance, measurement, updatedState, updatedCovariance) -> {

            predictedState.fillByMultiplying(transition, state);
            transformed.fillByMultiplying(transition, covariance);
            predicted.fillByMultiplying(transformed, transition.transpose());
            predicted.modifyMatching(PrimitiveMath.ADD, processNoise);

            projected.fillByMultiplying(observation, predicted);
            innovationCovariance.fillByMultiplying(projected, observation.transpose());
            innovationCovariance.modifyMatching(PrimitiveMath.ADD, measurementNoise);
            innovation.fillByMultiplying(observation, predictedState);
            innovation.modifyMatching(measurement, PrimitiveMath.SUBTRACT);

            // [K]^T = [S]^-1[H][P]-
            cholesky.decompose(innovationCovariance);
            MatrixStore<Double> gainTransposed = cholesky.getSolution(projected, preallocated);

            updatedState.fillByMultiplying(gainTransposed.transpose(), innovation);
            updatedState.modifyMatching(PrimitiveMath.ADD, predictedState);
            correction.fillByMultiplying(gainTransposed.transpose(), projected);
            updatedCovariance.fillMatching(predicted, PrimitiveMath.SUBTRACT, correction);
        };
    }

    @Override
    public DecompositionOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationLU(final int dim) {

        MatrixStore<Double>[] ret = this.makeArray(3);

        LU<Double> lu = LU.R064.make(dim, dim);
        MatrixStore<Double> identity = R064Store.FACTORY.makeIdentity(dim);

        return matrix -> {
            lu.decompose(matrix);
            // [A] with its rows in the pivot order is [L][U] – [P] is the identity with its rows in that order
            ret[0] = identity.rows(lu.getPivotOrder());
            ret[1] = lu.getL();
            ret[2] = lu.getU();
            return ret;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<MatrixStore<Double>, R064Store> getOperationMinimumNormSolver(final int numbEquations,
            final int numbVariables) {

        SingularValue<Double> svd = SingularValue.R064.make(numbEquations, numbVariables);
        PhysicalStore<Double> preallocated = svd.preallocate(numbEquations, numbVariables, 1);

        // Not svd.solve(...) – it requires full rank. The solution only uses the singular values above the rank threshold.
        return (body, rhs) -> {
            svd.decompose(body);
            return svd.getSolution(rhs, preallocated);
        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<MatrixStore<Double>, R064Store> getOperationMultiplyMatrixVector(final boolean transpose) {
        return (matrix, vector, product) -> product.fillByMultiplying(transpose ? matrix.transpose() : matrix, vector);
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationMultiplyToProduce() {
        return MatrixStore::multiply;
    }

    @Override
    public ProducingUnaryMatrixOperation<MatrixStore<Double>, R064Store> getOperationPseudoinverse(final int dim) {

        SingularValue<Double> svd = SingularValue.R064.make(dim, dim);
        PhysicalStore<Double> preallocated = svd.preallocate(dim, dim);

        return matrix -> {
            svd.decompose(matrix);
            return svd.getInverse(preallocated);
        };
    }

    @Override
    public DecompositionOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationQR(final int dim) {

        MatrixStore<Double>[] ret = this.makeArray(2);

        QR<Double> qr = QR.R064.make(dim, dim);

        return matrix -> {
            qr.decompose(matrix);
            ret[0] = qr.getQ();
            ret[1] = qr.getR();
            return ret;
        };
    }

    @Override
    public PreparingOperation<MatrixStore<Double>, R064Store> getOperationRepeatedSolver(final int dim) {
        return body -> {

            LU<Double> lu = LU.R064.make(body);
            lu.decompose(body);

            PhysicalStore<Double> preallocated = lu.preallocate(dim, dim, 1);

            return rhs -> lu.getSolution(rhs, preallocated);
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<MatrixStore<Double>, R064Store> getOperationRidgeRegression(final int numbEquations, final int numbVariables,
            final double lambda) {

        R064Store gram = R064Store.FACTORY.make(numbVariables, numbVariables);
        R064Store moment = R064Store.FACTORY.make(numbVariables, 1);
        UnaryFunction<Double> regularise = PrimitiveMath.ADD.by(lambda);

        Cholesky<Double> cholesky = Cholesky.R064.make(numbVariables, numbVariables);
        PhysicalStore<Double> preallocated = cholesky.preallocate(numbVariables, numbVariables, 1);

        return (design, observations) -> {

            gram.fillByMultiplying(design.transpose(), design);
            gram.modifyDiagonal(regularise);
            moment.fillByMultiplying(design.transpose(), observations);

            cholesky.decompose(gram);
            return cholesky.getSolution(moment, preallocated);
        };
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<MatrixStore<Double>, R064Store> getOperationScale() {
        return (a, s, b) -> b.fillMatching(PrimitiveMath.MULTIPLY.first(s), a);
    }

    @Override
    public ProducingUnaryMatrixOperation<MatrixStore<Double>, R064Store> getOperationSingularValues(final int dim) {

        SingularValue<Double> svd = SingularValue.R064.make(dim, dim);
        R064Store values = R064Store.FACTORY.make(dim, 1);

        return matrix -> {
            svd.computeValuesOnly(matrix);
            svd.getSingularValues(values.data);
            return values;
        };
    }

    @Override
    public DecompositionOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationSVD(final int dim) {

        MatrixStore<Double>[] ret = this.makeArray(3);
        SingularValue<Double> svd = SingularValue.R064.make(dim, dim);

        return matrix -> {
            svd.decompose(matrix);
            ret[0] = svd.getU();
            ret[1] = svd.getS();
            ret[2] = svd.getV().transpose();
            return ret;
        };
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<MatrixStore<Double>, R064Store> getOperationSymmetricIndefiniteSolver(final int dim) {

        // Symmetric but not positive definite – SolverTask chooses LU, measured faster on 10-1000 than LDL (much faster with native code)
        SolverTask<Double> task = SolverTask.R064.make(dim, dim, 1, true, false);

        PhysicalStore<Double> preallocated = task.preallocate(dim, dim, 1);

        return (body, rhs) -> task.solve(body, rhs, preallocated);
    }

    @Override
    public DecompositionOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationThinSVD(final int numbRows, final int numbCols) {

        MatrixStore<Double>[] ret = this.makeArray(3);
        SingularValue<Double> svd = SingularValue.R064.make(numbRows, numbCols);

        return matrix -> {
            svd.decompose(matrix);
            ret[0] = svd.getU();
            ret[1] = svd.getS();
            ret[2] = svd.getV().transpose();
            return ret;
        };
    }

    @Override
    public MutatingUnaryMatrixOperation<MatrixStore<Double>, R064Store> getOperationTranspose() {
        return (arg, ret) -> ret.fillMatching(arg.transpose());
    }

    @Override
    protected R064Store copy(final MatrixStore<Double> source, final R064Store destination) {
        source.supplyTo(destination);
        return destination;
    }

    @Override
    protected int countColumns(final MatrixStore<Double> matrix) {
        return matrix.getColDim();
    }

    @Override
    protected int countRows(final MatrixStore<Double> matrix) {
        return matrix.getRowDim();
    }

    @Override
    protected double doubleValue(final MatrixStore<Double> matrix, final int row, final int col) {
        return matrix.doubleValue(row, col);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected MatrixStore<Double>[] makeArray(final int length) {
        return (MatrixStore<Double>[]) new MatrixStore<?>[length];
    }

    @Override
    protected MatrixStore<Double> multiply(final MatrixStore<Double>... factors) {

        MatrixStore<Double> retVal = factors[0];

        for (int f = 1; f < factors.length; f++) {
            retVal = retVal.multiply(factors[f]);
        }

        return retVal;
    }

    @Override
    protected double norm(final MatrixStore<Double> matrix) {
        return matrix.norm();
    }

    @Override
    protected MatrixStore<Double> subtract(final MatrixStore<Double> left, final MatrixStore<Double> right) {
        return left.subtract(right);
    }

}
