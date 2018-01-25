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

import org.ojalgo.RecoverableCondition;
import org.ojalgo.access.Structure2D;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryOperation;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.matrix.task.SolverTask;

/**
 * oj! Algorithms
 */
public class ojAlgo extends MatrixBenchmarkLibrary<MatrixStore<Double>, PrimitiveDenseStore> {

    @Override
    public HermitianSolver getHermitianSolver() {
        return new HermitianSolver() {

            @Override
            public MatrixStore<Double> apply(final MatrixStore<Double> body, final MatrixStore<Double> rhs) {
                try {
                    return SolverTask.PRIMITIVE.make(body, rhs, true, false).solve(body, rhs);
                } catch (final RecoverableCondition exc) {
                    throw new IllegalArgumentException(exc);
                }
            }

        };
    }

    @Override
    public LeastSquaresSolver getLeastSquaresSolver() {
        return new LeastSquaresSolver() {

            @Override
            public MatrixStore<Double> apply(final MatrixStore<Double> body, final MatrixStore<Double> rhs) {
                try {
                    return SolverTask.PRIMITIVE.make(body, rhs, false, false).solve(body, rhs);
                } catch (final RecoverableCondition exc) {
                    throw new IllegalArgumentException(exc);
                }
            }

        };
    }

    @Override
    public MatrixBenchmarkLibrary<MatrixStore<Double>, PrimitiveDenseStore>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final PrimitiveDenseStore myMatrix = PrimitiveDenseStore.FACTORY.makeZero(numberOfRows, numberOfColumns);

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
    public ProducingUnaryOperation<MatrixStore<Double>, PrimitiveDenseStore> getOperationEigenvectors(final int dim) {

        final Structure2D shape = new Structure2D() {

            public long countColumns() {
                return dim;
            }

            public long countRows() {
                return dim;
            }

        };

        final Eigenvalue<Double> evd = Eigenvalue.PRIMITIVE.make(shape, true);

        return (matrix) -> {
            evd.decompose(matrix);
            return evd.getV();
        };
    }

    @Override
    public MutatingBinaryOperation<MatrixStore<Double>, PrimitiveDenseStore> getOperationFillByMultiplying() {
        return (arg1, arg2, ret) -> ret.fillByMultiplying(arg1, arg2);
    }

    @Override
    public ProducingBinaryOperation<MatrixStore<Double>, MatrixStore<Double>> getOperationMultiplyToProduce() {
        return (arg1, arg2) -> arg1.multiply(arg2);
    }

    @Override
    public ProducingUnaryOperation<MatrixStore<Double>, PrimitiveDenseStore> getOperationPseudoinverse(final int dim) {

        final Structure2D shape = new Structure2D() {

            public long countColumns() {
                return dim;
            }

            public long countRows() {
                return dim;
            }

        };

        final SingularValue<Double> svd = SingularValue.PRIMITIVE.make(shape);
        final PhysicalStore<Double> preallocated = svd.preallocate(shape);

        return (matrix) -> {
            svd.decompose(matrix);
            return svd.getInverse(preallocated);
        };
    }

    @Override
    public MutatingBinaryOperation<MatrixStore<Double>, PrimitiveDenseStore> getOperationSolveGeneral(final int dim) {

        final Structure2D bodyShape = new Structure2D() {

            public long countColumns() {
                return dim;
            }

            public long countRows() {
                return dim;
            }

        };

        final Structure2D rhsShape = new Structure2D() {

            public long countColumns() {
                return dim;
            }

            public long countRows() {
                return dim;
            }

        };

        final SolverTask<Double> task = SolverTask.PRIMITIVE.make(bodyShape, rhsShape, false, false);

        return (body, rhs, sol) -> task.solve(body, rhs, sol);
    };

    @Override
    protected double[][] convertFrom(final MatrixStore<Double> matrix) {
        return matrix.toRawCopy2D();
    }

    @Override
    protected MatrixStore<Double> convertTo(final double[][] raw) {
        return PrimitiveDenseStore.FACTORY.rows(raw);
    }

    @Override
    protected PrimitiveDenseStore copy(final MatrixStore<Double> source, final PrimitiveDenseStore destination) {
        source.supplyTo(destination);
        return destination;
    }

}