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
import org.ojalgo.benchmark.suite.JavaMatrixBenchmark;
import org.ojalgo.benchmark.suite.WorkloadSuite;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Solving an overdetermined system of linear equations in the least squares sense: minimise |[A][x] - [b]|.
 * <dl>
 * <dt>Input</dt>
 * <dd>[A], (RATIO*dim) x dim (RATIO = 3, as in the Java Matrix Benchmark), and [b], (RATIO*dim) x 1, random
 * elements, uniform in [-1, 1). As [b] is random there is no exact solution – a true least squares problem
 * (the Java Matrix Benchmark used [b] = [A][x]).</dd>
 * <dt>Measured</dt>
 * <dd>Solving for [x] – typically a QR decomposition and a solve – from scratch every invocation. Nothing may
 * be reused from a previous invocation.</dd>
 * <dt>Required</dt>
 * <dd>The residual orthogonal to the columns of [A]: |[A]<sup>T</sup>([A][x] - [b])| within a tolerance
 * scaled by RATIO*dim and the norms ({@code verifySolution}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>"Solve m>n"</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class LeastSquaresSolver extends MatrixBenchmarkOperation implements JavaMatrixBenchmark, WorkloadSuite {

    /**
     * The number of equations per variable – the body is (RATIO*dim)x(dim). The Java Matrix Benchmark uses 3.
     */
    private static final int RATIO = 3;

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(LeastSquaresSolver.class, SIZE);
    }

    private ProducingBinaryMatrixMatrixOperation<?, ?> myOperation;

    Object body;
    Object result;
    Object rhs;

    @Override
    @Benchmark
    public Object execute() {
        return result = myOperation.execute(body, rhs);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.make(lib);

        myOperation = library.getOperationEquationSystemSolver(RATIO * dim, dim, 1, false);

        body = MatrixBenchmarkOperation.makeRandom(RATIO * dim, dim, library);
        rhs = MatrixBenchmarkOperation.makeRandom(RATIO * dim, 1, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifySolution(body, rhs, result);
    }

}
