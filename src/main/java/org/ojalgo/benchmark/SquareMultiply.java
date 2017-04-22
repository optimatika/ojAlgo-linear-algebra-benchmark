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
 * Mac Pro: 2016-06-16
 *
 * <pre>
Benchmark                         (dim)  (library)   Mode  Cnt           Score          Error    Units
SquareMultiply.execute                2       EJML  thrpt   15  1115025722,165 ±  2185660,139  ops/min
SquareMultiply.execute                2        MTJ  thrpt   15    68903433,462 ±  1017964,935  ops/min
SquareMultiply.execute                2     ojAlgo  thrpt   15   749118803,207 ±  1101357,259  ops/min
SquareMultiply.execute                3       EJML  thrpt   15   525499968,364 ± 12822243,964  ops/min
SquareMultiply.execute                3        MTJ  thrpt   15    64091505,436 ±   253494,269  ops/min
SquareMultiply.execute                3     ojAlgo  thrpt   15   607068982,868 ± 19334929,203  ops/min
SquareMultiply.execute                4       EJML  thrpt   15   290721400,796 ±  5505098,300  ops/min
SquareMultiply.execute                4        MTJ  thrpt   15    64213979,990 ±  1569457,469  ops/min
SquareMultiply.execute                4     ojAlgo  thrpt   15   431863499,150 ±  1219953,745  ops/min
SquareMultiply.execute                5       EJML  thrpt   15   207672663,239 ±   283047,224  ops/min
SquareMultiply.execute                5        MTJ  thrpt   15    58103475,106 ±   526325,218  ops/min
SquareMultiply.execute                5     ojAlgo  thrpt   15   295220540,058 ±   621850,718  ops/min
SquareMultiply.execute               10       EJML  thrpt   15    37466146,807 ±   109065,459  ops/min
SquareMultiply.execute               10        MTJ  thrpt   15    39134087,920 ±   459558,234  ops/min
SquareMultiply.execute               10     ojAlgo  thrpt   15    44095203,535 ±   330452,705  ops/min
SquareMultiply.execute               20       EJML  thrpt   15     4825252,992 ±     6117,057  ops/min
SquareMultiply.execute               20        MTJ  thrpt   15    12974567,585 ±   105762,572  ops/min
SquareMultiply.execute               20     ojAlgo  thrpt   15     5429933,327 ±     9153,587  ops/min
SquareMultiply.execute               50       EJML  thrpt   15      392668,430 ±     4252,602  ops/min
SquareMultiply.execute               50        MTJ  thrpt   15     1550932,505 ±     9020,498  ops/min
SquareMultiply.execute               50     ojAlgo  thrpt   15      599785,177 ±     2137,223  ops/min
SquareMultiply.execute              100       EJML  thrpt   15       53032,006 ±      485,635  ops/min
SquareMultiply.execute              100        MTJ  thrpt   15      226246,179 ±     1207,405  ops/min
SquareMultiply.execute              100     ojAlgo  thrpt   15      130688,485 ±     5980,605  ops/min
SquareMultiply.execute              200       EJML  thrpt   15        7319,607 ±       13,978  ops/min
SquareMultiply.execute              200        MTJ  thrpt   15      104275,723 ±     1055,315  ops/min
SquareMultiply.execute              200     ojAlgo  thrpt   15       40537,583 ±      393,264  ops/min
SquareMultiply.execute              500       EJML  thrpt   15         492,080 ±        2,428  ops/min
SquareMultiply.execute              500        MTJ  thrpt   15       11098,306 ±      153,936  ops/min
SquareMultiply.execute              500     ojAlgo  thrpt   15        2838,004 ±      125,937  ops/min
SquareMultiply.execute             1000       EJML  thrpt   15          50,340 ±        3,281  ops/min
SquareMultiply.execute             1000        MTJ  thrpt   15        1598,225 ±      117,480  ops/min
SquareMultiply.execute             1000     ojAlgo  thrpt   15         418,851 ±       16,176  ops/min
 * </pre>
 *
 * MacBook Air: 2015-06-18
 *
 * <pre>
 * # Run complete. Total time: 00:45:32
 *
 * Benchmark               (dim)  (library)   Mode  Cnt           Score           Error    Units
 * SquareMultiply.execute      2       EJML  thrpt    3  1195467779,203 ± 226472548,806  ops/min
 * SquareMultiply.execute      2        MTJ  thrpt    3    68093507,462 ±  28552442,060  ops/min
 * SquareMultiply.execute      2     ojAlgo  thrpt    3   791236224,141 ±  74420268,703  ops/min
 * SquareMultiply.execute      3       EJML  thrpt    3   597660733,292 ± 210147150,484  ops/min
 * SquareMultiply.execute      3        MTJ  thrpt    3    59198262,305 ±   8731345,406  ops/min
 * SquareMultiply.execute      3     ojAlgo  thrpt    3   635231267,538 ±  41113421,087  ops/min
 * SquareMultiply.execute      4       EJML  thrpt    3   320325320,379 ± 162160440,911  ops/min
 * SquareMultiply.execute      4        MTJ  thrpt    3    66918434,519 ±  15460325,103  ops/min
 * SquareMultiply.execute      4     ojAlgo  thrpt    3   502872013,149 ± 246595742,019  ops/min
 * SquareMultiply.execute      5       EJML  thrpt    3   229686848,134 ± 276451890,246  ops/min
 * SquareMultiply.execute      5        MTJ  thrpt    3    60015171,918 ±  30914476,116  ops/min
 * SquareMultiply.execute      5     ojAlgo  thrpt    3   337204180,362 ± 241545524,599  ops/min
 * SquareMultiply.execute     10       EJML  thrpt    3    43385012,345 ±  19612952,742  ops/min
 * SquareMultiply.execute     10        MTJ  thrpt    3    43092775,029 ±   4934355,681  ops/min
 * SquareMultiply.execute     10     ojAlgo  thrpt    3    62519765,560 ±   4420042,551  ops/min
 * SquareMultiply.execute     20       EJML  thrpt    3     5408286,250 ±   1521352,504  ops/min
 * SquareMultiply.execute     20        MTJ  thrpt    3    13886250,404 ±  14548216,378  ops/min
 * SquareMultiply.execute     20     ojAlgo  thrpt    3     5964922,895 ±    837405,145  ops/min
 * SquareMultiply.execute     50       EJML  thrpt    3      420594,691 ±     93068,640  ops/min
 * SquareMultiply.execute     50        MTJ  thrpt    3     2476871,010 ±   1462376,949  ops/min
 * SquareMultiply.execute     50     ojAlgo  thrpt    3      417283,841 ±     90941,821  ops/min
 * SquareMultiply.execute    100       EJML  thrpt    3       56811,214 ±     38292,556  ops/min
 * SquareMultiply.execute    100        MTJ  thrpt    3      379821,916 ±     72654,788  ops/min
 * SquareMultiply.execute    100     ojAlgo  thrpt    3       90464,045 ±     24162,344  ops/min
 * SquareMultiply.execute    200       EJML  thrpt    3        8181,786 ±      6394,668  ops/min
 * SquareMultiply.execute    200        MTJ  thrpt    3       68437,831 ±     51847,412  ops/min
 * SquareMultiply.execute    200     ojAlgo  thrpt    3       12869,488 ±      4879,337  ops/min
 * SquareMultiply.execute    500       EJML  thrpt    3         544,871 ±       278,519  ops/min
 * SquareMultiply.execute    500        MTJ  thrpt    3        5140,720 ±       720,539  ops/min
 * SquareMultiply.execute    500     ojAlgo  thrpt    3         996,341 ±       318,266  ops/min
 * SquareMultiply.execute   1000       EJML  thrpt    3          56,580 ±        21,000  ops/min
 * SquareMultiply.execute   1000        MTJ  thrpt    3         572,412 ±      2156,684  ops/min
 * SquareMultiply.execute   1000     ojAlgo  thrpt    3         109,272 ±        41,031  ops/min
 * SquareMultiply.execute   2000       EJML  thrpt    3           7,106 ±         2,328  ops/min
 * SquareMultiply.execute   2000        MTJ  thrpt    3          87,722 ±       134,475  ops/min
 * SquareMultiply.execute   2000     ojAlgo  thrpt    3          13,237 ±         5,812  ops/min
 * SquareMultiply.execute   5000       EJML  thrpt    3           0,449 ±         0,068  ops/min
 * SquareMultiply.execute   5000        MTJ  thrpt    3           6,140 ±         0,781  ops/min
 * SquareMultiply.execute   5000     ojAlgo  thrpt    3           0,845 ±         0,024  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class SquareMultiply extends LinearAlgebraBenchmark {

	public static void main(final String[] args) throws RunnerException {
		LinearAlgebraBenchmark.run(SquareMultiply.class);
	}

	@Param({ "100", "1000", "10000" })
	public int dim;

	Object left;

	@Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
	public String library;

	private BenchmarkContestant<?>.MatrixMultiplier myPlainMultiplier;
	Object righ;

	@Override
	@Benchmark
	public Object execute() {
		return myPlainMultiplier.multiply(left, righ);
	}

	@Setup
	public void setup() {

		contestant = BenchmarkContestant.CONTESTANTS.get(library);

		myPlainMultiplier = contestant.getMatrixMultiplier();

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

		this.verifyStateless(myPlainMultiplier.getClass());

	}

}
