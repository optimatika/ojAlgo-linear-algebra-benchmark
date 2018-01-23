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
package org.ojalgo.benchmark.matrix;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.ojalgo.benchmark.matrix.MatrixBenchmarkOperation.MutatingBinaryOperation;
import org.ojalgo.benchmark.matrix.library.ACM;
import org.ojalgo.benchmark.matrix.library.EJML;
import org.ojalgo.benchmark.matrix.library.MTJ;
import org.ojalgo.benchmark.matrix.library.ojAlgo;
import org.ojalgo.benchmark.matrix.operation.DecomposeEigen;
import org.ojalgo.benchmark.matrix.operation.Square3Multiply;
import org.ojalgo.benchmark.matrix.operation.Square3Multiply2;

public abstract class MatrixBenchmarkLibrary<I, T extends I> {

    /**
     * A general (square) equation system solver
     *
     * @author apete
     */
    public abstract class GeneralSolver implements BinaryOperator<I> {

        public abstract I apply(final I body, final I rhs);

        @SuppressWarnings("unchecked")
        public final Object solve(final Object body, final Object rhs) {
            return this.apply((I) body, (I) rhs);
        }

    }

    public abstract class HermitianSolver implements BinaryOperator<I> {

        public abstract I apply(final I body, final I rhs);

        @SuppressWarnings("unchecked")
        public final Object solve(final Object body, final Object rhs) {
            return this.apply((I) body, (I) rhs);
        }

    }

    public abstract class LeastSquaresSolver implements BinaryOperator<I> {

        public abstract I apply(final I body, final I rhs);

        @SuppressWarnings("unchecked")
        public final Object solve(final Object body, final Object rhs) {
            return this.apply((I) body, (I) rhs);
        }

    }

    public abstract class LeftTransposedMultiplier implements BinaryOperator<I> {

        public abstract I apply(final I left, final I right);

        @SuppressWarnings("unchecked")
        public final Object multiply(final Object left, final Object right) {
            return this.apply((I) left, (I) left);
        }

    }

    public abstract class MatrixBuilder implements Supplier<I> {

        public abstract MatrixBuilder set(int row, int col, double value);

    }

    public abstract class RightTransposedMultiplier implements BinaryOperator<I> {

        public abstract I apply(final I left, final I right);

        @SuppressWarnings("unchecked")
        public final Object multiply(final Object left, final Object right) {
            return this.apply((I) left, (I) left);
        }

    }

    public abstract class SingularDecomposer implements UnaryOperator<I> {

        public abstract I apply(final I matrix);

        @SuppressWarnings("unchecked")
        public final Object decompose(final Object matrix) {
            return this.apply((I) matrix);
        }

    }

    public static final Map<String, MatrixBenchmarkLibrary<?, ?>> LIBRARIES = new HashMap<String, MatrixBenchmarkLibrary<?, ?>>();

    static {
        LIBRARIES.put(ACM.class.getSimpleName(), new ACM());
        LIBRARIES.put(EJML.class.getSimpleName(), new EJML());
        LIBRARIES.put(ojAlgo.class.getSimpleName(), new ojAlgo());
        LIBRARIES.put(MTJ.class.getSimpleName(), new MTJ());
    }

    public MatrixBenchmarkLibrary() {
        super();
    }

    @SuppressWarnings("unchecked")
    public final Object convert(final Object object) {
        if (object.getClass().isArray()) {
            return this.convertTo((double[][]) object);
        } else {
            return this.convertFrom((I) object);
        }
    }

    public abstract DecomposeEigen.TaskDefinition<I> getEigenDecomposer();

    public abstract GeneralSolver getGeneralSolver();

    public abstract HermitianSolver getHermitianSolver();

    public abstract LeastSquaresSolver getLeastSquaresSolver();

    public abstract LeftTransposedMultiplier getLeftTransposedMultiplier();

    public abstract MatrixBuilder getMatrixBuilder(int numberOfRows, int numberOfColumns);

    public abstract Square3Multiply.TaskDefinition<I> getMatrixMultiplier();

    public abstract Square3Multiply2.TaskDefinition<I> getMatrixMultiplier2();

    public abstract MutatingBinaryOperation<I, T> getFillByMultiplyingOperation();

    public abstract RightTransposedMultiplier getRightTransposedMultiplier();

    public abstract SingularDecomposer getSingularDecomposer();

    protected abstract double[][] convertFrom(I matrix);

    protected abstract I convertTo(double[][] raw);

}
