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
 * The eigenvalues, only, of a symmetric matrix.
 * <dl>
 * <dt>Input</dt>
 * <dd>[A], dim x dim, symmetric but indefinite, random elements uniform in [-1, 1) mirrored – as in
 * DecomposeEvD.</dd>
 * <dt>Measured</dt>
 * <dd>Calculating the eigenvalues, dim x 1 in any order, from scratch every invocation. The eigenvectors are
 * not needed – a library that can't avoid calculating them, does so. The library may exploit that [A] is
 * symmetric.</dd>
 * <dt>Required</dt>
 * <dd>Their sum is the trace of [A], the sum of their squares is the square of the Frobenius norm, and their
 * product is the determinant ({@code verifyEigenvalues}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>Not part of it.</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class SymmetricEigenvalues extends MatrixBenchmarkOperation implements WorkloadSuite {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(SymmetricEigenvalues.class, SIZE);
    }

    private ProducingUnaryMatrixOperation<?, ?> myOperation;

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

        myOperation = library.getOperationEigenvalues(dim);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        this.verifyEigenvalues(matrix, result);
    }

}
