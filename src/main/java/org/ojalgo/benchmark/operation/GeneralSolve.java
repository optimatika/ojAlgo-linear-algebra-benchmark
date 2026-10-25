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
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Solving a square system of linear equations: [A][x] = [b].
 * <dl>
 * <dt>Input</dt>
 * <dd>[A], dim x dim, and [b], dim x 1, random elements, uniform in [-1, 1).</dd>
 * <dt>Measured</dt>
 * <dd>Solving for [x] – typically an LU decomposition and a solve – from scratch every invocation. Nothing
 * (no decomposition) may be reused from a previous invocation.</dd>
 * <dt>Required</dt>
 * <dd>A small residual: |[A][x] - [b]| within a tolerance scaled by dim and |[A]||[x]| + |[b]|
 * ({@code verifySolution}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>"Solve m=n"</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class GeneralSolve extends MatrixBenchmarkOperation implements JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(GeneralSolve.class, SIZE);
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

        myOperation = library.getOperationEquationSystemSolver(dim, dim, 1, false);

        body = MatrixBenchmarkOperation.makeRandom(dim, dim, library);
        rhs = MatrixBenchmarkOperation.makeRandom(dim, 1, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifySolution(body, rhs, result);
    }

}
