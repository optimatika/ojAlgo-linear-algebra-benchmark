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

import org.ojalgo.array.operation.NativeLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixScalarOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.PropertyOperation;
import org.ojalgo.function.constant.PrimitiveMath;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.R064Store;
import org.ojalgo.matrix.task.DeterminantTask;
import org.ojalgo.matrix.task.InverterTask;
import org.ojalgo.matrix.task.SolverTask;

/**
 * oj! Algorithms – pure Java. Native code is switched off, even if the JVM grants native access.
 *
 * @see ojAlgoNative
 */
public class ojAlgo extends MatrixBenchmarkLibrary<MatrixStore<Double>, R064Store> {

    public ojAlgo() {
        this(false);
    }

    /**
     * ojAlgo decides whether to use native code once, when {@link NativeLibrary} is initialised, so this must
     * happen before ojAlgo performs any operation that could be delegated to native code. Each JMH fork
     * benchmarks a single library, so that is satisfied as long as forks are used.
     */
    ojAlgo(final boolean nativeCode) {

        super();

        if (!nativeCode) {
            System.setProperty(NativeLibrary.PROPERTY_LIBRARY, NativeLibrary.NONE);
        }

        if (NativeLibrary.isAvailable() != nativeCode) {
            if (nativeCode) {
                throw new IllegalStateException(
                        "No native BLAS available to ojAlgo! The (forked) JVM must be started with --enable-native-access=ALL-UNNAMED, and a native library must be found.");
            } else {
                throw new IllegalStateException("Not able to switch off ojAlgo's native code – it was already initialised. Are you running without forks?");
            }
        }
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
    public PropertyOperation<MatrixStore<Double>, R064Store> getOperationDeterminant(final int dim) {
        DeterminantTask<Double> task = DeterminantTask.R064.make(dim, false);
        return matA -> task.calculateDeterminant(matA);
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
    public MutatingUnaryMatrixOperation<MatrixStore<Double>, R064Store> getOperationInvert(final int dim, final boolean spd) {
        InverterTask<Double> task = InverterTask.R064.make(dim, spd);
        return task::invert; // Calculated in place – the returned inverse is result (the full matrix, also when SPD)
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
    public MutatingBinaryMatrixScalarOperation<MatrixStore<Double>, R064Store> getOperationScale() {
        return (a, s, b) -> b.fillMatching(PrimitiveMath.MULTIPLY.first(s), a);
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
