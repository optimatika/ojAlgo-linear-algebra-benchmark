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
 * The determinant, det([A]), of a general square matrix.
 * <dl>
 * <dt>Input</dt>
 * <dd>[A], dim x dim, random elements, uniform in [-1, 1).</dd>
 * <dt>Measured</dt>
 * <dd>Calculating the determinant – typically an LU decomposition – from scratch every invocation.</dd>
 * <dt>Required</dt>
 * <dd>The same value as a plain Java LU decomposition with partial pivoting: the same sign, and the
 * logarithms of the absolute values within a tolerance scaled by dim<sup>2</sup>. A determinant that
 * overflows must be infinite, with the correct sign ({@code verifyDeterminant}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>"Determinant"</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class Determinant extends MatrixBenchmarkOperation implements JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(Determinant.class, SIZE);
    }

    private PropertyOperation<?, ?> myOperation;

    Object original;
    double result;

    @Override
    @Benchmark
    public Object execute() {
        return result = myOperation.execute(original);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.make(lib);

        myOperation = library.getOperationDeterminant(dim);

        original = MatrixBenchmarkOperation.makeRandom(dim, dim, library);
        result = 0.0D;
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifyDeterminant(original, result);
    }

}
