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
 * Mac Pro 2015-06-13
 *
 * <pre>
 * </pre>
 *
 * MacBook Air: 2015-06-20
 *
 * <pre>
 * # Run complete. Total time: 00:21:28
 * # Run complete. Total time: 00:05:48
 *
 * Benchmark             (dim)  (library)   Mode  Cnt          Score          Error    Units
 * GeneralSolve.execute      2       EJML  thrpt    3  280567213,034 ±  9825748,921  ops/min
 * GeneralSolve.execute      2        MTJ  thrpt    3   50988636,684 ±   842138,821  ops/min
 * GeneralSolve.execute      2     ojAlgo  thrpt    3  819463736,003 ± 23455594,991  ops/min
 * GeneralSolve.execute      3       EJML  thrpt    3  182301335,447 ±  8438016,403  ops/min
 * GeneralSolve.execute      3        MTJ  thrpt    3   47645285,757 ±  1844500,532  ops/min
 * GeneralSolve.execute      3     ojAlgo  thrpt    3  648684736,296 ± 13136659,256  ops/min
 * GeneralSolve.execute      4       EJML  thrpt    3  125607243,151 ±  1375349,254  ops/min
 * GeneralSolve.execute      4        MTJ  thrpt    3   36662896,034 ±  1652967,275  ops/min
 * GeneralSolve.execute      4     ojAlgo  thrpt    3  399898444,173 ±  6692603,328  ops/min
 * GeneralSolve.execute      5       EJML  thrpt    3   86862292,481 ±  2952506,385  ops/min
 * GeneralSolve.execute      5        MTJ  thrpt    3   38117657,720 ±  1221614,477  ops/min
 * GeneralSolve.execute      5     ojAlgo  thrpt    3  188904998,288 ±  3674426,372  ops/min
 * GeneralSolve.execute     10       EJML  thrpt    3   20920972,436 ±   535815,287  ops/min
 * GeneralSolve.execute     10        MTJ  thrpt    3   23025657,598 ±   808556,909  ops/min
 * GeneralSolve.execute     10     ojAlgo  thrpt    3   14458037,748 ±   416863,270  ops/min
 * GeneralSolve.execute     20       EJML  thrpt    3    5550725,682 ±   173697,696  ops/min
 * GeneralSolve.execute     20        MTJ  thrpt    3    8257285,788 ±   279863,082  ops/min
 * GeneralSolve.execute     20     ojAlgo  thrpt    3    5918894,494 ±   130443,755  ops/min
 * GeneralSolve.execute     50       EJML  thrpt    3     667189,780 ±     7388,606  ops/min
 * GeneralSolve.execute     50        MTJ  thrpt    3     559616,648 ±   148744,511  ops/min
 * GeneralSolve.execute     50     ojAlgo  thrpt    3     701918,144 ±  1246113,629  ops/min
 * GeneralSolve.execute    100       EJML  thrpt    3     106893,121 ±     1336,645  ops/min
 * GeneralSolve.execute    100        MTJ  thrpt    3     172825,390 ±     6017,845  ops/min
 * GeneralSolve.execute    100     ojAlgo  thrpt    3      92974,713 ±     1924,464  ops/min
 * GeneralSolve.execute    200       EJML  thrpt    3      15725,831 ±      231,395  ops/min
 * GeneralSolve.execute    200        MTJ  thrpt    3      59455,006 ±     6505,710  ops/min
 * GeneralSolve.execute    200     ojAlgo  thrpt    3      15612,629 ±      140,819  ops/min
 * GeneralSolve.execute    500       EJML  thrpt    3       1070,363 ±       23,451  ops/min
 * GeneralSolve.execute    500        MTJ  thrpt    3      13677,728 ±      334,892  ops/min
 * GeneralSolve.execute    500     ojAlgo  thrpt    3       1247,596 ±      136,732  ops/min
 * GeneralSolve.execute   1000       EJML  thrpt    3        133,059 ±        4,878  ops/min
 * GeneralSolve.execute   1000        MTJ  thrpt    3       2362,280 ±      252,064  ops/min
 * GeneralSolve.execute   1000     ojAlgo  thrpt    3        218,230 ±       38,927  ops/min
 * GeneralSolve.execute   2000       EJML  thrpt    3         12,739 ±        2,576  ops/min
 * GeneralSolve.execute   2000        MTJ  thrpt    3        361,548 ±       52,815  ops/min
 * GeneralSolve.execute   2000     ojAlgo  thrpt    3         33,562 ±        2,747  ops/min
 * GeneralSolve.execute   5000       EJML  thrpt    3          0,926 ±         0,096  ops/min
 * GeneralSolve.execute   5000        MTJ  thrpt    3         14,396 ±        42,896  ops/min
 * GeneralSolve.execute   5000     ojAlgo  thrpt    3          1,291 ±         0,885  ops/min
 * </pre>
 *
 * <pre>
# Run complete. Total time: 00:21:38

Benchmark             (dim)  (library)   Mode  Cnt        Score        Error  Units
GeneralSolve.execute      2       EJML  thrpt   15  3410506.198 ± 641845.297  ops/s
GeneralSolve.execute      2        MTJ  thrpt   15   798048.227 ±  45644.832  ops/s
GeneralSolve.execute      2     ojAlgo  thrpt   15   832432.122 ± 490097.905  ops/s
GeneralSolve.execute      3       EJML  thrpt   15  2921243.676 ± 118524.511  ops/s
GeneralSolve.execute      3        MTJ  thrpt   15   727207.215 ±  61611.909  ops/s
GeneralSolve.execute      3     ojAlgo  thrpt   15  1154330.749 ± 310305.970  ops/s
GeneralSolve.execute      4       EJML  thrpt   15  2006885.062 ±  86288.673  ops/s
GeneralSolve.execute      4        MTJ  thrpt   15   710126.079 ±  28885.838  ops/s
GeneralSolve.execute      4     ojAlgo  thrpt   15  1071106.216 ± 174845.439  ops/s
GeneralSolve.execute      5       EJML  thrpt   15  1455480.894 ±  44142.695  ops/s
GeneralSolve.execute      5        MTJ  thrpt   15   641621.654 ±  17276.841  ops/s
GeneralSolve.execute      5     ojAlgo  thrpt   15   936908.062 ± 149175.065  ops/s
GeneralSolve.execute     10       EJML  thrpt   15   421945.061 ±   9190.756  ops/s
GeneralSolve.execute     10        MTJ  thrpt   15   390028.094 ±   9646.151  ops/s
GeneralSolve.execute     10     ojAlgo  thrpt   15   233593.476 ±  31589.210  ops/s
GeneralSolve.execute     20       EJML  thrpt   15   104109.090 ±   1881.241  ops/s
GeneralSolve.execute     20        MTJ  thrpt   15   162965.594 ±   3237.957  ops/s
GeneralSolve.execute     20     ojAlgo  thrpt   15    64531.686 ±  12413.451  ops/s
GeneralSolve.execute     50       EJML  thrpt   15    12042.858 ±    251.446  ops/s
GeneralSolve.execute     50        MTJ  thrpt   15     6722.021 ±     81.784  ops/s
GeneralSolve.execute     50     ojAlgo  thrpt   15    10134.773 ±   1619.518  ops/s
GeneralSolve.execute    100       EJML  thrpt   15     1846.891 ±     29.520  ops/s
GeneralSolve.execute    100        MTJ  thrpt   15     3050.135 ±     82.688  ops/s
GeneralSolve.execute    100     ojAlgo  thrpt   15     1788.800 ±     99.156  ops/s
GeneralSolve.execute    200       EJML  thrpt   15      255.580 ±      4.062  ops/s
GeneralSolve.execute    200        MTJ  thrpt   15     1218.059 ±      8.582  ops/s
GeneralSolve.execute    200     ojAlgo  thrpt   15      299.805 ±      5.614  ops/s
GeneralSolve.execute    500       EJML  thrpt   15       16.714 ±      0.295  ops/s
GeneralSolve.execute    500        MTJ  thrpt   15      179.497 ±      6.043  ops/s
GeneralSolve.execute    500     ojAlgo  thrpt   15       22.942 ±      0.994  ops/s
GeneralSolve.execute   1000       EJML  thrpt   15        1.637 ±      0.021  ops/s
GeneralSolve.execute   1000        MTJ  thrpt   15       21.849 ±      2.951  ops/s
GeneralSolve.execute   1000     ojAlgo  thrpt   15        2.199 ±      0.048  ops/s
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class GeneralSolve extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(GeneralSolve.class);
    }

    @Param({ "10", "100", "200", "500" })
    public int dim;
    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

    private ProducingBinaryMatrixMatrixOperation<?, ?> myOperation;

    Object body;
    Object rhs;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(body, rhs);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        myOperation = library.getOperationEquationSystemSolver(dim, dim, 1, false);

        body = this.makeRandom(dim, dim, library);
        rhs = this.makeRandom(dim, 1, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
