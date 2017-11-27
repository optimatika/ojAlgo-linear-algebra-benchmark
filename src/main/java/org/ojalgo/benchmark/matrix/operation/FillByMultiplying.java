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
Result "org.ojalgo.benchmark.matrix.operation.FillByMultiplying.execute":
  226,860 ±(99.9%) 10,931 ops/min [Average]
  (min, avg, max) = (226,171, 226,860, 227,258), stdev = 0,599
  CI (99.9%): [215,929, 237,791] (assumes normal distribution)


# Run complete. Total time: 00:46:51

Benchmark                  (dim)  (library)   Mode  Cnt           Score           Error    Units
FillByMultiplying.execute      1        ACM  thrpt    3   130195832,553 ±  50610258,368  ops/min
FillByMultiplying.execute      1       EJML  thrpt    3  3567745521,436 ±  73164059,367  ops/min
FillByMultiplying.execute      1     ojAlgo  thrpt    3  2330664719,350 ± 178003415,027  ops/min
FillByMultiplying.execute      1        MTJ  thrpt    3    57809662,012 ±   2857348,872  ops/min
FillByMultiplying.execute      2        ACM  thrpt    3    84175478,404 ±   7382748,938  ops/min
FillByMultiplying.execute      2       EJML  thrpt    3  1341948087,160 ± 199313175,158  ops/min
FillByMultiplying.execute      2     ojAlgo  thrpt    3  2073852684,219 ±  42451497,358  ops/min
FillByMultiplying.execute      2        MTJ  thrpt    3    55968751,624 ±  12911684,175  ops/min
FillByMultiplying.execute      3        ACM  thrpt    3    60546866,100 ±  28851220,347  ops/min
FillByMultiplying.execute      3       EJML  thrpt    3   597799471,156 ±  18580426,922  ops/min
FillByMultiplying.execute      3     ojAlgo  thrpt    3  1212452366,762 ±  15728306,773  ops/min
FillByMultiplying.execute      3        MTJ  thrpt    3    51035077,114 ±  25007520,075  ops/min
FillByMultiplying.execute      4        ACM  thrpt    3    44924598,952 ±   1674854,365  ops/min
FillByMultiplying.execute      4       EJML  thrpt    3   278452788,436 ±   6461193,770  ops/min
FillByMultiplying.execute      4     ojAlgo  thrpt    3   730889977,524 ±   6893044,622  ops/min
FillByMultiplying.execute      4        MTJ  thrpt    3    55611011,428 ±   7590705,782  ops/min
FillByMultiplying.execute      5        ACM  thrpt    3    34699588,705 ±   1191620,112  ops/min
FillByMultiplying.execute      5       EJML  thrpt    3   197794544,217 ±   6448163,668  ops/min
FillByMultiplying.execute      5     ojAlgo  thrpt    3   421946772,062 ±   6595428,946  ops/min
FillByMultiplying.execute      5        MTJ  thrpt    3    48389935,417 ±  18152393,151  ops/min
FillByMultiplying.execute     10        ACM  thrpt    3    12446763,075 ±    262512,802  ops/min
FillByMultiplying.execute     10       EJML  thrpt    3    35974364,514 ±   2385307,002  ops/min
FillByMultiplying.execute     10     ojAlgo  thrpt    3    52179128,614 ±    761543,257  ops/min
FillByMultiplying.execute     10        MTJ  thrpt    3    38863389,966 ±    714186,806  ops/min
FillByMultiplying.execute     20        ACM  thrpt    3     2966439,671 ±     92275,128  ops/min
FillByMultiplying.execute     20       EJML  thrpt    3     3270871,129 ±    322986,009  ops/min
FillByMultiplying.execute     20     ojAlgo  thrpt    3     5240271,628 ±    605556,927  ops/min
FillByMultiplying.execute     20        MTJ  thrpt    3    11815616,956 ±   1167496,177  ops/min
FillByMultiplying.execute     50        ACM  thrpt    3      256733,284 ±     54185,254  ops/min
FillByMultiplying.execute     50       EJML  thrpt    3      265766,674 ±      6461,436  ops/min
FillByMultiplying.execute     50     ojAlgo  thrpt    3      567630,639 ±     40260,273  ops/min
FillByMultiplying.execute     50        MTJ  thrpt    3     1659459,258 ±     55708,319  ops/min
FillByMultiplying.execute    100        ACM  thrpt    3       29866,013 ±     15976,452  ops/min
FillByMultiplying.execute    100       EJML  thrpt    3       55895,563 ±      1702,350  ops/min
FillByMultiplying.execute    100     ojAlgo  thrpt    3      133874,177 ±     14503,649  ops/min
FillByMultiplying.execute    100        MTJ  thrpt    3      240955,716 ±      7425,468  ops/min
FillByMultiplying.execute    200        ACM  thrpt    3        2655,788 ±       169,604  ops/min
FillByMultiplying.execute    200       EJML  thrpt    3        6834,055 ±       208,417  ops/min
FillByMultiplying.execute    200     ojAlgo  thrpt    3       36004,149 ±     23019,248  ops/min
FillByMultiplying.execute    200        MTJ  thrpt    3      120139,780 ±      6117,772  ops/min
FillByMultiplying.execute    500        ACM  thrpt    3          46,177 ±         3,203  ops/min
FillByMultiplying.execute    500       EJML  thrpt    3         448,310 ±       108,075  ops/min
FillByMultiplying.execute    500     ojAlgo  thrpt    3        2701,616 ±        43,445  ops/min
FillByMultiplying.execute    500        MTJ  thrpt    3        8971,591 ±       761,164  ops/min
FillByMultiplying.execute   1000        ACM  thrpt    3           3,456 ±         0,203  ops/min
FillByMultiplying.execute   1000       EJML  thrpt    3          47,777 ±         9,075  ops/min
FillByMultiplying.execute   1000     ojAlgo  thrpt    3         263,610 ±        35,152  ops/min
FillByMultiplying.execute   1000        MTJ  thrpt    3        1316,516 ±       487,116  ops/min
FillByMultiplying.execute   2000        ACM  thrpt    3           0,308 ±         0,004  ops/min
FillByMultiplying.execute   2000       EJML  thrpt    3           5,390 ±         1,009  ops/min
FillByMultiplying.execute   2000     ojAlgo  thrpt    3          14,286 ±         1,082  ops/min
FillByMultiplying.execute   2000        MTJ  thrpt    3         226,860 ±        10,931  ops/min
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
