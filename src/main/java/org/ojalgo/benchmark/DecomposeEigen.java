/*
 * Copyright 1997-2015 Optimatika (www.optimatika.se)
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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Mac Pro 2017-03-27
 *
 * <pre>
# Run complete. Total time: 00:41:48

Benchmark               (dim)  (library)   Mode  Cnt        Score       Error  Units
DecomposeEigen.execute      2        ACM  thrpt   15   536795.948 ± 22510.418  ops/s
DecomposeEigen.execute      2       EJML  thrpt   15  1426954.650 ± 55599.347  ops/s
DecomposeEigen.execute      2        MTJ  thrpt   15   129182.634 ±  4890.072  ops/s
DecomposeEigen.execute      2     ojAlgo  thrpt   15  1838395.032 ± 56550.904  ops/s
DecomposeEigen.execute      3        ACM  thrpt   15   339504.639 ± 17850.133  ops/s
DecomposeEigen.execute      3       EJML  thrpt   15   564488.161 ± 15962.775  ops/s
DecomposeEigen.execute      3        MTJ  thrpt   15    57882.905 ±   997.330  ops/s
DecomposeEigen.execute      3     ojAlgo  thrpt   15   793904.265 ± 39630.311  ops/s
DecomposeEigen.execute      4        ACM  thrpt   15   233153.670 ±  6469.204  ops/s
DecomposeEigen.execute      4       EJML  thrpt   15   299188.415 ± 18598.584  ops/s
DecomposeEigen.execute      4        MTJ  thrpt   15    45431.576 ±   624.977  ops/s
DecomposeEigen.execute      4     ojAlgo  thrpt   15   501529.762 ± 24517.456  ops/s
DecomposeEigen.execute      5        ACM  thrpt   15   164323.905 ±  6172.702  ops/s
DecomposeEigen.execute      5       EJML  thrpt   15   213353.287 ±  7279.367  ops/s
DecomposeEigen.execute      5        MTJ  thrpt   15    38215.573 ±  1646.812  ops/s
DecomposeEigen.execute      5     ojAlgo  thrpt   15   314517.883 ± 20166.147  ops/s
DecomposeEigen.execute     10        ACM  thrpt   15    43903.572 ±  1235.683  ops/s
DecomposeEigen.execute     10       EJML  thrpt   15    51787.623 ±  2901.456  ops/s
DecomposeEigen.execute     10        MTJ  thrpt   15    19437.337 ±   500.965  ops/s
DecomposeEigen.execute     10     ojAlgo  thrpt   15    79890.729 ±  4897.018  ops/s
DecomposeEigen.execute     20        ACM  thrpt   15     9248.306 ±    73.431  ops/s
DecomposeEigen.execute     20       EJML  thrpt   15    10750.193 ±   363.898  ops/s
DecomposeEigen.execute     20        MTJ  thrpt   15     7868.846 ±   148.955  ops/s
DecomposeEigen.execute     20     ojAlgo  thrpt   15    17701.635 ±  1400.365  ops/s
DecomposeEigen.execute     50        ACM  thrpt   15      904.975 ±    38.470  ops/s
DecomposeEigen.execute     50       EJML  thrpt   15     1195.038 ±    37.892  ops/s
DecomposeEigen.execute     50        MTJ  thrpt   15     1508.785 ±     9.394  ops/s
DecomposeEigen.execute     50     ojAlgo  thrpt   15     2124.752 ±    32.394  ops/s
DecomposeEigen.execute    100        ACM  thrpt   15      130.582 ±     3.751  ops/s
DecomposeEigen.execute    100       EJML  thrpt   15      209.574 ±     2.295  ops/s
DecomposeEigen.execute    100        MTJ  thrpt   15      316.339 ±     8.081  ops/s
DecomposeEigen.execute    100     ojAlgo  thrpt   15      336.345 ±     3.238  ops/s
DecomposeEigen.execute    200        ACM  thrpt   15       15.718 ±     0.422  ops/s
DecomposeEigen.execute    200       EJML  thrpt   15       31.971 ±     0.412  ops/s
DecomposeEigen.execute    200        MTJ  thrpt   15       77.078 ±     1.374  ops/s
DecomposeEigen.execute    200     ojAlgo  thrpt   15       52.705 ±     0.831  ops/s
DecomposeEigen.execute    500        ACM  thrpt   15        0.874 ±     0.051  ops/s
DecomposeEigen.execute    500       EJML  thrpt   15        2.269 ±     0.027  ops/s
DecomposeEigen.execute    500        MTJ  thrpt   15       10.647 ±     0.098  ops/s
DecomposeEigen.execute    500     ojAlgo  thrpt   15        3.540 ±     0.037  ops/s
DecomposeEigen.execute   1000        ACM  thrpt   15        0.054 ±     0.002  ops/s
DecomposeEigen.execute   1000       EJML  thrpt   15        0.253 ±     0.003  ops/s
DecomposeEigen.execute   1000        MTJ  thrpt   15        1.976 ±     0.038  ops/s
DecomposeEigen.execute   1000     ojAlgo  thrpt   15        0.462 ±     0.012  ops/s
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
public class DecomposeEigen extends LinearAlgebraBenchmark {

    public static void main(final String[] args) throws RunnerException {
        LinearAlgebraBenchmark.run(DecomposeEigen.class);
    }

    @Param({ "2", "3", "4", "5", "10", "20", "50", "100", "200", "500", "1000" })
    public int dim;
    @Param({ "ACM", "EJML", "MTJ", "ojAlgo" })
    public String library;

    Object matrix;

    private BenchmarkContestant<?>.EigenDecomposer myDecomposer;

    @Override
    @Benchmark
    public Object execute() {
        return myDecomposer.decompose(matrix);
    }

    @Setup
    public void setup() {

        contestant = BenchmarkContestant.CONTESTANTS.get(library);

        matrix = contestant.convert(this.makeSPD(dim));

        myDecomposer = contestant.getEigenDecomposer();
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        this.verifyStateless(myDecomposer.getClass());

    }

}
