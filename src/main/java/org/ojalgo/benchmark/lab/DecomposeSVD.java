/*
 * Copyright 1997-2017 Optimatika (www.optimatika.se)
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
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Mac Pro: 2018-03-04
 * 
 * <pre>
 * </pre>
 *
 * MacBook Pro: 2018-03-04
 *
 * <pre>
# Run complete. Total time: 00:24:47

Benchmark             (dim)   (lib)   Mode  Cnt        Score        Error    Units
DecomposeSVD.execute     10     ACM  thrpt    3  3360867.318 ± 365063.646  ops/min
DecomposeSVD.execute     10    EJML  thrpt    3  4901892.911 ± 522417.157  ops/min
DecomposeSVD.execute     10  ojAlgo  thrpt    3  4859845.832 ± 819330.955  ops/min
DecomposeSVD.execute     10     MTJ  thrpt    3  2468949.202 ± 704396.180  ops/min
DecomposeSVD.execute    100     ACM  thrpt    3     6415.992 ±    250.327  ops/min
DecomposeSVD.execute    100    EJML  thrpt    3    12451.286 ±   3286.352  ops/min
DecomposeSVD.execute    100  ojAlgo  thrpt    3    15896.994 ±   1121.752  ops/min
DecomposeSVD.execute    100     MTJ  thrpt    3    29052.226 ±   2799.026  ops/min
DecomposeSVD.execute   1000     ACM  thrpt    3        2.367 ±      1.888  ops/min
DecomposeSVD.execute   1000    EJML  thrpt    3       14.275 ±      0.701  ops/min
DecomposeSVD.execute   1000  ojAlgo  thrpt    3       16.303 ±      0.355  ops/min
DecomposeSVD.execute   1000     MTJ  thrpt    3      161.126 ±      3.737  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class DecomposeSVD extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(DecomposeSVD.class);
    }

    @Param({ "10", "100", "1000" })
    public int dim;
    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

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

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        matrix = this.makeRandom(dim, dim, library);

        myOperation = library.getOperationSVD(dim);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        if (!library.equals(matrix, dim, result)) {
            throw new BenchmarkRequirementsException("Not able to reconstruct the matrix!");
        }
    }

}
