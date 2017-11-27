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
package org.ojalgo.benchmark.matrix.operation;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.matrix.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.matrix.MatrixBenchmarkOperation;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * <h1>Mac Pro (Early 2009)</h1>
 * <h2>2017-11-27</h2>
 *
 * <pre>
Result "org.ojalgo.benchmark.matrix.suite.FillByMultiplying.execute":
  1705,970 ±(99.9%) 1661,444 ops/min [Average]
  (min, avg, max) = (1601,549, 1705,970, 1768,938), stdev = 91,069
  CI (99.9%): [44,526, 3367,414] (assumes normal distribution)


# Run complete. Total time: 00:16:41

Benchmark                  (dim)  (library)   Mode  Cnt           Score          Error    Units
FillByMultiplying.execute      1        ACM  thrpt    3   133509876,393 ± 11416322,389  ops/min
FillByMultiplying.execute      1       EJML  thrpt    3  3973998391,089 ± 56628908,712  ops/min
FillByMultiplying.execute      1     ojAlgo  thrpt    3  2335453142,903 ± 11270791,836  ops/min
FillByMultiplying.execute      1        MTJ  thrpt    3    58619282,070 ±  9016646,201  ops/min
FillByMultiplying.execute      2        ACM  thrpt    3    82091571,157 ±  9912227,323  ops/min
FillByMultiplying.execute      2       EJML  thrpt    3  1397927321,639 ± 13027030,155  ops/min
FillByMultiplying.execute      2     ojAlgo  thrpt    3  2189453160,090 ± 25126812,861  ops/min
FillByMultiplying.execute      2        MTJ  thrpt    3    54292162,644 ± 25429554,710  ops/min
FillByMultiplying.execute      3        ACM  thrpt    3    59293729,289 ±  5521067,113  ops/min
FillByMultiplying.execute      3       EJML  thrpt    3   619381670,038 ±  3534161,085  ops/min
FillByMultiplying.execute      3     ojAlgo  thrpt    3  1262196535,636 ±  5668832,891  ops/min
FillByMultiplying.execute      3        MTJ  thrpt    3    53248273,717 ± 21962096,949  ops/min
FillByMultiplying.execute      4        ACM  thrpt    3    44857961,834 ± 17684802,887  ops/min
FillByMultiplying.execute      4       EJML  thrpt    3   285050407,384 ±  3198931,507  ops/min
FillByMultiplying.execute      4     ojAlgo  thrpt    3   714832013,074 ± 42423339,132  ops/min
FillByMultiplying.execute      4        MTJ  thrpt    3    54005662,275 ± 16200949,931  ops/min
FillByMultiplying.execute      5        ACM  thrpt    3    34212932,590 ±   793359,330  ops/min
FillByMultiplying.execute      5       EJML  thrpt    3   197697522,702 ± 19329928,027  ops/min
FillByMultiplying.execute      5     ojAlgo  thrpt    3   429006864,713 ±  1956560,060  ops/min
FillByMultiplying.execute      5        MTJ  thrpt    3    49072779,752 ± 19717279,955  ops/min
FillByMultiplying.execute      8        ACM  thrpt    3    17674052,541 ±   287104,069  ops/min
FillByMultiplying.execute      8       EJML  thrpt    3    60466427,142 ± 18110004,860  ops/min
FillByMultiplying.execute      8     ojAlgo  thrpt    3   100155300,735 ±  1650859,694  ops/min
FillByMultiplying.execute      8        MTJ  thrpt    3    46003966,819 ± 10814171,351  ops/min
FillByMultiplying.execute     10        ACM  thrpt    3    12384643,884 ±   218927,248  ops/min
FillByMultiplying.execute     10       EJML  thrpt    3    34675525,519 ±  1737458,173  ops/min
FillByMultiplying.execute     10     ojAlgo  thrpt    3    52263689,961 ±   679568,437  ops/min
FillByMultiplying.execute     10        MTJ  thrpt    3    38972677,430 ±  4627255,276  ops/min
FillByMultiplying.execute     16        ACM  thrpt    3     4920984,968 ±    50625,889  ops/min
FillByMultiplying.execute     16       EJML  thrpt    3     6388069,320 ±    87738,426  ops/min
FillByMultiplying.execute     16     ojAlgo  thrpt    3     7339705,297 ±   916288,582  ops/min
FillByMultiplying.execute     16        MTJ  thrpt    3    14930494,711 ±  2071844,130  ops/min
FillByMultiplying.execute     20        ACM  thrpt    3     2872316,671 ±  1198022,066  ops/min
FillByMultiplying.execute     20       EJML  thrpt    3     4991384,834 ±    68635,816  ops/min
FillByMultiplying.execute     20     ojAlgo  thrpt    3     5337023,103 ±   884462,268  ops/min
FillByMultiplying.execute     20        MTJ  thrpt    3    11639719,403 ±  1765750,350  ops/min
FillByMultiplying.execute     32        ACM  thrpt    3      877934,913 ±    22912,642  ops/min
FillByMultiplying.execute     32       EJML  thrpt    3      941178,299 ±     6854,662  ops/min
FillByMultiplying.execute     32     ojAlgo  thrpt    3     1571106,836 ±     9231,194  ops/min
FillByMultiplying.execute     32        MTJ  thrpt    3     5079786,383 ±    82100,431  ops/min
FillByMultiplying.execute     50        ACM  thrpt    3      258285,215 ±    24159,582  ops/min
FillByMultiplying.execute     50       EJML  thrpt    3      381063,824 ±  1149112,384  ops/min
FillByMultiplying.execute     50     ojAlgo  thrpt    3      586132,439 ±   421377,519  ops/min
FillByMultiplying.execute     50        MTJ  thrpt    3     1658285,710 ±    30088,761  ops/min
FillByMultiplying.execute     64        ACM  thrpt    3      124717,588 ±     5886,222  ops/min
FillByMultiplying.execute     64       EJML  thrpt    3      126349,373 ±     1392,042  ops/min
FillByMultiplying.execute     64     ojAlgo  thrpt    3      409154,245 ±     2393,577  ops/min
FillByMultiplying.execute     64        MTJ  thrpt    3      872841,200 ±    20942,697  ops/min
FillByMultiplying.execute    100        ACM  thrpt    3       30694,992 ±     2789,816  ops/min
FillByMultiplying.execute    100       EJML  thrpt    3       55743,930 ±     1248,767  ops/min
FillByMultiplying.execute    100     ojAlgo  thrpt    3      132455,310 ±    14064,157  ops/min
FillByMultiplying.execute    100        MTJ  thrpt    3      241666,397 ±      904,960  ops/min
FillByMultiplying.execute    128        ACM  thrpt    3       15560,081 ±     3922,615  ops/min
FillByMultiplying.execute    128       EJML  thrpt    3       27243,681 ±      275,621  ops/min
FillByMultiplying.execute    128     ojAlgo  thrpt    3       96741,033 ±    18075,344  ops/min
FillByMultiplying.execute    128        MTJ  thrpt    3      242589,084 ±    22161,818  ops/min
FillByMultiplying.execute    200        ACM  thrpt    3        2604,684 ±      306,563  ops/min
FillByMultiplying.execute    200       EJML  thrpt    3        6791,938 ±      176,233  ops/min
FillByMultiplying.execute    200     ojAlgo  thrpt    3       39135,537 ±     1922,100  ops/min
FillByMultiplying.execute    200        MTJ  thrpt    3      115530,796 ±      888,664  ops/min
FillByMultiplying.execute    256        ACM  thrpt    3        1051,977 ±       10,153  ops/min
FillByMultiplying.execute    256       EJML  thrpt    3        3429,855 ±       24,040  ops/min
FillByMultiplying.execute    256     ojAlgo  thrpt    3       20627,758 ±     1713,292  ops/min
FillByMultiplying.execute    256        MTJ  thrpt    3       76085,155 ±     2941,512  ops/min
FillByMultiplying.execute    500        ACM  thrpt    3          54,016 ±        2,738  ops/min
FillByMultiplying.execute    500       EJML  thrpt    3         454,829 ±        1,987  ops/min
FillByMultiplying.execute    500     ojAlgo  thrpt    3        2909,490 ±      319,740  ops/min
FillByMultiplying.execute    500        MTJ  thrpt    3       12198,915 ±     1078,807  ops/min
FillByMultiplying.execute    512        ACM  thrpt    3          44,473 ±        0,649  ops/min
FillByMultiplying.execute    512       EJML  thrpt    3         428,077 ±        0,664  ops/min
FillByMultiplying.execute    512     ojAlgo  thrpt    3        2669,563 ±     1415,115  ops/min
FillByMultiplying.execute    512        MTJ  thrpt    3       11456,405 ±      222,816  ops/min
FillByMultiplying.execute   1000        ACM  thrpt    3           3,268 ±        0,407  ops/min
FillByMultiplying.execute   1000       EJML  thrpt    3          48,963 ±        3,978  ops/min
FillByMultiplying.execute   1000     ojAlgo  thrpt    3         283,759 ±       33,892  ops/min
FillByMultiplying.execute   1000        MTJ  thrpt    3        1705,970 ±     1661,444  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class FillByMultiplying extends MatrixBenchmarkOperation {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(FillByMultiplying.class);
    }

    @Param({ "1", "2", "3", "4", "5", "10", "20", "50", "100", "200", "500", "1000",
            "2000" /* , "5000", "10000" */ })
    public int dim;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String library;

    private MutatingBinaryOperation<?, ?> myOperation;

    Object left;
    Object product;
    Object right;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(product, left, right);
    }

    @Setup
    public void setup() {

        contestant = MatrixBenchmarkLibrary.LIBRARIES.get(library);

        myOperation = contestant.getFillByMultiplyingOperation();

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder leftBuilder = contestant.getMatrixBuilder(dim, dim);
        for (int j = 0; j < dim; j++) {
            for (int i = 0; i < dim; i++) {
                leftBuilder.set(i, j, Math.random());
            }
        }
        left = leftBuilder.get();

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder rightBuilder = contestant.getMatrixBuilder(dim, dim);
        for (int j = 0; j < dim; j++) {
            for (int i = 0; i < dim; i++) {
                rightBuilder.set(i, j, Math.random());
            }
        }
        right = rightBuilder.get();

        product = contestant.getMatrixBuilder(dim, dim).get();
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        this.verifyStateless(myOperation.getClass());

    }

}
