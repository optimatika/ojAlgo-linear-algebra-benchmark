/*
 * Copyright 1997-2020 Optimatika (www.optimatika.se)
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
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Mac Pro 2017-04-21
 *
 * <pre>
# Run complete. Total time: 00:21:51

Benchmark                  (dim)  (library)   Mode  Cnt         Score          Error    Units
DecomposeSingular.execute      2        ACM  thrpt    3  32536808.095 ±   956505.746  ops/min
DecomposeSingular.execute      2       EJML  thrpt    3  32608989.421 ±  1265476.515  ops/min
DecomposeSingular.execute      2        MTJ  thrpt    3   8185640.906 ±   450504.968  ops/min
DecomposeSingular.execute      2     ojAlgo  thrpt    3  56878655.707 ± 16575162.064  ops/min
DecomposeSingular.execute      3        ACM  thrpt    3  16511210.388 ±   873734.016  ops/min
DecomposeSingular.execute      3       EJML  thrpt    3  15954667.993 ±  2605948.444  ops/min
DecomposeSingular.execute      3        MTJ  thrpt    3   5281430.725 ±   938832.777  ops/min
DecomposeSingular.execute      3     ojAlgo  thrpt    3  24102168.669 ±   725407.419  ops/min
DecomposeSingular.execute      4        ACM  thrpt    3  10873883.155 ±  2167114.080  ops/min
DecomposeSingular.execute      4       EJML  thrpt    3   9494306.299 ±  1086568.700  ops/min
DecomposeSingular.execute      4        MTJ  thrpt    3   3867724.471 ±   251605.645  ops/min
DecomposeSingular.execute      4     ojAlgo  thrpt    3  16583014.463 ±  2820702.851  ops/min
DecomposeSingular.execute      5        ACM  thrpt    3   6388455.366 ±  6667323.813  ops/min
DecomposeSingular.execute      5       EJML  thrpt    3   6695890.929 ±   748042.628  ops/min
DecomposeSingular.execute      5        MTJ  thrpt    3   2703901.755 ±   395928.417  ops/min
DecomposeSingular.execute      5     ojAlgo  thrpt    3   9082625.658 ±  1268306.599  ops/min
DecomposeSingular.execute     10        ACM  thrpt    3   1490945.305 ±   466222.669  ops/min
DecomposeSingular.execute     10       EJML  thrpt    3   1745460.998 ±   397576.742  ops/min
DecomposeSingular.execute     10        MTJ  thrpt    3    931817.959 ±    71602.716  ops/min
DecomposeSingular.execute     10     ojAlgo  thrpt    3   2232419.646 ±   108564.813  ops/min
DecomposeSingular.execute     20        ACM  thrpt    3    278390.966 ±    17721.526  ops/min
DecomposeSingular.execute     20       EJML  thrpt    3    406774.722 ±    28390.913  ops/min
DecomposeSingular.execute     20        MTJ  thrpt    3    238938.013 ±     9051.579  ops/min
DecomposeSingular.execute     20     ojAlgo  thrpt    3    494423.344 ±    62990.838  ops/min
DecomposeSingular.execute     50        ACM  thrpt    3     21713.323 ±      522.904  ops/min
DecomposeSingular.execute     50       EJML  thrpt    3     40313.745 ±     1992.415  ops/min
DecomposeSingular.execute     50        MTJ  thrpt    3     41603.509 ±     3185.403  ops/min
DecomposeSingular.execute     50     ojAlgo  thrpt    3     53313.774 ±     8427.499  ops/min
DecomposeSingular.execute    100        ACM  thrpt    3      2497.800 ±       47.005  ops/min
DecomposeSingular.execute    100       EJML  thrpt    3      6324.495 ±      876.695  ops/min
DecomposeSingular.execute    100        MTJ  thrpt    3      9285.069 ±     1170.040  ops/min
DecomposeSingular.execute    100     ojAlgo  thrpt    3      8325.952 ±     1068.931  ops/min
DecomposeSingular.execute    200        ACM  thrpt    3       245.167 ±       30.861  ops/min
DecomposeSingular.execute    200       EJML  thrpt    3       927.351 ±      103.558  ops/min
DecomposeSingular.execute    200        MTJ  thrpt    3      2144.868 ±      454.645  ops/min
DecomposeSingular.execute    200     ojAlgo  thrpt    3      1170.619 ±      227.509  ops/min
DecomposeSingular.execute    500        ACM  thrpt    3        10.084 ±        1.253  ops/min
DecomposeSingular.execute    500       EJML  thrpt    3        62.114 ±        6.340  ops/min
DecomposeSingular.execute    500        MTJ  thrpt    3       283.685 ±        3.724  ops/min
DecomposeSingular.execute    500     ojAlgo  thrpt    3        77.293 ±        1.305  ops/min
DecomposeSingular.execute   1000        ACM  thrpt    3         0.621 ±        0.559  ops/min
DecomposeSingular.execute   1000       EJML  thrpt    3         7.984 ±        0.703  ops/min
DecomposeSingular.execute   1000        MTJ  thrpt    3        59.693 ±       23.157  ops/min
DecomposeSingular.execute   1000     ojAlgo  thrpt    3         9.032 ±        1.908  ops/min
 * </pre>
 *
 * MacBook Air: 2015-06-20
 *
 * <pre>
 * # Run complete. Total time: 00:26:41
 *
 * Benchmark                  (dim)  (library)   Mode  Cnt         Score          Error    Units
 * DecomposeSingular.execute      2       EJML  thrpt    3  32502428,926 ±  4710154,863  ops/min
 * DecomposeSingular.execute      2        MTJ  thrpt    3   7964881,789 ±  1581675,018  ops/min
 * DecomposeSingular.execute      2     ojAlgo  thrpt    3  40632099,642 ±  4731766,669  ops/min
 * DecomposeSingular.execute      3       EJML  thrpt    3  18583328,283 ± 18511434,596  ops/min
 * DecomposeSingular.execute      3        MTJ  thrpt    3   5482691,535 ±  1977588,607  ops/min
 * DecomposeSingular.execute      3     ojAlgo  thrpt    3  23001928,199 ±  8673210,399  ops/min
 * DecomposeSingular.execute      4       EJML  thrpt    3   9510607,672 ±  1973535,683  ops/min
 * DecomposeSingular.execute      4        MTJ  thrpt    3   4100405,623 ±   925861,106  ops/min
 * DecomposeSingular.execute      4     ojAlgo  thrpt    3  12446049,514 ±  2540121,213  ops/min
 * DecomposeSingular.execute      5       EJML  thrpt    3   7099406,961 ±  1889125,480  ops/min
 * DecomposeSingular.execute      5        MTJ  thrpt    3   3180970,931 ±   944004,486  ops/min
 * DecomposeSingular.execute      5     ojAlgo  thrpt    3   8234663,669 ±  4715953,484  ops/min
 * DecomposeSingular.execute     10       EJML  thrpt    3   1902966,310 ±   796984,788  ops/min
 * DecomposeSingular.execute     10        MTJ  thrpt    3   1256697,542 ±   254570,961  ops/min
 * DecomposeSingular.execute     10     ojAlgo  thrpt    3   2653376,089 ±   773179,735  ops/min
 * DecomposeSingular.execute     20       EJML  thrpt    3    389635,172 ±   146403,387  ops/min
 * DecomposeSingular.execute     20        MTJ  thrpt    3    324442,995 ±   121651,374  ops/min
 * DecomposeSingular.execute     20     ojAlgo  thrpt    3    506706,847 ±    69666,709  ops/min
 * DecomposeSingular.execute     50       EJML  thrpt    3     44856,020 ±    13499,083  ops/min
 * DecomposeSingular.execute     50        MTJ  thrpt    3     47430,003 ±    33581,105  ops/min
 * DecomposeSingular.execute     50     ojAlgo  thrpt    3     61380,257 ±    22364,807  ops/min
 * DecomposeSingular.execute    100       EJML  thrpt    3      7556,505 ±     4079,901  ops/min
 * DecomposeSingular.execute    100        MTJ  thrpt    3     11872,533 ±      649,675  ops/min
 * DecomposeSingular.execute    100     ojAlgo  thrpt    3      9283,170 ±     2071,847  ops/min
 * DecomposeSingular.execute    200       EJML  thrpt    3      1033,044 ±      582,972  ops/min
 * DecomposeSingular.execute    200        MTJ  thrpt    3      2579,772 ±      889,169  ops/min
 * DecomposeSingular.execute    200     ojAlgo  thrpt    3      1265,597 ±     1633,121  ops/min
 * DecomposeSingular.execute    500       EJML  thrpt    3        68,040 ±       66,023  ops/min
 * DecomposeSingular.execute    500        MTJ  thrpt    3       290,811 ±      260,552  ops/min
 * DecomposeSingular.execute    500     ojAlgo  thrpt    3        86,145 ±       16,555  ops/min
 * DecomposeSingular.execute   1000       EJML  thrpt    3         8,217 ±        1,077  ops/min
 * DecomposeSingular.execute   1000        MTJ  thrpt    3        42,715 ±       18,701  ops/min
 * DecomposeSingular.execute   1000     ojAlgo  thrpt    3         5,587 ±        2,089  ops/min
 * DecomposeSingular.execute   2000       EJML  thrpt    3         1,043 ±        0,119  ops/min
 * DecomposeSingular.execute   2000        MTJ  thrpt    3         7,106 ±        4,904  ops/min
 * DecomposeSingular.execute   2000     ojAlgo  thrpt    3         0,744 ±        0,099  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class Pseudoinverse extends MatrixBenchmarkOperation {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(Pseudoinverse.class);
    }

    @Param({ "10", "100", "1000" })
    public int dim;
    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

    private ProducingUnaryMatrixOperation<?, ?> myOperation;

    Object matrix;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(matrix);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        matrix = this.makeSPD(dim, library);

        myOperation = library.getOperationPseudoinverse(dim);
    }

    @Override
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myDecomposer.getClass());

    }

}
