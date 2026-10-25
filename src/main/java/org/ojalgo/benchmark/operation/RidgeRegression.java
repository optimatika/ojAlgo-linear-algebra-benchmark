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
package org.ojalgo.benchmark.operation;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation;
import org.ojalgo.benchmark.suite.WorkloadSuite;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Ridge regression – regularised least squares: minimise |[X][&beta;] - [y]|<sup>2</sup> +
 * &lambda;|[&beta;]|<sup>2</sup>, by solving ([X]<sup>T</sup>[X] + &lambda;[I])[&beta;] = [X]<sup>T</sup>[y].
 * <dl>
 * <dt>Input</dt>
 * <dd>[X], (RATIO*dim) x dim (RATIO = 3, as in LeastSquaresSolver), and [y], (RATIO*dim) x 1, random elements,
 * uniform in [-1, 1). &lambda; = 1.</dd>
 * <dt>Measured</dt>
 * <dd>Solving for [&beta;] – typically forming [X]<sup>T</sup>[X] + &lambda;[I] and [X]<sup>T</sup>[y], then a
 * Cholesky decomposition and a solve – from scratch every invocation. Workspace for the intermediate results is
 * preallocated in setup.</dd>
 * <dt>Required</dt>
 * <dd>[X]<sup>T</sup>([X][&beta;] - [y]) + &lambda;[&beta;] = 0, within a tolerance scaled by RATIO*dim and the
 * norms ({@code verifyRidgeSolution}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>Not part of it.</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class RidgeRegression extends MatrixBenchmarkOperation implements WorkloadSuite {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(RidgeRegression.class, SIZE);
    }

    private static final double LAMBDA = 1.0;

    /**
     * The number of observations per variable.
     */
    private static final int RATIO = 3;

    private ProducingBinaryMatrixMatrixOperation<?, ?> myOperation;

    Object design;
    Object observations;
    Object result;

    @Override
    @Benchmark
    public Object execute() {
        return result = myOperation.execute(design, observations);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.make(lib);

        myOperation = library.getOperationRidgeRegression(RATIO * dim, dim, LAMBDA);

        design = MatrixBenchmarkOperation.makeRandom(RATIO * dim, dim, library);
        observations = MatrixBenchmarkOperation.makeRandom(RATIO * dim, 1, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifyRidgeSolution(design, observations, LAMBDA, result);
    }

}
