/*
 * Copyright 1997-2025 Optimatika (www.optimatika.se)
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
Benchmark             (dim)   (lib)   Mode  Cnt         Score         Error    Units
DecomposeEvD.execute     10     ACM  thrpt    3   6320190.423 ± 2612722.707  ops/min
DecomposeEvD.execute     10    EJML  thrpt    3  10190309.875 ±  960055.458  ops/min
DecomposeEvD.execute     10  ojAlgo  thrpt    3  15151371.225 ± 2933426.677  ops/min
DecomposeEvD.execute     16     ACM  thrpt    3   2258405.664 ±  635915.075  ops/min
DecomposeEvD.execute     16    EJML  thrpt    3   3483522.569 ±  534696.795  ops/min
DecomposeEvD.execute     16  ojAlgo  thrpt    3   5167724.773 ±  394446.821  ops/min
DecomposeEvD.execute     16     MTJ  thrpt    3   1784077.915 ±  968987.134  ops/min
DecomposeEvD.execute     20     ACM  thrpt    3   1407412.041 ±  104648.111  ops/min
DecomposeEvD.execute     20    EJML  thrpt    3   2113633.542 ±  400877.389  ops/min
DecomposeEvD.execute     20  ojAlgo  thrpt    3   2919489.293 ±  805948.716  ops/min
DecomposeEvD.execute     20     MTJ  thrpt    3   1314468.075 ±  397704.698  ops/min
DecomposeEvD.execute     32     ACM  thrpt    3    433547.431 ±   24437.010  ops/min
DecomposeEvD.execute     32    EJML  thrpt    3    586740.359 ±   28730.677  ops/min
DecomposeEvD.execute     32  ojAlgo  thrpt    3    698577.448 ±  788535.760  ops/min
DecomposeEvD.execute     32     MTJ  thrpt    3    452424.441 ±  639524.549  ops/min
DecomposeEvD.execute     50     ACM  thrpt    3     81412.780 ±   11122.576  ops/min
DecomposeEvD.execute     50    EJML  thrpt    3    123219.241 ±  114361.410  ops/min
DecomposeEvD.execute     50  ojAlgo  thrpt    3    308883.878 ±  105766.634  ops/min
DecomposeEvD.execute     50     MTJ  thrpt    3    168028.779 ±  124663.573  ops/min
DecomposeEvD.execute     64     ACM  thrpt    3     42915.011 ±   71804.570  ops/min
DecomposeEvD.execute     64    EJML  thrpt    3     95095.680 ±    9790.488  ops/min
DecomposeEvD.execute     64  ojAlgo  thrpt    3    200808.617 ±   17159.669  ops/min
DecomposeEvD.execute     64     MTJ  thrpt    3    143592.100 ±   74779.144  ops/min
DecomposeEvD.execute    100     ACM  thrpt    3     17571.106 ±    6097.191  ops/min
DecomposeEvD.execute    100    EJML  thrpt    3     30046.875 ±    7385.108  ops/min
DecomposeEvD.execute    100  ojAlgo  thrpt    3     65202.869 ±    4575.695  ops/min
DecomposeEvD.execute    100     MTJ  thrpt    3     46954.529 ±   47254.524  ops/min
DecomposeEvD.execute    128     ACM  thrpt    3      9926.939 ±    3902.090  ops/min
DecomposeEvD.execute    128    EJML  thrpt    3     14679.895 ±    4486.203  ops/min
DecomposeEvD.execute    128  ojAlgo  thrpt    3     32426.294 ±   15785.822  ops/min
DecomposeEvD.execute    128     MTJ  thrpt    3     28581.743 ±   41373.934  ops/min
DecomposeEvD.execute    200     ACM  thrpt    3      2318.419 ±    1669.914  ops/min
DecomposeEvD.execute    200    EJML  thrpt    3      2813.243 ±      77.093  ops/min
DecomposeEvD.execute    200  ojAlgo  thrpt    3      6854.357 ±     289.305  ops/min
DecomposeEvD.execute    200     MTJ  thrpt    3      7283.414 ±   22644.247  ops/min
DecomposeEvD.execute    256     ACM  thrpt    3       591.388 ±     290.455  ops/min
DecomposeEvD.execute    256    EJML  thrpt    3      1570.055 ±     538.748  ops/min
DecomposeEvD.execute    256  ojAlgo  thrpt    3      3246.703 ±    1277.347  ops/min
DecomposeEvD.execute    256     MTJ  thrpt    3      3411.191 ±    5275.767  ops/min
DecomposeEvD.execute    500     ACM  thrpt    3       103.701 ±      33.420  ops/min
DecomposeEvD.execute    500    EJML  thrpt    3       218.483 ±     134.560  ops/min
DecomposeEvD.execute    500  ojAlgo  thrpt    3       383.022 ±     546.442  ops/min
DecomposeEvD.execute    500     MTJ  thrpt    3      1582.760 ±    4153.384  ops/min
DecomposeEvD.execute    512     ACM  thrpt    3       121.609 ±      55.534  ops/min
DecomposeEvD.execute    512    EJML  thrpt    3       249.212 ±     305.763  ops/min
DecomposeEvD.execute    512  ojAlgo  thrpt    3       589.090 ±      16.524  ops/min
DecomposeEvD.execute    512     MTJ  thrpt    3      1385.008 ±     858.414  ops/min
DecomposeEvD.execute   1000     ACM  thrpt    3        10.405 ±       1.516  ops/min
DecomposeEvD.execute   1000    EJML  thrpt    3        37.365 ±       6.811  ops/min
DecomposeEvD.execute   1000  ojAlgo  thrpt    3        80.501 ±      26.801  ops/min
DecomposeEvD.execute   1000     MTJ  thrpt    3       314.078 ±     219.011  ops/min
 * </pre>
 *
 * <h1>Mac Pro (Early 2009)</h1>
 * <h2>2017-04-22</h2>
 *
 * <pre>
# Run complete. Total time: 00:09:27

Benchmark               (dim)  (library)   Mode  Cnt          Score         Error    Units
DecomposeEigen.execute      2        ACM  thrpt    3   33397262.344 ±  249323.821  ops/min
DecomposeEigen.execute      2       EJML  thrpt    3   94220563.635 ± 4838080.227  ops/min
DecomposeEigen.execute      2        MTJ  thrpt    3    7771431.065 ±   62895.481  ops/min
DecomposeEigen.execute      2     ojAlgo  thrpt    3  120742245.634 ± 1638043.407  ops/min
DecomposeEigen.execute      3        ACM  thrpt    3   22625726.902 ± 1820042.504  ops/min
DecomposeEigen.execute      3       EJML  thrpt    3   40173043.585 ±  515516.541  ops/min
DecomposeEigen.execute      3        MTJ  thrpt    3    3639615.196 ±  167072.002  ops/min
DecomposeEigen.execute      3     ojAlgo  thrpt    3   49411207.108 ± 2742733.801  ops/min
DecomposeEigen.execute      4        ACM  thrpt    3   14315708.901 ±  543309.091  ops/min
DecomposeEigen.execute      4       EJML  thrpt    3   19613268.425 ±   50017.519  ops/min
DecomposeEigen.execute      4        MTJ  thrpt    3    3003803.411 ±   14680.159  ops/min
DecomposeEigen.execute      4     ojAlgo  thrpt    3   29928601.712 ±  514872.775  ops/min
DecomposeEigen.execute      5        ACM  thrpt    3    9738321.327 ±  186779.450  ops/min
DecomposeEigen.execute      5       EJML  thrpt    3   13352140.758 ±  271359.713  ops/min
DecomposeEigen.execute      5        MTJ  thrpt    3    2401550.470 ±  137698.503  ops/min
DecomposeEigen.execute      5     ojAlgo  thrpt    3   18518925.149 ±  414202.187  ops/min
DecomposeEigen.execute     10        ACM  thrpt    3    2888860.143 ±   92476.861  ops/min
DecomposeEigen.execute     10       EJML  thrpt    3    3314815.290 ±   55300.514  ops/min
DecomposeEigen.execute     10        MTJ  thrpt    3    1212760.102 ±   68935.498  ops/min
DecomposeEigen.execute     10     ojAlgo  thrpt    3    4534784.144 ±  133178.199  ops/min
DecomposeEigen.execute     20        ACM  thrpt    3     572653.744 ±   97982.205  ops/min
DecomposeEigen.execute     20       EJML  thrpt    3     631092.776 ±   39677.499  ops/min
DecomposeEigen.execute     20        MTJ  thrpt    3     482842.198 ±   19881.610  ops/min
DecomposeEigen.execute     20     ojAlgo  thrpt    3    1139876.829 ±   29248.264  ops/min
DecomposeEigen.execute     50        ACM  thrpt    3      52320.760 ±    7918.925  ops/min
DecomposeEigen.execute     50       EJML  thrpt    3      76655.832 ±    2413.594  ops/min
DecomposeEigen.execute     50        MTJ  thrpt    3      93036.607 ±    4786.415  ops/min
DecomposeEigen.execute     50     ojAlgo  thrpt    3     124228.913 ±    2611.090  ops/min
DecomposeEigen.execute    100        ACM  thrpt    3       8112.929 ±     173.137  ops/min
DecomposeEigen.execute    100       EJML  thrpt    3      12822.364 ±      90.490  ops/min
DecomposeEigen.execute    100        MTJ  thrpt    3      19272.925 ±    2311.391  ops/min
DecomposeEigen.execute    100     ojAlgo  thrpt    3      21422.459 ±     736.925  ops/min
DecomposeEigen.execute    200        ACM  thrpt    3        942.634 ±      57.384  ops/min
DecomposeEigen.execute    200       EJML  thrpt    3       1936.929 ±      30.788  ops/min
DecomposeEigen.execute    200        MTJ  thrpt    3       4473.691 ±     160.030  ops/min
DecomposeEigen.execute    200     ojAlgo  thrpt    3       3284.895 ±      89.331  ops/min
DecomposeEigen.execute    500        ACM  thrpt    3         52.764 ±       5.107  ops/min
DecomposeEigen.execute    500       EJML  thrpt    3        137.048 ±       5.047  ops/min
DecomposeEigen.execute    500        MTJ  thrpt    3        653.686 ±      56.168  ops/min
DecomposeEigen.execute    500     ojAlgo  thrpt    3        216.250 ±       1.088  ops/min
DecomposeEigen.execute   1000        ACM  thrpt    3          3.429 ±       1.446  ops/min
DecomposeEigen.execute   1000       EJML  thrpt    3         15.457 ±       1.017  ops/min
DecomposeEigen.execute   1000        MTJ  thrpt    3        122.480 ±      43.279  ops/min
DecomposeEigen.execute   1000     ojAlgo  thrpt    3         28.640 ±       5.896  ops/min
 * </pre>
 *
 * <h1>MacBook Pro (2017)</h1>
 * <h2>2018-04-02</h2>
 *
 * <pre>
# Run complete. Total time: 00:22:30

Benchmark             (dim)   (lib)   Mode  Cnt         Score         Error    Units
DecomposeEvD.execute     10     ACM  thrpt    3   5442405.572 ±  265042.555  ops/min
DecomposeEvD.execute     10    EJML  thrpt    3   8178797.869 ± 5412544.163  ops/min
DecomposeEvD.execute     10  ojAlgo  thrpt    3  11565317.217 ±  404159.267  ops/min
DecomposeEvD.execute     10     MTJ  thrpt    3   2881351.395 ±  988968.345  ops/min
DecomposeEvD.execute    100     ACM  thrpt    3     16507.930 ±    1150.860  ops/min
DecomposeEvD.execute    100    EJML  thrpt    3     25445.140 ±    1938.831  ops/min
DecomposeEvD.execute    100  ojAlgo  thrpt    3     47068.451 ±    6624.771  ops/min
DecomposeEvD.execute    100     MTJ  thrpt    3     41896.833 ±   68360.220  ops/min
DecomposeEvD.execute   1000     ACM  thrpt    3         7.601 ±       1.521  ops/min
DecomposeEvD.execute   1000    EJML  thrpt    3        29.728 ±       4.718  ops/min
DecomposeEvD.execute   1000  ojAlgo  thrpt    3        51.252 ±       0.353  ops/min
DecomposeEvD.execute   1000     MTJ  thrpt    3       252.679 ±     162.917  ops/min
 * </pre>
 *
 * MacBook Air: 2015-06-16
 *
 * <pre>
 * # Run complete. Total time: 01:41:52
 *
Benchmark               (dim)  (library)   Mode  Cnt         Score         Error    Units
DecomposeEigen.execute      2       EJML  thrpt    3  89462361,257 ± 17152845,758  ops/min
DecomposeEigen.execute      2        MTJ  thrpt    3   8525733,495 ±  2632684,583  ops/min
DecomposeEigen.execute      2     ojAlgo  thrpt    3  84525045,475 ± 30993308,244  ops/min
DecomposeEigen.execute      3       EJML  thrpt    3  34074685,900 ± 63302294,511  ops/min
DecomposeEigen.execute      3        MTJ  thrpt    3   3521362,388 ± 13208271,244  ops/min
DecomposeEigen.execute      3     ojAlgo  thrpt    3  39326595,693 ±  6529965,619  ops/min
DecomposeEigen.execute      4       EJML  thrpt    3  19573483,059 ± 10145750,122  ops/min
DecomposeEigen.execute      4        MTJ  thrpt    3   2986575,226 ±  7448742,369  ops/min
DecomposeEigen.execute      4     ojAlgo  thrpt    3  23695687,555 ±  7246852,912  ops/min
DecomposeEigen.execute      5       EJML  thrpt    3  13624211,510 ±  1980635,981  ops/min
DecomposeEigen.execute      5        MTJ  thrpt    3   2677441,070 ±  2447986,027  ops/min
DecomposeEigen.execute      5     ojAlgo  thrpt    3  17977544,806 ± 10916783,544  ops/min
DecomposeEigen.execute     10       EJML  thrpt    3   2959953,377 ±  3119143,129  ops/min
DecomposeEigen.execute     10        MTJ  thrpt    3   1417382,654 ±   644821,653  ops/min
DecomposeEigen.execute     10     ojAlgo  thrpt    3   5333046,484 ±  1370599,099  ops/min
DecomposeEigen.execute     20       EJML  thrpt    3    709012,840 ±   532469,417  ops/min
DecomposeEigen.execute     20        MTJ  thrpt    3    548325,371 ±   336390,414  ops/min
DecomposeEigen.execute     20     ojAlgo  thrpt    3   1210446,687 ±   417784,576  ops/min
DecomposeEigen.execute     50       EJML  thrpt    3     66459,120 ±    73294,910  ops/min
DecomposeEigen.execute     50        MTJ  thrpt    3    102209,133 ±    29849,067  ops/min
DecomposeEigen.execute     50     ojAlgo  thrpt    3    139173,259 ±    21831,583  ops/min
DecomposeEigen.execute    100       EJML  thrpt    3     13489,764 ±     8158,598  ops/min
DecomposeEigen.execute    100        MTJ  thrpt    3     17338,373 ±     3304,911  ops/min
DecomposeEigen.execute    100     ojAlgo  thrpt    3     21366,545 ±     9267,261  ops/min
DecomposeEigen.execute    200       EJML  thrpt    3      2027,740 ±      615,860  ops/min
DecomposeEigen.execute    200        MTJ  thrpt    3      4554,098 ±     1446,725  ops/min
DecomposeEigen.execute    200     ojAlgo  thrpt    3      3247,915 ±      355,730  ops/min
DecomposeEigen.execute    500       EJML  thrpt    3       141,407 ±       48,280  ops/min
DecomposeEigen.execute    500        MTJ  thrpt    3       692,945 ±      209,134  ops/min
DecomposeEigen.execute    500     ojAlgo  thrpt    3       214,952 ±       13,988  ops/min
DecomposeEigen.execute   1000       EJML  thrpt    3        15,372 ±        0,922  ops/min
DecomposeEigen.execute   1000        MTJ  thrpt    3       103,098 ±        4,627  ops/min
DecomposeEigen.execute   1000     ojAlgo  thrpt    3        25,599 ±        2,029  ops/min
 * DecomposeEigen.execute   2000     ojAlgo  thrpt    5         3,290 ±       0,108  ops/min
 * DecomposeEigen.execute   2000       EJML  thrpt    5         2,202 ±       0,034  ops/min
 * DecomposeEigen.execute   2000        MTJ  thrpt    5        17,206 ±       1,364  ops/min
 * DecomposeEigen.execute   5000     ojAlgo  thrpt    5         0,212 ±       0,013  ops/min
 * DecomposeEigen.execute   5000        MTJ  thrpt    5         1,416 ±       0,061  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class DecomposeEvD extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(DecomposeEvD.class);
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

        matrix = MatrixBenchmarkOperation.makeSPD(dim, library);

        myOperation = library.getOperationEvD(dim);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        if (!library.equals(matrix, dim, result)) {
            throw new BenchmarkRequirementsException("Not able to reconstruct the matrix!");
        }
    }

}
