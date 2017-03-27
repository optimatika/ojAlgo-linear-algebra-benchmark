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
 * Mac Pro 2017-03-15
 *
 * <pre>
# Run complete. Total time: 00:23:15

Benchmark               (dim)  (library)   Mode  Cnt        Score       Error  Units
DecomposeEigen.execute      2       EJML  thrpt    5  1588948.697 ± 10522.036  ops/s
DecomposeEigen.execute      2        MTJ  thrpt    5   136739.957 ±  1588.252  ops/s
DecomposeEigen.execute      2     ojAlgo  thrpt    5  1390676.198 ±  5230.988  ops/s
DecomposeEigen.execute      3       EJML  thrpt    5   586254.824 ±  2782.997  ops/s
DecomposeEigen.execute      3        MTJ  thrpt    5    61974.174 ±  2109.253  ops/s
DecomposeEigen.execute      3     ojAlgo  thrpt    5   691992.388 ±  3039.329  ops/s
DecomposeEigen.execute      4       EJML  thrpt    5   328198.423 ±  2484.400  ops/s
DecomposeEigen.execute      4        MTJ  thrpt    5    49345.305 ±   362.092  ops/s
DecomposeEigen.execute      4     ojAlgo  thrpt    5   416739.575 ±  1953.413  ops/s
DecomposeEigen.execute      5       EJML  thrpt    5   225819.643 ±  1029.626  ops/s
DecomposeEigen.execute      5        MTJ  thrpt    5    40075.590 ±  1050.354  ops/s
DecomposeEigen.execute      5     ojAlgo  thrpt    5   312181.215 ±  1783.941  ops/s
DecomposeEigen.execute     10       EJML  thrpt    5    53854.242 ±   173.035  ops/s
DecomposeEigen.execute     10        MTJ  thrpt    5    18692.964 ±  1216.545  ops/s
DecomposeEigen.execute     10     ojAlgo  thrpt    5    77584.149 ±   876.953  ops/s
DecomposeEigen.execute     20       EJML  thrpt    5    10799.539 ±    42.450  ops/s
DecomposeEigen.execute     20        MTJ  thrpt    5     7928.053 ±    21.067  ops/s
DecomposeEigen.execute     20     ojAlgo  thrpt    5    20241.613 ±    37.410  ops/s
DecomposeEigen.execute     50       EJML  thrpt    5     1242.983 ±    25.181  ops/s
DecomposeEigen.execute     50        MTJ  thrpt    5     1524.953 ±    18.243  ops/s
DecomposeEigen.execute     50     ojAlgo  thrpt    5     1996.984 ±     5.000  ops/s
DecomposeEigen.execute    100       EJML  thrpt    5      207.909 ±     0.461  ops/s
DecomposeEigen.execute    100        MTJ  thrpt    5      310.073 ±     3.670  ops/s
DecomposeEigen.execute    100     ojAlgo  thrpt    5      340.594 ±     1.125  ops/s
DecomposeEigen.execute    200       EJML  thrpt    5       31.712 ±     0.196  ops/s
DecomposeEigen.execute    200        MTJ  thrpt    5       75.214 ±     1.558  ops/s
DecomposeEigen.execute    200     ojAlgo  thrpt    5       51.367 ±     1.074  ops/s
DecomposeEigen.execute    500       EJML  thrpt    5        2.293 ±     0.014  ops/s
DecomposeEigen.execute    500        MTJ  thrpt    5       10.834 ±     0.299  ops/s
DecomposeEigen.execute    500     ojAlgo  thrpt    5        3.566 ±     0.074  ops/s
DecomposeEigen.execute   1000       EJML  thrpt    5        0.256 ±     0.001  ops/s
DecomposeEigen.execute   1000        MTJ  thrpt    5        2.095 ±     0.106  ops/s
DecomposeEigen.execute   1000     ojAlgo  thrpt    5        0.452 ±     0.022  ops/s
DecomposeEigen.execute   2000       EJML  thrpt    5        0.030 ±     0.001  ops/s
DecomposeEigen.execute   2000        MTJ  thrpt    5        0.305 ±     0.008  ops/s
DecomposeEigen.execute   2000     ojAlgo  thrpt    5        0.046 ±     0.001  ops/s
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

<<<<<<< HEAD
    @Param({ "2", "3", "4", "5", "10", "20", "50", "100", "200", "500", "1000", "2000"/* , "5000", "10000" */ })
    public int dim;
    @Param({ "EJML", "MTJ", "ojAlgo" })
=======
    @Param({ "2", "3", "4", "5", "10", "20", "50", "100", "200", "500", "1000", "2000" })
    public int dim;
    @Param({ "ACM", "EJML", "MTJ", "ojAlgo" })
>>>>>>> branch 'master' of https://github.com/optimatika/ojAlgo-linear-algebra-benchmark.git
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
