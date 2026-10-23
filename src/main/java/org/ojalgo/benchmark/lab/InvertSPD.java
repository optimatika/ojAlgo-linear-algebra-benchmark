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
package org.ojalgo.benchmark.lab;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.BenchmarkSuite;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * The inverse, [A]<sup>-1</sup>, of a symmetric positive definite matrix.
 * <dl>
 * <dt>Input</dt>
 * <dd>[A], dim x dim, symmetric positive definite, [a][a]<sup>T</sup> + [I] with [a] uniform in [-1, 1) (the Java Matrix Benchmark used [0, 1)).</dd>
 * <dt>Measured</dt>
 * <dd>Inverting [A] into the preallocated result, from scratch every invocation. The library may exploit that [A] is symmetric positive definite (Cholesky), but must deliver the full inverse – not only a triangle of it.</dd>
 * <dt>Required</dt>
 * <dd>[A]([A]<sup>-1</sup>[x]) = [x] for a random vector [x] ({@code verifyInverse}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>"Invert Symm b=inv(a)"</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class InvertSPD extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(InvertSPD.class, SIZE);
    }

    private MutatingUnaryMatrixOperation<?, ?> myOperation;

    Object original;
    Object result;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(original, result);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.make(lib);

        myOperation = library.getOperationInvert(dim, true);

        original = MatrixBenchmarkOperation.makeSPD(dim, library);
        result = MatrixBenchmarkOperation.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifyInverse(original, result);
    }

}
