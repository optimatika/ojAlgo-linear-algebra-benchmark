/*
 * Copyright 1997-2018 Optimatika
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
package org.ojalgo.benchmark;

public interface BenchmarkSuite {

    /**
     * A suite corresponding to the classical Java Matrix Benchmark set of tests.
     * <ul>
     * <li>add</li>
     * <li>scale</li>
     * <li>transpose</li>
     * <li>multiply</li>
     * <li>multiply (transp)</li>
     * <li>invert</li>
     * <li>invert SPD</li>
     * <li>Determinant</li>
     * <li>Solve</li>
     * <li>Solve Least Squares</li>
     * <li>SVD</li>
     * <li>EvD</li>
     * </ul>
     * https://lessthanoptimal.github.io/Java-Matrix-Benchmark/
     * https://github.com/lessthanoptimal/Java-Matrix-Benchmark
     *
     * @author apete
     */
    public interface JavaMatrixBenchmark extends BenchmarkSuite {

    }

    public interface MatrixDecomposition extends BenchmarkSuite {

    }

}
