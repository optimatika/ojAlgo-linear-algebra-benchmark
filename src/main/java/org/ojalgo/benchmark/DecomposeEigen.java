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
 * Mac Pro 2015-07-01
 *
 * <pre>
# Run complete. Total time: 00:13:36

Benchmark               (dim)  (library)   Mode  Cnt         Score         Error    Units
DecomposeEigen.execute      2       EJML  thrpt    3  88419411,622 ± 2632421,642  ops/min
DecomposeEigen.execute      2        MTJ  thrpt    3   7960120,758 ±  131808,738  ops/min
DecomposeEigen.execute      2     ojAlgo  thrpt    3  90079803,092 ± 1241935,865  ops/min
DecomposeEigen.execute      3       EJML  thrpt    3  30763741,324 ± 1332825,150  ops/min
DecomposeEigen.execute      3        MTJ  thrpt    3   3782723,778 ±  278004,057  ops/min
DecomposeEigen.execute      3     ojAlgo  thrpt    3  36140444,133 ±  405368,262  ops/min
DecomposeEigen.execute      4       EJML  thrpt    3  16099265,219 ±  211426,044  ops/min
DecomposeEigen.execute      4        MTJ  thrpt    3   3081823,874 ±   78593,344  ops/min
DecomposeEigen.execute      4     ojAlgo  thrpt    3  25876373,025 ± 3492652,031  ops/min
DecomposeEigen.execute      5       EJML  thrpt    3  12344613,990 ±  389596,033  ops/min
DecomposeEigen.execute      5        MTJ  thrpt    3   2553529,480 ±  107826,612  ops/min
DecomposeEigen.execute      5     ojAlgo  thrpt    3  16593123,088 ±  426553,403  ops/min
DecomposeEigen.execute     10       EJML  thrpt    3   2838306,797 ±  192581,259  ops/min
DecomposeEigen.execute     10        MTJ  thrpt    3   1257630,710 ±   12740,192  ops/min
DecomposeEigen.execute     10     ojAlgo  thrpt    3   4548650,123 ±  127705,006  ops/min
DecomposeEigen.execute     20       EJML  thrpt    3    589257,561 ±   11316,301  ops/min
DecomposeEigen.execute     20        MTJ  thrpt    3    537911,085 ±   19071,275  ops/min
DecomposeEigen.execute     20     ojAlgo  thrpt    3   1093043,013 ±   57693,924  ops/min
DecomposeEigen.execute     50       EJML  thrpt    3     71166,939 ±    2520,196  ops/min
DecomposeEigen.execute     50        MTJ  thrpt    3     87017,786 ±    2774,765  ops/min
DecomposeEigen.execute     50     ojAlgo  thrpt    3    121113,268 ±    2833,273  ops/min
DecomposeEigen.execute    100       EJML  thrpt    3     12388,172 ±     623,489  ops/min
DecomposeEigen.execute    100        MTJ  thrpt    3     15264,238 ±     974,360  ops/min
DecomposeEigen.execute    100     ojAlgo  thrpt    3     20448,991 ±     320,693  ops/min
DecomposeEigen.execute    200       EJML  thrpt    3      1838,544 ±      41,112  ops/min
DecomposeEigen.execute    200        MTJ  thrpt    3      4110,957 ±     227,798  ops/min
DecomposeEigen.execute    200     ojAlgo  thrpt    3      3091,946 ±       6,959  ops/min
DecomposeEigen.execute    500       EJML  thrpt    3       131,009 ±      10,196  ops/min
DecomposeEigen.execute    500        MTJ  thrpt    3       582,979 ±      43,421  ops/min
DecomposeEigen.execute    500     ojAlgo  thrpt    3       211,055 ±       2,400  ops/min
DecomposeEigen.execute   1000       EJML  thrpt    3        15,013 ±       1,056  ops/min
DecomposeEigen.execute   1000        MTJ  thrpt    3       107,251 ±       4,268  ops/min
DecomposeEigen.execute   1000     ojAlgo  thrpt    3        26,805 ±       9,113  ops/min
DecomposeEigen.execute   2000       EJML  thrpt    3         1,734 ±       0,041  ops/min
DecomposeEigen.execute   2000        MTJ  thrpt    3        16,002 ±       1,247  ops/min
DecomposeEigen.execute   2000     ojAlgo  thrpt    3         2,692 ±       0,109  ops/min
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

	@Param({ "2", "3", "4", "5", "10", "20", "50", "100", "200", "500", "1000",
			"2000"/* , "5000", "10000" */ })
	public int dim;
	@Param({ "EJML", "MTJ", "ojAlgo" })
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
