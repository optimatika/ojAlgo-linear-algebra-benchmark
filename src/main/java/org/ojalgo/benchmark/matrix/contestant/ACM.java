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
package org.ojalgo.benchmark.matrix.contestant;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.ojalgo.benchmark.matrix.BenchmarkContestant;

/**
 * Apache Commons Math
 */
public class ACM extends BenchmarkContestant<RealMatrix> {

	@Override
	protected double[][] convertFrom(final RealMatrix matrix) {
		return matrix.getData();
	}

	@Override
	protected RealMatrix convertTo(final double[][] raw) {
		return new Array2DRowRealMatrix(raw);
	}

	@Override
	public BenchmarkContestant<RealMatrix>.EigenDecomposer getEigenDecomposer() {
		return new EigenDecomposer() {

			@Override
			public RealMatrix apply(final RealMatrix matrix) {

				final EigenDecomposition tmpEvD = new EigenDecomposition(matrix);

				return tmpEvD.getV();
			}
		};
	}

	@Override
	public BenchmarkContestant<RealMatrix>.GeneralSolver getGeneralSolver() {
		return new GeneralSolver() {

			@Override
			public RealMatrix apply(final RealMatrix body, final RealMatrix rhs) {

				final LUDecomposition tmpLU = new LUDecomposition(body);

				return tmpLU.getSolver().solve(rhs);
			}
		};
	}

	@Override
	public BenchmarkContestant<RealMatrix>.HermitianSolver getHermitianSolver() {
		return new HermitianSolver() {

			@Override
			public RealMatrix apply(final RealMatrix body, final RealMatrix rhs) {

				final CholeskyDecomposition tmpCholesky = new CholeskyDecomposition(body);

				return tmpCholesky.getSolver().solve(rhs);
			}

		};
	}

	@Override
	public BenchmarkContestant<RealMatrix>.LeastSquaresSolver getLeastSquaresSolver() {
		return new LeastSquaresSolver() {

			@Override
			public RealMatrix apply(final RealMatrix body, final RealMatrix rhs) {

				final QRDecomposition tmpQR = new QRDecomposition(body);

				return tmpQR.getSolver().solve(rhs);
			}

		};
	}

	@Override
	public BenchmarkContestant<RealMatrix>.MatrixBuilder getMatrixBuilder(final int numberOfRows,
			final int numberOfColumns) {
		return new MatrixBuilder() {

			private final Array2DRowRealMatrix myMatrix = new Array2DRowRealMatrix(numberOfRows, numberOfColumns);

			public RealMatrix get() {
				return myMatrix;
			}

			@Override
			public void set(final int row, final int col, final double value) {
				myMatrix.setEntry(row, col, value);
			}

		};
	}

	@Override
	public BenchmarkContestant<RealMatrix>.MatrixMultiplier getMatrixMultiplier() {
		return new MatrixMultiplier() {

			@Override
			public RealMatrix apply(final RealMatrix left, final RealMatrix right) {
				return left.multiply(right);
			}

		};
	}

	@Override
	public BenchmarkContestant<RealMatrix>.SingularDecomposer getSingularDecomposer() {
		return new SingularDecomposer() {

			@Override
			public RealMatrix apply(final RealMatrix matrix) {

				final SingularValueDecomposition svd = new SingularValueDecomposition(matrix);

				final RealMatrix U = svd.getU();
				final RealMatrix S = svd.getS();
				final RealMatrix V = svd.getV();

				return svd.getS();
			}

		};
	}

	@Override
	public BenchmarkContestant<RealMatrix>.TransposedMultiplier getTransposedMultiplier() {
		return new TransposedMultiplier() {

			@Override
			public RealMatrix apply(final RealMatrix left, final RealMatrix right) {
				return left.multiply(right.transpose());
			}

		};
	}

}
