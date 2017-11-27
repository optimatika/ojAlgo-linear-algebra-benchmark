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
package org.ojalgo.benchmark.matrix.suite;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.matrix.MatrixBenchmarkContestant;
import org.ojalgo.benchmark.matrix.MatrixBenchmarkSuite;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Mac Pro: 2016-06-13
 *
 * <pre>
 * </pre>
 *
 * Mac Pro: 2016-06-13
 *
 * <pre>
# Run complete. Total time: 00:04:49

Benchmark                         (dim)  (library)   Mode  Cnt           Score           Error    Units
SquareMultiplyTransposed.execute      2       EJML  thrpt    3  1105098298,325 ± 441132957,604  ops/min
SquareMultiplyTransposed.execute      2        MTJ  thrpt    3    66520966,161 ±  14646059,633  ops/min
SquareMultiplyTransposed.execute      2     ojAlgo  thrpt    3   748311723,081 ±  31482293,474  ops/min
SquareMultiplyTransposed.execute      3       EJML  thrpt    3   538811093,707 ±  11368277,460  ops/min
SquareMultiplyTransposed.execute      3        MTJ  thrpt    3    62098285,119 ±  17399932,630  ops/min
SquareMultiplyTransposed.execute      3     ojAlgo  thrpt    3   618930562,592 ±  27164248,019  ops/min
SquareMultiplyTransposed.execute      4       EJML  thrpt    3   292181680,571 ±  57471698,985  ops/min
SquareMultiplyTransposed.execute      4        MTJ  thrpt    3    61364718,584 ±   7126321,731  ops/min
SquareMultiplyTransposed.execute      4     ojAlgo  thrpt    3   429421929,945 ±  42023413,830  ops/min
SquareMultiplyTransposed.execute      5       EJML  thrpt    3   207573102,672 ±   5369511,075  ops/min
SquareMultiplyTransposed.execute      5        MTJ  thrpt    3    57665356,148 ±   1187933,400  ops/min
SquareMultiplyTransposed.execute      5     ojAlgo  thrpt    3   295035453,576 ±   9833366,606  ops/min
SquareMultiplyTransposed.execute     10       EJML  thrpt    3    37331195,694 ±    715634,605  ops/min
SquareMultiplyTransposed.execute     10        MTJ  thrpt    3    39855913,292 ±   5320013,474  ops/min
SquareMultiplyTransposed.execute     10     ojAlgo  thrpt    3    43753205,880 ±   1659231,923  ops/min
SquareMultiplyTransposed.execute     20       EJML  thrpt    3     4834167,366 ±     22095,366  ops/min
SquareMultiplyTransposed.execute     20        MTJ  thrpt    3    13000728,372 ±   1269310,380  ops/min
SquareMultiplyTransposed.execute     20     ojAlgo  thrpt    3     5133548,044 ±     64483,787  ops/min
SquareMultiplyTransposed.execute     50       EJML  thrpt    3      377212,470 ±     13364,084  ops/min
SquareMultiplyTransposed.execute     50        MTJ  thrpt    3     1460361,785 ±    609263,550  ops/min
SquareMultiplyTransposed.execute     50     ojAlgo  thrpt    3      600459,321 ±     35889,836  ops/min
SquareMultiplyTransposed.execute    100       EJML  thrpt    3       53981,109 ±     29655,861  ops/min
SquareMultiplyTransposed.execute    100        MTJ  thrpt    3      225670,842 ±      6108,485  ops/min
SquareMultiplyTransposed.execute    100     ojAlgo  thrpt    3      136475,528 ±     22460,286  ops/min
SquareMultiplyTransposed.execute    200       EJML  thrpt    3        7372,916 ±        83,375  ops/min
SquareMultiplyTransposed.execute    200        MTJ  thrpt    3      105387,706 ±      6180,372  ops/min
SquareMultiplyTransposed.execute    200     ojAlgo  thrpt    3       40171,230 ±      3433,523  ops/min
SquareMultiplyTransposed.execute    500       EJML  thrpt    3         493,104 ±        46,475  ops/min
SquareMultiplyTransposed.execute    500        MTJ  thrpt    3       11124,514 ±       658,281  ops/min
SquareMultiplyTransposed.execute    500     ojAlgo  thrpt    3        2905,646 ±      2658,720  ops/min
SquareMultiplyTransposed.execute   1000       EJML  thrpt    3          53,964 ±         2,979  ops/min
SquareMultiplyTransposed.execute   1000        MTJ  thrpt    3        1430,790 ±      1336,576  ops/min
SquareMultiplyTransposed.execute   1000     ojAlgo  thrpt    3         397,112 ±       846,492  ops/min
 * </pre>
 *
 * MacBook Pro: 2017-11-26
 *
 * <pre>
Result "org.ojalgo.benchmark.matrix.SquareMultiplyRightTransposed.execute":
  202.915 ±(99.9%) 27.347 ops/min [Average]
  (min, avg, max) = (201.805, 202.915, 204.620), stdev = 1.499
  CI (99.9%): [175.569, 230.262] (assumes normal distribution)


# Run complete. Total time: 00:03:15

Benchmark                              (dim)  (library)   Mode  Cnt           Score           Error    Units
SquareMultiplyRightTransposed.execute      2       EJML  thrpt    3  1724060092.011 ±  65396510.907  ops/min
SquareMultiplyRightTransposed.execute      2     ojAlgo  thrpt    3   704169517.962 ± 142318323.525  ops/min
SquareMultiplyRightTransposed.execute      3       EJML  thrpt    3  1033590631.137 ± 165010509.941  ops/min
SquareMultiplyRightTransposed.execute      3     ojAlgo  thrpt    3   423638972.807 ±  34002247.889  ops/min
SquareMultiplyRightTransposed.execute      4       EJML  thrpt    3   603379823.687 ± 166353257.136  ops/min
SquareMultiplyRightTransposed.execute      4     ojAlgo  thrpt    3   262935341.732 ±  59029463.176  ops/min
SquareMultiplyRightTransposed.execute      5       EJML  thrpt    3   345062237.030 ±  95645123.500  ops/min
SquareMultiplyRightTransposed.execute      5     ojAlgo  thrpt    3   166567211.303 ±  36911095.893  ops/min
SquareMultiplyRightTransposed.execute     10       EJML  thrpt    3    58441365.638 ±   2157613.614  ops/min
SquareMultiplyRightTransposed.execute     10     ojAlgo  thrpt    3     6355299.394 ±   1169788.575  ops/min
SquareMultiplyRightTransposed.execute     20       EJML  thrpt    3     8890660.471 ±   3108236.735  ops/min
SquareMultiplyRightTransposed.execute     20     ojAlgo  thrpt    3     4558602.079 ±    979922.176  ops/min
SquareMultiplyRightTransposed.execute     50       EJML  thrpt    3      688228.506 ±     93275.849  ops/min
SquareMultiplyRightTransposed.execute     50     ojAlgo  thrpt    3      547683.165 ±    214203.203  ops/min
SquareMultiplyRightTransposed.execute    100       EJML  thrpt    3       89363.459 ±     13461.607  ops/min
SquareMultiplyRightTransposed.execute    100     ojAlgo  thrpt    3      126474.843 ±     12438.003  ops/min
SquareMultiplyRightTransposed.execute    200       EJML  thrpt    3       12783.384 ±      1593.165  ops/min
SquareMultiplyRightTransposed.execute    200     ojAlgo  thrpt    3       21000.720 ±      7863.501  ops/min
SquareMultiplyRightTransposed.execute    500       EJML  thrpt    3         798.284 ±       124.370  ops/min
SquareMultiplyRightTransposed.execute    500     ojAlgo  thrpt    3        1539.304 ±       299.592  ops/min
SquareMultiplyRightTransposed.execute   1000       EJML  thrpt    3          73.208 ±        30.899  ops/min
SquareMultiplyRightTransposed.execute   1000     ojAlgo  thrpt    3         202.915 ±        27.347  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class SquareMultiplyRightTransposed extends MatrixBenchmarkSuite {

    @FunctionalInterface
    public static interface TaskDefinition<T> {

        int doThThing();

    }

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkSuite.run(SquareMultiplyRightTransposed.class);
    }

    @Param({ "1", "2", "3", "4", "5", "8", "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000", "1024", "2000", "2048", "4096",
            "5000", "8192", "10000" })
    public int dim;

    @Param({ "EJML", "ojAlgo" })
    public String library;

    private MatrixBenchmarkContestant<?>.LeftTransposedMultiplier myTransposedMultiplier;

    Object left;
    Object righ;

    @Override
    @Benchmark
    public Object execute() {
        return myTransposedMultiplier.multiply(left, righ);
    }

    @Setup
    public void setup() {

        contestant = MatrixBenchmarkContestant.CONTESTANTS.get(library);

        myTransposedMultiplier = contestant.getLeftTransposedMultiplier();

        final double[][] tmpLeft = new double[dim][dim];
        for (int i = 0; i < tmpLeft.length; i++) {
            final double[] tmpRow = tmpLeft[i];
            for (int j = 0; j < tmpRow.length; j++) {
                tmpRow[j] = Math.random();
            }
        }
        left = contestant.convert(tmpLeft);

        final double[][] tmpRight = new double[dim][dim];
        for (int i = 0; i < tmpRight.length; i++) {
            final double[] tmpRow = tmpRight[i];
            for (int j = 0; j < tmpRow.length; j++) {
                tmpRow[j] = Math.random();
            }
        }
        righ = contestant.convert(tmpRight);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        this.verifyStateless(myTransposedMultiplier.getClass());

    }

}
