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
 * Solving with a decomposition that already exists: [A][x] = [b], with [A] decomposed in setup.
 * <dl>
 * <dt>Input</dt>
 * <dd>[A], dim x dim, and [b], dim x 1, random elements, uniform in [-1, 1).</dd>
 * <dt>Measured</dt>
 * <dd>Solving for [x] using the decomposition of [A] – typically LU – that was calculated in setup, and is not
 * measured. Unlike in the other benchmarks, every invocation reuses that decomposition – that is what is
 * measured: the substitutions repeated by e.g. Newton methods with a fixed Jacobian, or implicit time stepping,
 * instead of multiplying with an inverse.</dd>
 * <dt>Required</dt>
 * <dd>A small residual: |[A][x] - [b]| within a tolerance scaled by dim and |[A]||[x]| + |[b]|
 * ({@code verifySolution}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>Not part of it. What an inverse is typically calculated for (compare InvertGeneral), without calculating
 * it.</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class RepeatedSolve extends MatrixBenchmarkOperation implements WorkloadSuite {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(RepeatedSolve.class, SIZE);
    }

    private ProducingUnaryMatrixOperation<?, ?> myOperation;

    Object body;
    Object result;
    Object rhs;

    @Override
    @Benchmark
    public Object execute() {
        return result = myOperation.execute(rhs);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.make(lib);

        body = MatrixBenchmarkOperation.makeRandom(dim, dim, library);
        rhs = MatrixBenchmarkOperation.makeRandom(dim, 1, library);

        myOperation = library.getOperationRepeatedSolver(dim).execute(body);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifySolution(body, rhs, result);
    }

}
