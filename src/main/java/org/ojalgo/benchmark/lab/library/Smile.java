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

import static smile.linalg.Transpose.NO_TRANSPOSE;
import static smile.linalg.Transpose.TRANSPOSE;

import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixScalarOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.PropertyOperation;

import smile.linalg.NativeLibrary;
import smile.linalg.UPLO;
import smile.tensor.DenseMatrix;
import smile.tensor.EVD;
import smile.tensor.ScalarType;
import smile.tensor.SVD;

/**
 * SMILE (Statistical Machine Intelligence and Learning Engine) – pure Java. Its linear algebra backend is set
 * to "java", even if there is a native BLAS/LAPACK to use.
 * <p>
 * The decompositions overwrite the matrix they're called on, so the input is copied first. That is part of
 * what is measured, as it is for the other libraries that don't decompose in place.
 *
 * @see SmileNative
 */
public class Smile extends MatrixBenchmarkLibrary<DenseMatrix, DenseMatrix> {

    public Smile() {
        this(false);
    }

    /**
     * SMILE reads the backend system property every time it chooses between native and Java code, so it only
     * has to be set before the first operation.
     */
    Smile(final boolean nativeCode) {

        super();

        System.setProperty(NativeLibrary.BACKEND_PROPERTY, nativeCode ? "native" : "java");

        // With the "native" backend these throw if there is no native library – fail now rather than in the benchmark
        if (NativeLibrary.isBlasAvailable() != nativeCode || NativeLibrary.isLapackAvailable() != nativeCode) {
            throw new IllegalStateException("Not able to configure SMILE's linear algebra backend!");
        }
    }

    @Override
    public MatrixBenchmarkLibrary<DenseMatrix, DenseMatrix>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final DenseMatrix myMatrix = DenseMatrix.zeros(ScalarType.Float64, numberOfRows, numberOfColumns);

            @Override
            public DenseMatrix get() {
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
    public MutatingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationAdd() {
        return (a, b, c) -> c.add(1.0, a, 1.0, b);
    }

    @Override
    public PropertyOperation<DenseMatrix, DenseMatrix> getOperationDeterminant(final int dim) {
        return matA -> matA.copy().lu().det();
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationEquationSystemSolver(final int numbEquations, final int numbVariables,
            final int numbSolutions, final boolean spd) {
        if (numbEquations == numbVariables) {
            if (spd) {
                return (body, rhs) -> {
                    DenseMatrix solution = rhs.copy();
                    body.copy().withUplo(UPLO.LOWER).cholesky().solve(solution);
                    return solution;
                };
            }
            return (body, rhs) -> {
                DenseMatrix solution = rhs.copy();
                body.copy().lu().solve(solution);
                return solution;
            };
        }
        if (numbEquations <= numbVariables) {
            return null;
        }
        return (body, rhs) -> {
            DenseMatrix solution = rhs.copy();
            body.copy().qr().solve(solution);
            // The solution is in the first numbVariables rows
            return solution.submatrix(0, 0, numbVariables, numbSolutions);
        };
    }

    @Override
    public DecompositionOperation<DenseMatrix, DenseMatrix> getOperationEvD(final int dim) {

        DenseMatrix[] ret = this.makeArray(3);

        return matrix -> {
            // Flagged as symmetric, to get the symmetric algorithm
            EVD evd = matrix.copy().withUplo(UPLO.LOWER).eigen(false, true);
            ret[0] = evd.Vr();
            ret[1] = evd.diag();
            ret[2] = ret[0].transpose();
            return ret;
        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationFillByMultiplying(final boolean transpL, final boolean transpR) {
        return (left, right, product) -> DenseMatrix.mm(1.0, transpL ? TRANSPOSE : NO_TRANSPOSE, left, transpR ? TRANSPOSE : NO_TRANSPOSE, right, 0.0,
                product);
    }

    @Override
    public MutatingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationInvert(final int dim, final boolean spd) {
        if (spd) {
            return (matA, result) -> this.copy(matA.copy().withUplo(UPLO.LOWER).cholesky().inverse(), result);
        }
        return (matA, result) -> this.copy(matA.inverse(), result);
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<DenseMatrix, DenseMatrix> getOperationMultiplyToProduce() {
        return DenseMatrix::mm;
    }

    @Override
    public ProducingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationPseudoinverse(final int dim) {
        return matrix -> matrix.copy().svd(true).pinv();
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<DenseMatrix, DenseMatrix> getOperationScale() {
        return (a, s, b) -> this.copy(a, b).scale(s);
    }

    @Override
    public DecompositionOperation<DenseMatrix, DenseMatrix> getOperationSVD(final int dim) {

        DenseMatrix[] ret = this.makeArray(3);

        return matrix -> {
            SVD svd = matrix.copy().svd(true);
            ret[0] = svd.U();
            ret[1] = svd.diag();
            ret[2] = svd.Vt();
            return ret;
        };
    }

    @Override
    public MutatingUnaryMatrixOperation<DenseMatrix, DenseMatrix> getOperationTranspose() {
        return (matA, result) -> this.copy(matA.transpose(), result);
    }

    /**
     * Matrices of the same shape have the same (padded) memory layout.
     */
    @Override
    protected DenseMatrix copy(final DenseMatrix source, final DenseMatrix destination) {
        destination.memory().copyFrom(source.memory());
        return destination;
    }

    @Override
    protected int countColumns(final DenseMatrix matrix) {
        return matrix.ncol();
    }

    @Override
    protected int countRows(final DenseMatrix matrix) {
        return matrix.nrow();
    }

    @Override
    protected double doubleValue(final DenseMatrix matrix, final int row, final int col) {
        return matrix.get(row, col);
    }

    @Override
    protected DenseMatrix[] makeArray(final int length) {
        return new DenseMatrix[length];
    }

    @Override
    protected DenseMatrix multiply(final DenseMatrix... factors) {

        DenseMatrix retVal = factors[0];

        for (int f = 1; f < factors.length; f++) {
            retVal = retVal.mm(factors[f]);
        }

        return retVal;
    }

    @Override
    protected double norm(final DenseMatrix matrix) {
        return matrix.norm();
    }

    @Override
    protected DenseMatrix subtract(final DenseMatrix left, final DenseMatrix right) {
        return left.copy().sub(right);
    }

}
