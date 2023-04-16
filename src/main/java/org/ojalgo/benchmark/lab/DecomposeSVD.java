/*
 * Copyright 1997-2023 Optimatika (www.optimatika.se)
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
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * MacBook Pro (16-inch, 2019): 2022-01-04
 *
 * <pre>
Benchmark             (dim)   (lib)   Mode  Cnt        Score         Error    Units
DecomposeSVD.execute     10     ACM  thrpt    3  3857517.136 ±  855870.395  ops/min
DecomposeSVD.execute     10    EJML  thrpt    3  6011033.204 ±  929732.722  ops/min
DecomposeSVD.execute     10  ojAlgo  thrpt    3  5529471.295 ± 1055581.128  ops/min
DecomposeSVD.execute     10     MTJ  thrpt    3  2849179.185 ± 1913086.777  ops/min
DecomposeSVD.execute     16     ACM  thrpt    3  1233746.445 ±  168662.439  ops/min
DecomposeSVD.execute     16    EJML  thrpt    3  2074843.977 ±  223785.530  ops/min
DecomposeSVD.execute     16  ojAlgo  thrpt    3  1865694.974 ±  124178.338  ops/min
DecomposeSVD.execute     20     ACM  thrpt    3   741046.723 ±   74730.524  ops/min
DecomposeSVD.execute     20    EJML  thrpt    3  1212117.510 ±   92212.828  ops/min
DecomposeSVD.execute     20  ojAlgo  thrpt    3  1152934.101 ±  141244.724  ops/min
DecomposeSVD.execute     20     MTJ  thrpt    3   861957.168 ±  896274.078  ops/min
DecomposeSVD.execute     32     ACM  thrpt    3   218009.209 ±   18821.236  ops/min
DecomposeSVD.execute     32    EJML  thrpt    3   255609.136 ±  142900.668  ops/min
DecomposeSVD.execute     32  ojAlgo  thrpt    3   227307.553 ±    8793.711  ops/min
DecomposeSVD.execute     32     MTJ  thrpt    3   270610.938 ±   79597.464  ops/min
DecomposeSVD.execute     50     ACM  thrpt    3    41164.830 ±     447.662  ops/min
DecomposeSVD.execute     50    EJML  thrpt    3    62755.059 ±    2739.324  ops/min
DecomposeSVD.execute     50  ojAlgo  thrpt    3    86072.293 ±   34296.488  ops/min
DecomposeSVD.execute     50     MTJ  thrpt    3   102115.903 ±   45422.824  ops/min
DecomposeSVD.execute     64     ACM  thrpt    3    17247.072 ±     122.750  ops/min
DecomposeSVD.execute     64    EJML  thrpt    3    40351.828 ±   13351.029  ops/min
DecomposeSVD.execute     64  ojAlgo  thrpt    3    44752.283 ±    1987.386  ops/min
DecomposeSVD.execute     64     MTJ  thrpt    3    67100.507 ±    5260.366  ops/min
DecomposeSVD.execute    100     ACM  thrpt    3     5644.051 ±      70.602  ops/min
DecomposeSVD.execute    100    EJML  thrpt    3     9227.971 ±    2454.570  ops/min
DecomposeSVD.execute    100  ojAlgo  thrpt    3    12825.973 ±     261.156  ops/min
DecomposeSVD.execute    100     MTJ  thrpt    3    20806.921 ±     862.327  ops/min
DecomposeSVD.execute    128     ACM  thrpt    3     2752.501 ±     127.097  ops/min
DecomposeSVD.execute    128    EJML  thrpt    3     4920.399 ±     625.475  ops/min
DecomposeSVD.execute    128  ojAlgo  thrpt    3     8022.574 ±      94.567  ops/min
DecomposeSVD.execute    128     MTJ  thrpt    3    14451.857 ±     870.716  ops/min
DecomposeSVD.execute    200     ACM  thrpt    3      527.196 ±     439.926  ops/min
DecomposeSVD.execute    200    EJML  thrpt    3     1648.071 ±      17.635  ops/min
DecomposeSVD.execute    200  ojAlgo  thrpt    3     2088.163 ±     572.120  ops/min
DecomposeSVD.execute    200     MTJ  thrpt    3     3950.640 ±    4105.937  ops/min
DecomposeSVD.execute    256     ACM  thrpt    3      300.006 ±     100.670  ops/min
DecomposeSVD.execute    256    EJML  thrpt    3      713.482 ±     467.350  ops/min
DecomposeSVD.execute    256  ojAlgo  thrpt    3      979.355 ±     883.492  ops/min
DecomposeSVD.execute    256     MTJ  thrpt    3     2388.395 ±     932.052  ops/min
DecomposeSVD.execute    500     ACM  thrpt    3       31.830 ±       2.108  ops/min
DecomposeSVD.execute    500    EJML  thrpt    3      105.292 ±      54.217  ops/min
DecomposeSVD.execute    500  ojAlgo  thrpt    3      145.149 ±       1.379  ops/min
DecomposeSVD.execute    500     MTJ  thrpt    3      375.041 ±     318.788  ops/min
DecomposeSVD.execute    512     ACM  thrpt    3       30.540 ±      52.200  ops/min
DecomposeSVD.execute    512    EJML  thrpt    3       95.068 ±       0.957  ops/min
DecomposeSVD.execute    512  ojAlgo  thrpt    3      108.001 ±      28.220  ops/min
DecomposeSVD.execute    512     MTJ  thrpt    3      418.636 ±      29.220  ops/min
DecomposeSVD.execute   1000     ACM  thrpt    3        2.166 ±       1.581  ops/min
DecomposeSVD.execute   1000    EJML  thrpt    3       10.316 ±       3.738  ops/min
DecomposeSVD.execute   1000  ojAlgo  thrpt    3       19.224 ±       2.012  ops/min
DecomposeSVD.execute   1000     MTJ  thrpt    3      100.429 ±     121.225  ops/min
 * </pre>
 *
 * Mac Pro: 2018-03-04
 *
 * <pre>
# Run complete. Total time: 00:35:06

Benchmark             (dim)   (lib)   Mode  Cnt        Score        Error    Units
DecomposeSVD.execute     10     ACM  thrpt    3  1457705.262 ±   1523.294  ops/min
DecomposeSVD.execute     10    EJML  thrpt    3  2087096.374 ±  16126.801  ops/min
DecomposeSVD.execute     10  ojAlgo  thrpt    3  2367444.116 ± 180452.169  ops/min
DecomposeSVD.execute     10     MTJ  thrpt    3  1086278.734 ± 737026.114  ops/min
DecomposeSVD.execute    100     ACM  thrpt    3     2993.041 ±     78.715  ops/min
DecomposeSVD.execute    100    EJML  thrpt    3     6940.818 ±     24.389  ops/min
DecomposeSVD.execute    100  ojAlgo  thrpt    3     9529.632 ±     45.425  ops/min
DecomposeSVD.execute    100     MTJ  thrpt    3     9937.422 ±   1479.086  ops/min
DecomposeSVD.execute   1000     ACM  thrpt    3        0.792 ±      0.470  ops/min
DecomposeSVD.execute   1000    EJML  thrpt    3        8.079 ±      0.131  ops/min
DecomposeSVD.execute   1000  ojAlgo  thrpt    3       10.290 ±      0.178  ops/min
DecomposeSVD.execute   1000     MTJ  thrpt    3       64.108 ±      9.646  ops/min
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

    @Param({ "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000" })
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

        matrix = MatrixBenchmarkOperation.makeRandom(dim, dim, library);

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
