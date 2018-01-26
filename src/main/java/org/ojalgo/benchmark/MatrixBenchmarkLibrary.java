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
package org.ojalgo.benchmark;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixScalarOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;
import org.ojalgo.benchmark.lab.library.ACM;
import org.ojalgo.benchmark.lab.library.EJML;
import org.ojalgo.benchmark.lab.library.MTJ;
import org.ojalgo.benchmark.lab.library.ojAlgo;

public abstract class MatrixBenchmarkLibrary<I, T extends I> {

    /**
     * @deprecated
     */
    @Deprecated
    public abstract class HermitianSolver implements BinaryOperator<I> {

        public abstract I apply(final I body, final I rhs);

        @SuppressWarnings("unchecked")
        public final Object solve(final Object body, final Object rhs) {
            return this.apply((I) body, (I) rhs);
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public abstract class LeastSquaresSolver implements BinaryOperator<I> {

        public abstract I apply(final I body, final I rhs);

        @SuppressWarnings("unchecked")
        public final Object solve(final Object body, final Object rhs) {
            return this.apply((I) body, (I) rhs);
        }

    }

    public abstract class MatrixBuilder implements Supplier<I> {

        public abstract MatrixBuilder set(int row, int col, double value);

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

    /**
     * @deprected Replace with something new named getOperation*
     */
    public abstract HermitianSolver getHermitianSolver();

    /**
     * @deprected Replace with something new named getOperation*
     */
    public abstract LeastSquaresSolver getLeastSquaresSolver();

    public abstract MatrixBuilder getMatrixBuilder(int numberOfRows, int numberOfColumns);

    public abstract ProducingUnaryMatrixOperation<I, T> getOperationEigenvectors(int dim);

    public abstract DecompositionOperation<I, I> getOperationEvD(int dim);

    /**
     * <pre>
     * arg1 == left input matrix
     * arg2 == right input matrix
     * ret == product - the results of matrix multiplication [left]x[right] should end up in that matrix
     * </pre>
     */
    public abstract MutatingBinaryMatrixMatrixOperation<I, T> getOperationFillByMultiplying();

    public abstract MutatingBinaryMatrixMatrixOperation<I, T> getOperationAdd();

    public abstract MutatingBinaryMatrixScalarOperation<I, T> getOperationScale();

    public abstract ProducingBinaryMatrixMatrixOperation<I, I> getOperationMultiplyToProduce();

    public abstract ProducingUnaryMatrixOperation<I, T> getOperationPseudoinverse(int dim);

    public abstract MutatingBinaryMatrixMatrixOperation<I, T> getOperationSolveGeneral(int dim);

    public abstract DecompositionOperation<I, I> getOperationSVD(int dim);

    protected abstract double[][] convertFrom(I matrix);

    protected abstract I convertTo(double[][] raw);

    protected abstract T copy(I source, T destination);

}
