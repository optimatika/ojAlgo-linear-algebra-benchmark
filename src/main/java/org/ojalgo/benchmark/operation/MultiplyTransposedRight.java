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
 * Matrix multiplication with the right factor transposed: [C] = [A][B]<sup>T</sup>.
 * <dl>
 * <dt>Input</dt>
 * <dd>[A] and [B], dim x dim, random elements, uniform in [-1, 1).</dd>
 * <dt>Measured</dt>
 * <dd>Multiplying [A] and the transpose of [B] into the preallocated [C]. How the transpose is handled is up
 * to the library – any explicit transpose is part of what is measured.</dd>
 * <dt>Required</dt>
 * <dd>The product, checked by multiplying with a random vector: [A]([B]<sup>T</sup>[x]) = [C][x]
 * ({@code verifyProduct}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>"Mult c=a*b^T"</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class MultiplyTransposedRight extends MatrixBenchmarkOperation implements JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(MultiplyTransposedRight.class, SIZE);
    }

    private MutatingBinaryMatrixMatrixOperation<?, ?> myOperation;

    Object left;
    Object product;
    Object right;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(left, right, product);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.make(lib);

        myOperation = library.getOperationFillByMultiplying(false, true);

        left = MatrixBenchmarkOperation.makeRandom(dim, dim, library);
        right = MatrixBenchmarkOperation.makeRandom(dim, dim, library);
        product = MatrixBenchmarkOperation.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifyProduct(left, false, right, true, product);
    }

}
