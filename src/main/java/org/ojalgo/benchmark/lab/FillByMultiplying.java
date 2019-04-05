/*
* Copyright 1997-2019 Optimatika (www.optimatika.se)
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
 * <h1>Mac Pro (Early 2009)</h1>
 * <h2>2018-01-23</h2>
 *
 * <pre>
Result "org.ojalgo.benchmark.matrix.operation.FillByMultiplying.execute":
  194.146 ±(99.9%) 117.518 ops/min [Average]
  (min, avg, max) = (186.729, 194.146, 198.335), stdev = 6.442
  CI (99.9%): [76.628, 311.664] (assumes normal distribution)


# Run complete. Total time: 00:59:43

Benchmark                 (dim)  (library)   Mode  Cnt           Score           Error    Units
FillByMultiplying.execute      1        ACM  thrpt    3   329263223.508 ±  96027296.474  ops/min
FillByMultiplying.execute      1       EJML  thrpt    3  4291732954.059 ± 105373742.725  ops/min
FillByMultiplying.execute      1     ojAlgo  thrpt    3  2301941211.909 ±  43271779.723  ops/min
FillByMultiplying.execute      1        MTJ  thrpt    3    70257463.645 ±   3096263.648  ops/min
FillByMultiplying.execute      2        ACM  thrpt    3   206204634.779 ±  13080458.952  ops/min
FillByMultiplying.execute      2       EJML  thrpt    3  1395749101.564 ±  48910711.071  ops/min
FillByMultiplying.execute      2     ojAlgo  thrpt    3  1763662189.314 ± 170774034.576  ops/min
FillByMultiplying.execute      2        MTJ  thrpt    3    69063002.481 ±   3196416.703  ops/min
FillByMultiplying.execute      3        ACM  thrpt    3   146513520.056 ±  11797181.853  ops/min
FillByMultiplying.execute      3       EJML  thrpt    3   553747399.586 ±  10740379.424  ops/min
FillByMultiplying.execute      3     ojAlgo  thrpt    3  1341106069.535 ±  24995137.903  ops/min
FillByMultiplying.execute      3        MTJ  thrpt    3    63526546.622 ±   1890233.478  ops/min
FillByMultiplying.execute      4        ACM  thrpt    3    96464279.073 ±   1903087.484  ops/min
FillByMultiplying.execute      4       EJML  thrpt    3   315320635.079 ±  23733226.717  ops/min
FillByMultiplying.execute      4     ojAlgo  thrpt    3   722491071.678 ±   3031593.487  ops/min
FillByMultiplying.execute      4        MTJ  thrpt    3    59914856.634 ±   5683177.133  ops/min
FillByMultiplying.execute      5        ACM  thrpt    3    72072893.253 ±   1347489.214  ops/min
FillByMultiplying.execute      5       EJML  thrpt    3   215795124.049 ±   5361061.055  ops/min
FillByMultiplying.execute      5     ojAlgo  thrpt    3   426940958.309 ±   6951675.938  ops/min
FillByMultiplying.execute      5        MTJ  thrpt    3    56248533.337 ±   1509286.603  ops/min
FillByMultiplying.execute      8        ACM  thrpt    3    28767033.328 ±    675190.416  ops/min
FillByMultiplying.execute      8       EJML  thrpt    3    66319304.199 ±   2623543.566  ops/min
FillByMultiplying.execute      8     ojAlgo  thrpt    3    93516811.929 ±   2316402.715  ops/min
FillByMultiplying.execute      8        MTJ  thrpt    3    53059131.732 ±   8564379.811  ops/min
FillByMultiplying.execute     10        ACM  thrpt    3    18333157.201 ±    674002.832  ops/min
FillByMultiplying.execute     10       EJML  thrpt    3    38726821.791 ±    432341.330  ops/min
FillByMultiplying.execute     10     ojAlgo  thrpt    3    54310008.976 ±   1057948.912  ops/min
FillByMultiplying.execute     10        MTJ  thrpt    3    44469252.116 ±   1782393.162  ops/min
FillByMultiplying.execute     16        ACM  thrpt    3     6123594.576 ±    279905.221  ops/min
FillByMultiplying.execute     16       EJML  thrpt    3    10409616.819 ±    268603.381  ops/min
FillByMultiplying.execute     16     ojAlgo  thrpt    3     6183801.473 ±   5292396.955  ops/min
FillByMultiplying.execute     16        MTJ  thrpt    3    10678282.941 ±    276939.990  ops/min
FillByMultiplying.execute     20        ACM  thrpt    3     3436623.157 ±    112123.743  ops/min
FillByMultiplying.execute     20       EJML  thrpt    3     5943321.206 ±    165241.303  ops/min
FillByMultiplying.execute     20     ojAlgo  thrpt    3     3526168.111 ±   1209826.908  ops/min
FillByMultiplying.execute     20        MTJ  thrpt    3     8809863.851 ±    475867.605  ops/min
FillByMultiplying.execute     32        ACM  thrpt    3      920205.452 ±    144022.093  ops/min
FillByMultiplying.execute     32       EJML  thrpt    3     1556788.135 ±   2891033.723  ops/min
FillByMultiplying.execute     32     ojAlgo  thrpt    3      902212.310 ±    136738.304  ops/min
FillByMultiplying.execute     32        MTJ  thrpt    3     4391859.652 ±     93739.766  ops/min
FillByMultiplying.execute     50        ACM  thrpt    3      263820.168 ±     15451.349  ops/min
FillByMultiplying.execute     50       EJML  thrpt    3      255196.093 ±     37975.897  ops/min
FillByMultiplying.execute     50     ojAlgo  thrpt    3      483159.338 ±    127904.359  ops/min
FillByMultiplying.execute     50        MTJ  thrpt    3     1489005.752 ±     51611.074  ops/min
FillByMultiplying.execute     64        ACM  thrpt    3      119548.209 ±      3177.785  ops/min
FillByMultiplying.execute     64       EJML  thrpt    3      138625.111 ±      2112.338  ops/min
FillByMultiplying.execute     64     ojAlgo  thrpt    3      342990.467 ±     15641.647  ops/min
FillByMultiplying.execute     64        MTJ  thrpt    3      802495.225 ±     46073.445  ops/min
FillByMultiplying.execute    100        ACM  thrpt    3       22859.403 ±      8576.811  ops/min
FillByMultiplying.execute    100       EJML  thrpt    3       57411.877 ±      6162.488  ops/min
FillByMultiplying.execute    100     ojAlgo  thrpt    3      107915.006 ±      5392.087  ops/min
FillByMultiplying.execute    100        MTJ  thrpt    3      224638.832 ±     24406.133  ops/min
FillByMultiplying.execute    128        ACM  thrpt    3       13573.053 ±      3505.167  ops/min
FillByMultiplying.execute    128       EJML  thrpt    3       26224.898 ±       644.921  ops/min
FillByMultiplying.execute    128     ojAlgo  thrpt    3       84758.208 ±      5730.780  ops/min
FillByMultiplying.execute    128        MTJ  thrpt    3      258551.891 ±      9910.751  ops/min
FillByMultiplying.execute    200        ACM  thrpt    3        2229.626 ±       150.199  ops/min
FillByMultiplying.execute    200       EJML  thrpt    3        7020.602 ±      3459.258  ops/min
FillByMultiplying.execute    200     ojAlgo  thrpt    3       32340.479 ±      5511.657  ops/min
FillByMultiplying.execute    200        MTJ  thrpt    3      114174.188 ±      1990.288  ops/min
FillByMultiplying.execute    256        ACM  thrpt    3         940.938 ±        48.058  ops/min
FillByMultiplying.execute    256       EJML  thrpt    3        3159.279 ±        29.932  ops/min
FillByMultiplying.execute    256     ojAlgo  thrpt    3       16092.998 ±      7246.103  ops/min
FillByMultiplying.execute    256        MTJ  thrpt    3       72868.599 ±     49246.246  ops/min
FillByMultiplying.execute    500        ACM  thrpt    3          62.068 ±         1.941  ops/min
FillByMultiplying.execute    500       EJML  thrpt    3         490.712 ±         7.512  ops/min
FillByMultiplying.execute    500     ojAlgo  thrpt    3        2278.636 ±       440.263  ops/min
FillByMultiplying.execute    500        MTJ  thrpt    3       11759.198 ±      1037.668  ops/min
FillByMultiplying.execute    512        ACM  thrpt    3          54.314 ±         1.092  ops/min
FillByMultiplying.execute    512       EJML  thrpt    3         340.816 ±        26.230  ops/min
FillByMultiplying.execute    512     ojAlgo  thrpt    3        2101.099 ±       369.204  ops/min
FillByMultiplying.execute    512        MTJ  thrpt    3       11094.697 ±       903.918  ops/min
FillByMultiplying.execute   1000        ACM  thrpt    3           3.383 ±         1.759  ops/min
FillByMultiplying.execute   1000       EJML  thrpt    3          50.264 ±         6.818  ops/min
FillByMultiplying.execute   1000     ojAlgo  thrpt    3         277.238 ±        37.923  ops/min
FillByMultiplying.execute   1000        MTJ  thrpt    3        1682.720 ±        65.638  ops/min
FillByMultiplying.execute   1024        ACM  thrpt    3           3.009 ±         1.581  ops/min
FillByMultiplying.execute   1024       EJML  thrpt    3          37.449 ±         1.668  ops/min
FillByMultiplying.execute   1024     ojAlgo  thrpt    3         261.330 ±        17.740  ops/min
FillByMultiplying.execute   1024        MTJ  thrpt    3        1547.594 ±       148.474  ops/min
FillByMultiplying.execute   2000        ACM  thrpt    3           0.275 ±         0.034  ops/min
FillByMultiplying.execute   2000       EJML  thrpt    3           5.595 ±         0.276  ops/min
FillByMultiplying.execute   2000     ojAlgo  thrpt    3          14.194 ±         0.304  ops/min
FillByMultiplying.execute   2000        MTJ  thrpt    3         194.146 ±       117.518  ops/min
 * </pre>
 *
 * <h2>2018-01-23</h2>
 *
 * <pre>
Result "org.ojalgo.benchmark.matrix.operation.FillByMultiplying.execute":
  1706,836 ±(99.9%) 200,497 ops/min [Average]
  (min, avg, max) = (1698,092, 1706,836, 1719,173), stdev = 10,990
  CI (99.9%): [1506,339, 1907,333] (assumes normal distribution)


# Run complete. Total time: 01:15:04

Benchmark                  (dim)  (library)   Mode  Cnt      Score      Error    Units
FillByMultiplying.execute    500        ACM  thrpt    3     44,298 ±    1,634  ops/min
FillByMultiplying.execute    500       EJML  thrpt    3    454,249 ±   11,834  ops/min
FillByMultiplying.execute    500     ojAlgo  thrpt    3   2848,255 ±  388,521  ops/min
FillByMultiplying.execute    500        MTJ  thrpt    3  11732,788 ± 6604,706  ops/min
FillByMultiplying.execute   1000        ACM  thrpt    3      3,459 ±    0,012  ops/min
FillByMultiplying.execute   1000       EJML  thrpt    3     48,771 ±    2,006  ops/min
FillByMultiplying.execute   1000     ojAlgo  thrpt    3    280,325 ±   44,632  ops/min
FillByMultiplying.execute   1000        MTJ  thrpt    3   1706,836 ±  200,497  ops/min
 * </pre>
 *
 * <h1>MacBook Pro</h1>
 * <h2>2018-02-18</h2>
 *
 * <pre>
# Run complete. Total time: 00:55:34

Benchmark                  (dim)   (lib)   Mode  Cnt        Score        Error    Units
FillByMultiplying.execute    100    EJML  thrpt    3   104466,112 ±  12858,837  ops/min
FillByMultiplying.execute    100  ojAlgo  thrpt    3   187823,159 ±  75227,149  ops/min
FillByMultiplying.execute    100     MTJ  thrpt    3  1055800,735 ±  94181,457  ops/min
FillByMultiplying.execute    200    EJML  thrpt    3    13343,216 ±    810,210  ops/min
FillByMultiplying.execute    200  ojAlgo  thrpt    3    28358,169 ±   5036,854  ops/min
FillByMultiplying.execute    200     MTJ  thrpt    3   207054,612 ± 363281,490  ops/min
FillByMultiplying.execute    500    EJML  thrpt    3      848,180 ±    134,510  ops/min
FillByMultiplying.execute    500  ojAlgo  thrpt    3     1745,767 ±    255,289  ops/min
FillByMultiplying.execute    500     MTJ  thrpt    3    16927,409 ±  18080,689  ops/min
FillByMultiplying.execute   1000    EJML  thrpt    3       91,859 ±     85,520  ops/min
FillByMultiplying.execute   1000  ojAlgo  thrpt    3      214,194 ±    177,195  ops/min
FillByMultiplying.execute   1000     MTJ  thrpt    3     2475,510 ±    211,499  ops/min
FillByMultiplying.execute   2000    EJML  thrpt    3       11,469 ±      1,537  ops/min
FillByMultiplying.execute   2000  ojAlgo  thrpt    3       27,661 ±      3,262  ops/min
FillByMultiplying.execute   2000     MTJ  thrpt    3      331,002 ±     14,727  ops/min
FillByMultiplying.execute   5000    EJML  thrpt    3        0,695 ±      0,044  ops/min
FillByMultiplying.execute   5000  ojAlgo  thrpt    3        0,740 ±      0,013  ops/min
FillByMultiplying.execute   5000     MTJ  thrpt    3       21,268 ±      1,937  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class FillByMultiplying extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(FillByMultiplying.class);
    }

    @Param({ "100", "150", "200", "350", "500", "750", "1000" })
    public int dim;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

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

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        myOperation = library.getOperationFillByMultiplying(false, false);

        left = this.makeRandom(dim, dim, library);
        right = this.makeRandom(dim, dim, library);
        product = this.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
