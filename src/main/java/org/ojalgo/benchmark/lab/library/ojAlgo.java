/*
 * Copyright 1997-2022 Optimatika (www.optimatika.se)
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
import org.ojalgo.matrix.store.Primitive64Store;
import org.ojalgo.matrix.task.DeterminantTask;
import org.ojalgo.matrix.task.InverterTask;
import org.ojalgo.matrix.task.SolverTask;

/**
 * oj! Algorithms
 */
public class ojAlgo extends MatrixBenchmarkLibrary<MatrixStore<Double>, Primitive64Store> {

    @Override
    public MatrixBenchmarkLibrary<MatrixStore<Double>, Primitive64Store>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final Primitive64Store myMatrix = Primitive64Store.FACTORY.make(numberOfRows, numberOfColumns);

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
    public MutatingBinaryMatrixMatrixOperation<MatrixStore<Double>, Primitive64Store> getOperationAdd() {
        return (a, b, c) -> c.fillMatching(a, PrimitiveMath.ADD, b);
    }

    @Override
    public PropertyOperation<MatrixStore<Double>, Primitive64Store> getOperationDeterminant(final int dim) {
        DeterminantTask<Double> task = DeterminantTask.PRIMITIVE.make(dim, false);
        return matA -> task.calculateDeterminant(matA);
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<MatrixStore<Double>, Primitive64Store> getOperationEquationSystemSolver(final int numbEquations,
            final int numbVariables, final int numbSolutions, final boolean spd) {

        SolverTask<Double> task = SolverTask.PRIMITIVE.make(numbEquations, numbVariables, numbSolutions, spd, spd);

        PhysicalStore<Double> preallocated = task.preallocate(numbEquations, numbVariables, numbSolutions);

        return (body, rhs) -> task.solve(body, rhs, preallocated);
    }

    @Override
    public DecompositionOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationEvD(final int dim) {

        MatrixStore<Double>[] ret = this.makeArray(3);

        Eigenvalue<Double> evd = Eigenvalue.PRIMITIVE.make(dim, true);

        return matrix -> {
            evd.decompose(matrix);
            ret[0] = evd.getV();
            ret[1] = evd.getD();
            ret[2] = ret[0].transpose();
            return ret;
        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<MatrixStore<Double>, Primitive64Store> getOperationFillByMultiplying(final boolean transpL,
            final boolean transpR) {
        return (left, right, product) -> product.fillByMultiplying(transpL ? left.transpose() : left, transpR ? right.transpose() : right);
    }

    @Override
    public MutatingUnaryMatrixOperation<MatrixStore<Double>, Primitive64Store> getOperationInvert(final int dim, final boolean spd) {
        InverterTask<Double> task = InverterTask.PRIMITIVE.make(dim, spd);
        return task::invert;
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationMultiplyToProduce() {
        return MatrixStore::multiply;
    }

    @Override
    public ProducingUnaryMatrixOperation<MatrixStore<Double>, Primitive64Store> getOperationPseudoinverse(final int dim) {

        SingularValue<Double> svd = SingularValue.PRIMITIVE.make(dim, dim);
        PhysicalStore<Double> preallocated = svd.preallocate(dim, dim);

        return matrix -> {
            svd.decompose(matrix);
            return svd.getInverse(preallocated);
        };
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<MatrixStore<Double>, Primitive64Store> getOperationScale() {
        return (a, s, b) -> b.fillMatching(PrimitiveMath.MULTIPLY.first(s), a);
    }

    @Override
    public DecompositionOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationSVD(final int dim) {

        MatrixStore<Double>[] ret = this.makeArray(3);
        SingularValue<Double> svd = SingularValue.PRIMITIVE.make(dim, dim);

        return matrix -> {
            svd.decompose(matrix);
            ret[0] = svd.getU();
            ret[1] = svd.getD();
            ret[2] = svd.getV().transpose();
            return ret;
        };
    }

    @Override
    public MutatingUnaryMatrixOperation<MatrixStore<Double>, Primitive64Store> getOperationTranspose() {
        return (arg, ret) -> ret.fillMatching(arg.transpose());
    }

    @Override
    protected Primitive64Store copy(final MatrixStore<Double> source, final Primitive64Store destination) {
        source.supplyTo(destination);
        return destination;
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
