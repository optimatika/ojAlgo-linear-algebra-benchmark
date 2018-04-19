/*
 * Copyright 1997-2018 Optimatika (www.optimatika.se)
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
import java.util.function.Supplier;

import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixScalarOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.PropertyOperation;
import org.ojalgo.benchmark.lab.library.ACM;
import org.ojalgo.benchmark.lab.library.EJML;
import org.ojalgo.benchmark.lab.library.MTJ;
import org.ojalgo.benchmark.lab.library.ojAlgo;
import org.ojalgo.constant.PrimitiveMath;
import org.ojalgo.netio.BasicLogger;

/**
 * <p>
 * The type paramater I is intended to be some high level interface that defines the shape of the matrix as
 * well as how to get individual elements.
 * </p>
 * <p>
 * The type paramater T should be set to some specific, fully mutable, implementation.
 * </p>
 *
 * @author apete
 */
public abstract class MatrixBenchmarkLibrary<I, T extends I> {

    public abstract class ElementsExtractor {

        public abstract double get(int row, int col);

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
    public boolean equals(final Object expected, final int dim, final Object actual) {

        I left;
        if (expected.getClass().isArray()) {
            left = this.multiply((I[]) expected);
        } else {
            left = (I) expected;
        }

        I right;
        if (actual.getClass().isArray()) {
            right = this.multiply((I[]) actual);
        } else {
            right = (I) actual;
        }

        final I diff = this.subtract(left, right);

        final double error = this.norm(diff);

        final boolean retVal = (error <= (dim * dim * PrimitiveMath.MACHINE_EPSILON));

        if (!retVal) {
            BasicLogger.debug("Error {} too large for {}", error, dim);
        }

        return retVal;
    }

    public abstract MatrixBuilder getMatrixBuilder(int numberOfRows, int numberOfColumns);

    public abstract MutatingBinaryMatrixMatrixOperation<I, T> getOperationAdd();

    public abstract PropertyOperation<I, T> getOperationDeterminant(int dim);

    public abstract ProducingBinaryMatrixMatrixOperation<I, T> getOperationEquationSystemSolver(int numbEquations, int numbVariables, int numbSolutions,
            boolean spd);

    public abstract DecompositionOperation<I, I> getOperationEvD(int dim);

    /**
     * <pre>
     * arg1 == left input matrix
     * arg2 == right input matrix
     * ret == product - the results of matrix multiplication [left]x[right] should end up in that matrix
     * </pre>
     * @param transpL TODO
     * @param transpR TODO
     */
    public abstract MutatingBinaryMatrixMatrixOperation<I, T> getOperationFillByMultiplying(boolean transpL, boolean transpR);

    public abstract MutatingUnaryMatrixOperation<I, T> getOperationInvert(int dim, boolean spd);

    public abstract ProducingBinaryMatrixMatrixOperation<I, I> getOperationMultiplyToProduce();

    public abstract ProducingUnaryMatrixOperation<I, T> getOperationPseudoinverse(int dim);

    public abstract MutatingBinaryMatrixScalarOperation<I, T> getOperationScale();

    public abstract DecompositionOperation<I, I> getOperationSVD(int dim);

    public abstract MutatingUnaryMatrixOperation<I, T> getOperationTranspose();

    protected abstract T copy(I source, T destination);

    protected abstract I[] makeArray(int length);

    @SuppressWarnings("unchecked")
    protected abstract I multiply(I... factors);

    protected abstract double norm(I matrix);

    protected abstract I subtract(I left, I right);

}
