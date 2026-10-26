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
 * Solving a symmetric indefinite system of linear equations – the KKT system of an equality constrained
 * quadratic program: [H A<sup>T</sup>; A 0][x; y] = [c; d].
 * <dl>
 * <dt>Input</dt>
 * <dd>The body, dim x dim, with m = dim/3 constraints and n = dim - m variables: [H], n x n, symmetric
 * positive definite, [a][a]<sup>T</sup> + [I] with [a] uniform in [-1, 1), and [A], m x n, uniform in [-1,
 * 1). The right hand side, dim x 1, uniform in [-1, 1). (Below dim = 3 there are no constraints – the body is
 * [H].)</dd>
 * <dt>Measured</dt>
 * <dd>Solving for [x; y], from scratch every invocation. The library may exploit that the body is symmetric
 * (LDL<sup>T</sup> with symmetric pivoting), but not its KKT structure. A library that has no symmetric
 * indefinite solver uses a general one (LU). Nothing may be reused from a previous invocation.</dd>
 * <dt>Required</dt>
 * <dd>A small residual: |[K][s] - [r]| within a tolerance scaled by dim and |[K]||[s]| + |[r]|
 * ({@code verifySolution}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>Not part of it.</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class SymmetricIndefiniteSolve extends MatrixBenchmarkOperation implements WorkloadSuite {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(SymmetricIndefiniteSolve.class, SIZE);
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

        myOperation = library.getOperationSymmetricIndefiniteSolver(dim);

        int nbConstraints = dim / 3;

        body = MatrixBenchmarkOperation.makeKKT(dim - nbConstraints, nbConstraints, library);
        rhs = MatrixBenchmarkOperation.makeRandom(dim, 1, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifySolution(body, rhs, result);
    }

}
