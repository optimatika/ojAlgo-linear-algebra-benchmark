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
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * LU decomposition, with row pivoting: [P][A] = [L][U].
 * <dl>
 * <dt>Input</dt>
 * <dd>[A], dim x dim, random elements, uniform in [-1, 1).</dd>
 * <dt>Measured</dt>
 * <dd>Decomposing [A] and extracting the permutation [P], [L] and [U], all dim x dim, from scratch every
 * invocation.</dd>
 * <dt>Required</dt>
 * <dd>[P] a permutation matrix, [L] lower and [U] upper triangular, and [P][A] = [L][U] – checked by
 * multiplying with a random vector ({@code verifyLUDecomposition}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>Not part of it.</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class DecomposeLU extends MatrixBenchmarkOperation implements DecompositionSuite {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(DecomposeLU.class, SIZE);
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

        matrix = MatrixBenchmarkOperation.makeRandom(dim, dim, library);

        myOperation = library.getOperationLU(dim);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        if (!(result instanceof Object[] factors)) {
            throw new BenchmarkRequirementsException("Decomposition is missing!");
        }
        this.verifyLUDecomposition(matrix, factors[0], factors[1], factors[2]);
    }

}
