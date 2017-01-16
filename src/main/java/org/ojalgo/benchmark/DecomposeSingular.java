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
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Mac Pro 2015-07-01
 *
 * <pre>
# Run complete. Total time: 00:23:20

Benchmark                  (dim)  (library)   Mode  Cnt         Score         Error    Units
DecomposeSingular.execute      2       EJML  thrpt    3  32200811,380 ± 4571874,603  ops/min
DecomposeSingular.execute      2        MTJ  thrpt    3   8206077,801 ±  588636,625  ops/min
DecomposeSingular.execute      2     ojAlgo  thrpt    3  40667898,587 ±  795862,621  ops/min
DecomposeSingular.execute      3       EJML  thrpt    3  17635298,999 ±  442053,678  ops/min
DecomposeSingular.execute      3        MTJ  thrpt    3   5030349,755 ±  119993,847  ops/min
DecomposeSingular.execute      3     ojAlgo  thrpt    3  22774832,786 ±  608800,516  ops/min
DecomposeSingular.execute      4       EJML  thrpt    3   9993867,965 ±  184607,218  ops/min
DecomposeSingular.execute      4        MTJ  thrpt    3   3754987,137 ±   75170,672  ops/min
DecomposeSingular.execute      4     ojAlgo  thrpt    3  12953522,340 ±  159425,556  ops/min
DecomposeSingular.execute      5       EJML  thrpt    3   6108887,974 ±   69364,230  ops/min
DecomposeSingular.execute      5        MTJ  thrpt    3   3054350,341 ±  132306,541  ops/min
DecomposeSingular.execute      5     ojAlgo  thrpt    3   8092410,897 ±   89743,402  ops/min
DecomposeSingular.execute     10       EJML  thrpt    3   1578659,947 ±  222781,043  ops/min
DecomposeSingular.execute     10        MTJ  thrpt    3   1018093,145 ±   40143,955  ops/min
DecomposeSingular.execute     10     ojAlgo  thrpt    3   2217426,763 ±   22911,429  ops/min
DecomposeSingular.execute     20       EJML  thrpt    3    386870,234 ±   12463,108  ops/min
DecomposeSingular.execute     20        MTJ  thrpt    3    258994,549 ±   11203,620  ops/min
DecomposeSingular.execute     20     ojAlgo  thrpt    3    464825,625 ±    3213,276  ops/min
DecomposeSingular.execute     50       EJML  thrpt    3     38481,520 ±    1671,625  ops/min
DecomposeSingular.execute     50        MTJ  thrpt    3     45101,316 ±    7418,932  ops/min
DecomposeSingular.execute     50     ojAlgo  thrpt    3     52287,577 ±    1947,801  ops/min
DecomposeSingular.execute    100       EJML  thrpt    3      6484,066 ±     135,079  ops/min
DecomposeSingular.execute    100        MTJ  thrpt    3      9896,366 ±     632,632  ops/min
DecomposeSingular.execute    100     ojAlgo  thrpt    3      8427,178 ±     705,938  ops/min
DecomposeSingular.execute    200       EJML  thrpt    3       919,604 ±      24,750  ops/min
DecomposeSingular.execute    200        MTJ  thrpt    3      2309,658 ±     391,329  ops/min
DecomposeSingular.execute    200     ojAlgo  thrpt    3      1291,026 ±      23,142  ops/min
DecomposeSingular.execute    500       EJML  thrpt    3        66,288 ±       8,773  ops/min
DecomposeSingular.execute    500        MTJ  thrpt    3       294,087 ±      87,077  ops/min
DecomposeSingular.execute    500     ojAlgo  thrpt    3        96,226 ±       4,350  ops/min
DecomposeSingular.execute   1000       EJML  thrpt    3         7,989 ±       0,344  ops/min
DecomposeSingular.execute   1000        MTJ  thrpt    3        61,122 ±       4,298  ops/min
DecomposeSingular.execute   1000     ojAlgo  thrpt    3         9,538 ±       3,848  ops/min
DecomposeSingular.execute   2000       EJML  thrpt    3         0,922 ±       0,056  ops/min
DecomposeSingular.execute   2000        MTJ  thrpt    3         9,772 ±       4,025  ops/min
DecomposeSingular.execute   2000     ojAlgo  thrpt    3         1,108 ±       0,300  ops/min
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
public class DecomposeSingular extends LinearAlgebraBenchmark {

	public static void main(final String[] args) throws RunnerException {
		LinearAlgebraBenchmark.run(DecomposeSingular.class);
	}

	@Param({ "2", "3", "4", "5", "10", "20", "50", "100", "200",
			"500" /* , "1000", "2000" , "5000", "10000" */ })
	public int dim;
	@Param({ "ACM", "EJML", "MTJ", "ojAlgo" })
	public String library;

	Object matrix;

	private BenchmarkContestant<?>.SingularDecomposer myDecomposer;

	@Override
	@Benchmark
	public Object execute() {
		return myDecomposer.decompose(matrix);
	}

	@Setup
	public void setup() {

		contestant = BenchmarkContestant.CONTESTANTS.get(library);

		matrix = contestant.convert(this.makeSPD(dim));

		myDecomposer = contestant.getSingularDecomposer();
	}

	@Override
	public void verify() throws BenchmarkRequirementsException {

		this.verifyStateless(myDecomposer.getClass());

	}

}
