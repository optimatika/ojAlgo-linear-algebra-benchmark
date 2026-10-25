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
import org.ojalgo.benchmark.suite.DecompositionSuite;
import org.ojalgo.benchmark.suite.JavaMatrixBenchmark;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Eigenvalue decomposition of a symmetric matrix: [A] = [V][D][V]<sup>T</sup>.
 * <dl>
 * <dt>Input</dt>
 * <dd>[A], dim x dim, symmetric but indefinite, random elements uniform in [-1, 1) mirrored.</dd>
 * <dt>Measured</dt>
 * <dd>Decomposing [A] and extracting the eigenvectors [V] and the (diagonal) eigenvalues [D], both dim x dim,
 * from scratch every invocation. The library may exploit that [A] is symmetric.</dd>
 * <dt>Required</dt>
 * <dd>[A][V] = [V][D] and [V]<sup>T</sup>[V] = [I], both checked by multiplying with a random vector
 * ({@code verifyEigenpairs}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>"Eigen for Symm Matrices"</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class DecomposeEvD extends MatrixBenchmarkOperation implements JavaMatrixBenchmark, DecompositionSuite {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(DecomposeEvD.class, SIZE);
    }

    private DecompositionOperation<?, ?> myOperation;

    Object matrix;
    Object result;

    @Override
    @Benchmark
    public Object execute() {
        return result = myOperation.execute(matrix);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.make(lib);

        matrix = MatrixBenchmarkOperation.makeSymmetric(dim, library);

        myOperation = library.getOperationEvD(dim);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        if (!(result instanceof Object[] factors)) {
            throw new BenchmarkRequirementsException("Decomposition is missing!");
        }
        this.verifyEigenpairs(matrix, factors[0], factors[1]);
    }

}
