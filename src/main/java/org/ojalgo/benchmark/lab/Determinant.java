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
Benchmark            (dim)   (lib)   Mode  Cnt          Score          Error    Units
Determinant.execute     10     ACM  thrpt    3   59448502.268 ± 18276458.280  ops/min
Determinant.execute     10    EJML  thrpt    3   71092864.892 ± 26512805.281  ops/min
Determinant.execute     10  ojAlgo  thrpt    3  115831072.387 ± 23496574.110  ops/min
Determinant.execute     16     ACM  thrpt    3   24970477.503 ±  3810607.363  ops/min
Determinant.execute     16    EJML  thrpt    3   29072531.002 ±  3872482.040  ops/min
Determinant.execute     16  ojAlgo  thrpt    3   40981313.235 ± 10028697.735  ops/min
Determinant.execute     20     ACM  thrpt    3   14376511.428 ±  1080772.225  ops/min
Determinant.execute     20    EJML  thrpt    3   15727436.425 ±   684399.051  ops/min
Determinant.execute     20  ojAlgo  thrpt    3   16323832.068 ±   816225.752  ops/min
Determinant.execute     32     ACM  thrpt    3    4319940.257 ±   667065.364  ops/min
Determinant.execute     32    EJML  thrpt    3    5358833.772 ±   201420.756  ops/min
Determinant.execute     32  ojAlgo  thrpt    3    4287498.309 ±   540086.742  ops/min
Determinant.execute     50     ACM  thrpt    3    1185194.819 ±   311677.193  ops/min
Determinant.execute     50    EJML  thrpt    3    1563944.179 ±   608307.411  ops/min
Determinant.execute     50  ojAlgo  thrpt    3    2606775.242 ± 15704325.422  ops/min
Determinant.execute     64     ACM  thrpt    3     644959.138 ±    57009.327  ops/min
Determinant.execute     64    EJML  thrpt    3     799045.139 ±   229610.824  ops/min
Determinant.execute     64  ojAlgo  thrpt    3     980697.262 ±   295534.644  ops/min
Determinant.execute    100     ACM  thrpt    3     161903.867 ±    40797.027  ops/min
Determinant.execute    100    EJML  thrpt    3     219586.259 ±    15302.638  ops/min
Determinant.execute    100  ojAlgo  thrpt    3     362126.879 ±    98214.981  ops/min
Determinant.execute    128     ACM  thrpt    3      82698.768 ±    15603.781  ops/min
Determinant.execute    128    EJML  thrpt    3     103348.317 ±     8269.080  ops/min
Determinant.execute    128  ojAlgo  thrpt    3     191991.391 ±    51295.454  ops/min
Determinant.execute    200     ACM  thrpt    3      20620.412 ±      715.289  ops/min
Determinant.execute    200    EJML  thrpt    3      26241.673 ±     2666.113  ops/min
Determinant.execute    200  ojAlgo  thrpt    3      40801.309 ±    19580.018  ops/min
Determinant.execute    256     ACM  thrpt    3       6727.113 ±     2428.190  ops/min
Determinant.execute    256    EJML  thrpt    3       8195.695 ±      226.706  ops/min
Determinant.execute    256  ojAlgo  thrpt    3      17089.356 ±     7724.278  ops/min
Determinant.execute    500     ACM  thrpt    3        634.353 ±      375.477  ops/min
Determinant.execute    500    EJML  thrpt    3       1026.240 ±       64.314  ops/min
Determinant.execute    500  ojAlgo  thrpt    3       2398.878 ±     2256.362  ops/min
Determinant.execute    512     ACM  thrpt    3        526.614 ±      489.208  ops/min
Determinant.execute    512    EJML  thrpt    3        911.238 ±      158.244  ops/min
Determinant.execute    512  ojAlgo  thrpt    3       2198.508 ±     1647.737  ops/min
Determinant.execute   1000     ACM  thrpt    3         70.462 ±       64.909  ops/min
Determinant.execute   1000    EJML  thrpt    3        128.947 ±        7.241  ops/min
Determinant.execute   1000  ojAlgo  thrpt    3        217.377 ±       11.295  ops/min
 * </pre>
 *
 * <pre>
# Run complete. Total time: 00:15:30

Benchmark            (dim)   (lib)   Mode  Cnt         Score        Error    Units
Determinant.execute     10     ACM  thrpt    3  25782802.727 ±  64675.877  ops/min
Determinant.execute     10    EJML  thrpt    3  27278386.525 ± 584668.666  ops/min
Determinant.execute     10  ojAlgo  thrpt    3  33458646.538 ±  26591.136  ops/min
Determinant.execute    100     ACM  thrpt    3     83695.835 ±    305.917  ops/min
Determinant.execute    100    EJML  thrpt    3    111320.928 ±    291.818  ops/min
Determinant.execute    100  ojAlgo  thrpt    3    128324.306 ±  22547.848  ops/min
Determinant.execute   1000     ACM  thrpt    3        15.473 ±      1.013  ops/min
Determinant.execute   1000    EJML  thrpt    3       132.956 ±      0.718  ops/min
Determinant.execute   1000  ojAlgo  thrpt    3       220.580 ±      2.028  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class Determinant extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(Determinant.class);
    }

    @Param({ "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000" })
    public int dim;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

    private PropertyOperation<?, ?> myOperation;

    Object original;
    Object result;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(original);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        myOperation = library.getOperationDeterminant(dim);

        original = MatrixBenchmarkOperation.makeRandom(dim, dim, library);
        result = 0.0D;
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
