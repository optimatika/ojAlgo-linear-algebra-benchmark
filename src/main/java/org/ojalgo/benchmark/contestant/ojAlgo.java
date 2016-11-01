/*
 * Copyright 1997-2015 Optimatika (www.optimatika.se)
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
package org.ojalgo.benchmark.contestant;

import org.ojalgo.benchmark.BenchmarkContestant;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.matrix.task.SolverTask;
import org.ojalgo.matrix.task.TaskException;

/**
 * oj! Algorithms
 */
public class ojAlgo extends BenchmarkContestant<MatrixStore<Double>> {

    @Override
    public BenchmarkContestant<MatrixStore<Double>>.EigenDecomposer getEigenDecomposer() {
        return new EigenDecomposer() {

            @Override
            public MatrixStore<Double> apply(final MatrixStore<Double> matrix) {
                final Eigenvalue<Double> tmpEvD = Eigenvalue.make(matrix, true);
                tmpEvD.decompose(matrix);
                return tmpEvD.getV();
            }

        };
    }

    @Override
    public GeneralSolver getGeneralSolver() {
        return new GeneralSolver() {

            @Override
            public MatrixStore<Double> apply(final MatrixStore<Double> body, final MatrixStore<Double> rhs) {
                try {
                    return SolverTask.PRIMITIVE.make(body, rhs, false, false).solve(body, rhs);
                } catch (final TaskException exc) {
                    throw new IllegalArgumentException(exc);
                }
            }

        };
    }

    @Override
    public HermitianSolver getHermitianSolver() {
        return new HermitianSolver() {

            @Override
            public MatrixStore<Double> apply(final MatrixStore<Double> body, final MatrixStore<Double> rhs) {
                try {
                    return SolverTask.PRIMITIVE.make(body, rhs, true, false).solve(body, rhs);
                } catch (final TaskException exc) {
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
                } catch (final TaskException exc) {
                    throw new IllegalArgumentException(exc);
                }
            }

        };
    }

    @Override
    public BenchmarkContestant<MatrixStore<Double>>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final PrimitiveDenseStore myMatrix = PrimitiveDenseStore.FACTORY.makeZero(numberOfRows, numberOfColumns);

            public MatrixStore<Double> get() {
                return myMatrix;
            }

            @Override
            public void set(final int row, final int col, final double value) {
                myMatrix.set(row, col, value);
            }

        };
    }

    @Override
    public MatrixMultiplier getMatrixMultiplier() {
        return new MatrixMultiplier() {

            @Override
            public MatrixStore<Double> apply(final MatrixStore<Double> left, final MatrixStore<Double> right) {

                final PrimitiveDenseStore retVal = PrimitiveDenseStore.FACTORY.makeZero(left.countRows(), right.countColumns());

                retVal.fillByMultiplying(left, right);

                return retVal;
            }

        };
    }

    @Override
    public BenchmarkContestant<MatrixStore<Double>>.SingularDecomposer getSingularDecomposer() {
        return new SingularDecomposer() {

            @Override
            public MatrixStore<Double> apply(final MatrixStore<Double> matrix) {
                final SingularValue<Double> tmpDecomposer = SingularValue.make(matrix);
                tmpDecomposer.decompose(matrix);
                tmpDecomposer.getQ1();
                tmpDecomposer.getD();
                return tmpDecomposer.getQ2();
            }

        };
    }

    @Override
    public TransposedMultiplier getTransposedMultiplier() {
        return new TransposedMultiplier() {

            @Override
            public MatrixStore<Double> apply(final MatrixStore<Double> left, final MatrixStore<Double> right) {

                final PrimitiveDenseStore retVal = PrimitiveDenseStore.FACTORY.makeZero(left.countRows(), right.countColumns());

                retVal.fillByMultiplying(left, right.transpose());

                return retVal;
            }

        };
    }

    @Override
    protected double[][] convertFrom(final MatrixStore<Double> matrix) {
        return matrix.toRawCopy2D();
    }

    @Override
    protected MatrixStore<Double> convertTo(final double[][] raw) {
        return PrimitiveDenseStore.FACTORY.rows(raw);
    }

}
