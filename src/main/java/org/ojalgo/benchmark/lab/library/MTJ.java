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

import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.DecompositionOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.MutatingBinaryMatrixScalarOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingBinaryMatrixMatrixOperation;
import org.ojalgo.benchmark.MatrixBenchmarkOperation.ProducingUnaryMatrixOperation;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.SVD;
import no.uib.cipr.matrix.SymmDenseEVD;
import no.uib.cipr.matrix.SymmTridiagMatrix;

/**
 * Matrix Toolkits Java
 */
public class MTJ extends MatrixBenchmarkLibrary<Matrix, Matrix> {

    @Override
    public HermitianSolver getHermitianSolver() {
        return new HermitianSolver() {

            @Override
            public Matrix apply(final Matrix body, final Matrix rhs) {

                final DenseMatrix result = new DenseMatrix(body.numColumns(), rhs.numColumns());

                body.solve(rhs, result);

                return result;
            }

        };
    }

    @Override
    public LeastSquaresSolver getLeastSquaresSolver() {
        return new LeastSquaresSolver() {

            @Override
            public Matrix apply(final Matrix body, final Matrix rhs) {

                final DenseMatrix result = new DenseMatrix(body.numColumns(), rhs.numColumns());

                body.solve(rhs, result);

                return result;
            }

        };
    }

    @Override
    public MatrixBenchmarkLibrary<Matrix, Matrix>.MatrixBuilder getMatrixBuilder(final int numberOfRows, final int numberOfColumns) {
        return new MatrixBuilder() {

            private final DenseMatrix myMatrix = new DenseMatrix(numberOfRows, numberOfColumns);

            public Matrix get() {
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
    public MutatingBinaryMatrixMatrixOperation<Matrix, Matrix> getOperationAdd() {
        return (a, b, c) -> c.set(a).add(b);
    }

    @Override
    public ProducingUnaryMatrixOperation<Matrix, Matrix> getOperationEigenvectors(final int dim) {
        return (input) -> SymmDenseEVD.factorize(input).getEigenvectors();
    }

    @Override
    public DecompositionOperation<Matrix, Matrix> getOperationEvD(final int dim) {

        final Matrix[] ret = new Matrix[3];
        final double[] offDiag = new double[dim - 1];

        return (matrix) -> {
            final SymmDenseEVD evd = SymmDenseEVD.factorize(matrix);
            ret[0] = new SymmTridiagMatrix(evd.getEigenvalues(), offDiag);
            ret[1] = evd.getEigenvectors();
            return ret;
        };
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<Matrix, Matrix> getOperationFillByMultiplying() {
        return (left, right, product) -> left.mult(right, product);
    }

    @Override
    public ProducingBinaryMatrixMatrixOperation<Matrix, Matrix> getOperationMultiplyToProduce() {
        return (left, right) -> {
            final DenseMatrix product = new DenseMatrix(left.numRows(), left.numColumns());
            return left.mult(right, product);
        };
    }

    @Override
    public ProducingUnaryMatrixOperation<Matrix, Matrix> getOperationPseudoinverse(final int dim) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutatingBinaryMatrixScalarOperation<Matrix, Matrix> getOperationScale() {
        return (a, s, b) -> b.set(s, a);
    }

    @Override
    public MutatingBinaryMatrixMatrixOperation<Matrix, Matrix> getOperationSolveGeneral(final int dim) {
        return (body, rhs, sol) -> body.solve(rhs, sol);
    }

    @Override
    public DecompositionOperation<Matrix, Matrix> getOperationSVD(final int dim) {

        final Matrix[] ret = new Matrix[3];
        final double[] offDiag = new double[dim - 1];

        return (matrix) -> {
            final SVD svd = SVD.factorize(matrix);
            ret[0] = svd.getU();
            ret[1] = new SymmTridiagMatrix(svd.getS(), offDiag);
            ret[2] = svd.getVt().transpose();
            return ret;
        };
    }

    @Override
    protected double[][] convertFrom(final Matrix matrix) {
        final double[][] retVal = new double[matrix.numRows()][matrix.numColumns()];
        for (int i = 0; i < retVal.length; i++) {
            final double[] tmpRow = retVal[i];
            for (int j = 0; j < tmpRow.length; j++) {
                tmpRow[j] = matrix.get(i, j);
            }
        }
        return retVal;
    }

    @Override
    protected Matrix convertTo(final double[][] raw) {
        return new DenseMatrix(raw);
    }

    @Override
    protected Matrix copy(final Matrix source, final Matrix destination) {
        destination.set(source);
        return destination;
    }

    @Override
    protected Matrix multiply(final Matrix... factors) {

        Matrix retVal = factors[0];

        for (int f = 1; f < factors.length; f++) {
            retVal = retVal.mult(factors[f], new DenseMatrix(retVal.numRows(), factors[f].numColumns()));
        }

        return retVal;
    }

}
