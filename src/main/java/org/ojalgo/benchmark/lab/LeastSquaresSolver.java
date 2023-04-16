/*
 * Copyright 1997-2023 Optimatika (www.optimatika.se)
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
 * MacBook Pro (16-inch, 2019): 2022-01-14
 *
 * <pre>
Benchmark                   (dim)   (lib)   Mode  Cnt         Score         Error    Units
LeastSquaresSolver.execute     10     ACM  thrpt    3  14147224.620 ± 1017690.248  ops/min
LeastSquaresSolver.execute     10    EJML  thrpt    3  26312772.417 ± 1420470.851  ops/min
LeastSquaresSolver.execute     10  ojAlgo  thrpt    3   8632904.424 ± 1635064.050  ops/min
LeastSquaresSolver.execute     10     MTJ  thrpt    3  11289497.149 ± 1355003.022  ops/min
LeastSquaresSolver.execute     16     ACM  thrpt    3   5593856.395 ± 1630200.785  ops/min
LeastSquaresSolver.execute     16    EJML  thrpt    3   9783792.213 ± 2445552.143  ops/min
LeastSquaresSolver.execute     16  ojAlgo  thrpt    3   3553651.924 ±  302433.307  ops/min
LeastSquaresSolver.execute     16     MTJ  thrpt    3   6795619.670 ± 1031563.975  ops/min
LeastSquaresSolver.execute     20     ACM  thrpt    3   3550160.960 ±  835630.008  ops/min
LeastSquaresSolver.execute     20    EJML  thrpt    3   5599693.518 ±  625965.378  ops/min
LeastSquaresSolver.execute     20  ojAlgo  thrpt    3   2106863.092 ±  291629.127  ops/min
LeastSquaresSolver.execute     20     MTJ  thrpt    3   5042197.010 ±  610886.527  ops/min
LeastSquaresSolver.execute     32     ACM  thrpt    3   1022704.527 ±  253119.435  ops/min
LeastSquaresSolver.execute     32    EJML  thrpt    3   1487795.086 ±  199035.170  ops/min
LeastSquaresSolver.execute     32  ojAlgo  thrpt    3    552000.200 ±   82871.448  ops/min
LeastSquaresSolver.execute     32     MTJ  thrpt    3   2214962.452 ±  358530.812  ops/min
LeastSquaresSolver.execute     50     ACM  thrpt    3    325743.095 ±   15955.022  ops/min
LeastSquaresSolver.execute     50    EJML  thrpt    3    380686.456 ±    1461.407  ops/min
LeastSquaresSolver.execute     50  ojAlgo  thrpt    3    282929.026 ±  187622.822  ops/min
LeastSquaresSolver.execute     50     MTJ  thrpt    3   1005124.372 ±  105401.923  ops/min
LeastSquaresSolver.execute     64     ACM  thrpt    3    175941.150 ±    3675.162  ops/min
LeastSquaresSolver.execute     64    EJML  thrpt    3    201712.282 ±    8882.430  ops/min
LeastSquaresSolver.execute     64  ojAlgo  thrpt    3    173618.861 ±   15020.640  ops/min
LeastSquaresSolver.execute     64     MTJ  thrpt    3    553564.597 ±   19183.910  ops/min
LeastSquaresSolver.execute    100     ACM  thrpt    3     53322.927 ±    3064.429  ops/min
LeastSquaresSolver.execute    100    EJML  thrpt    3     57322.787 ±    1928.148  ops/min
LeastSquaresSolver.execute    100  ojAlgo  thrpt    3     61621.677 ±    3832.334  ops/min
LeastSquaresSolver.execute    100     MTJ  thrpt    3    205836.811 ±    3005.810  ops/min
LeastSquaresSolver.execute    128     ACM  thrpt    3     26574.120 ±    2115.893  ops/min
LeastSquaresSolver.execute    128    EJML  thrpt    3     28108.216 ±    1818.621  ops/min
LeastSquaresSolver.execute    128  ojAlgo  thrpt    3     34211.704 ±    1934.280  ops/min
LeastSquaresSolver.execute    128     MTJ  thrpt    3     65906.145 ±    4831.794  ops/min
LeastSquaresSolver.execute    200     ACM  thrpt    3      7355.808 ±     209.057  ops/min
LeastSquaresSolver.execute    200    EJML  thrpt    3      7692.234 ±     387.152  ops/min
LeastSquaresSolver.execute    200  ojAlgo  thrpt    3     10738.984 ±     711.649  ops/min
LeastSquaresSolver.execute    200     MTJ  thrpt    3     27500.258 ±    2694.902  ops/min
LeastSquaresSolver.execute    256     ACM  thrpt    3      3577.491 ±    1814.157  ops/min
LeastSquaresSolver.execute    256    EJML  thrpt    3      3466.631 ±    1076.163  ops/min
LeastSquaresSolver.execute    256  ojAlgo  thrpt    3      5520.001 ±     191.128  ops/min
LeastSquaresSolver.execute    256     MTJ  thrpt    3     14046.921 ±     422.035  ops/min
LeastSquaresSolver.execute    500     ACM  thrpt    3       505.182 ±      82.514  ops/min
LeastSquaresSolver.execute    500    EJML  thrpt    3       511.017 ±      70.980  ops/min
LeastSquaresSolver.execute    500  ojAlgo  thrpt    3      1341.534 ±      32.798  ops/min
LeastSquaresSolver.execute    500     MTJ  thrpt    3      4195.065 ±     119.358  ops/min
LeastSquaresSolver.execute    512     ACM  thrpt    3       422.181 ±      32.975  ops/min
LeastSquaresSolver.execute    512    EJML  thrpt    3       420.296 ±      64.136  ops/min
LeastSquaresSolver.execute    512  ojAlgo  thrpt    3      1159.276 ±      55.956  ops/min
LeastSquaresSolver.execute    512     MTJ  thrpt    3      3449.016 ±    2660.783  ops/min
LeastSquaresSolver.execute   1000     ACM  thrpt    3        62.527 ±       3.489  ops/min
LeastSquaresSolver.execute   1000    EJML  thrpt    3        63.301 ±       6.679  ops/min
LeastSquaresSolver.execute   1000  ojAlgo  thrpt    3       248.216 ±      60.196  ops/min
LeastSquaresSolver.execute   1000     MTJ  thrpt    3       880.430 ±      23.485  ops/min
 * </pre>
 *
 * Mac Pro 2018-02-23
 *
 * <pre>
# Run complete. Total time: 00:20:26

Benchmark                   (dim)   (lib)   Mode  Cnt        Score        Error    Units
LeastSquaresSolver.execute     10     ACM  thrpt    3  5200707.434 ±  62665.368  ops/min
LeastSquaresSolver.execute     10    EJML  thrpt    3  9173323.292 ± 280396.776  ops/min
LeastSquaresSolver.execute     10  ojAlgo  thrpt    3  5793229.586 ± 648123.702  ops/min
LeastSquaresSolver.execute     10     MTJ  thrpt    3  5213698.794 ±  90154.483  ops/min
LeastSquaresSolver.execute    100     ACM  thrpt    3    31520.051 ±    214.258  ops/min
LeastSquaresSolver.execute    100    EJML  thrpt    3    31236.966 ±     37.154  ops/min
LeastSquaresSolver.execute    100  ojAlgo  thrpt    3    25226.972 ±   3038.485  ops/min
LeastSquaresSolver.execute    100     MTJ  thrpt    3    56666.168 ±    819.864  ops/min
LeastSquaresSolver.execute   1000     ACM  thrpt    3       33.696 ±     10.208  ops/min
LeastSquaresSolver.execute   1000    EJML  thrpt    3       34.215 ±      0.509  ops/min
LeastSquaresSolver.execute   1000  ojAlgo  thrpt    3       97.905 ±      2.333  ops/min
LeastSquaresSolver.execute   1000     MTJ  thrpt    3      314.707 ±     12.122  ops/min
 * </pre>
 *
 * MacBook Air: 2015-06-18
 *
 * <pre>
 * # Run complete. Total time: 00:04:54
 *
 * Benchmark                  (dim)  (library)   Mode  Cnt          Score          Error    Units
 * LeastSquaresSolve.execute      2       EJML  thrpt    3   88958538,938 ± 17867459,758  ops/min
 * LeastSquaresSolve.execute      2        MTJ  thrpt    3   12851756,128 ±  3943084,610  ops/min
 * LeastSquaresSolve.execute      2     ojAlgo  thrpt    3  132859369,825 ± 14587940,282  ops/min
 * LeastSquaresSolve.execute      3       EJML  thrpt    3   55793970,332 ± 10897984,929  ops/min
 * LeastSquaresSolve.execute      3        MTJ  thrpt    3   11389993,843 ±  3421040,780  ops/min
 * LeastSquaresSolve.execute      3     ojAlgo  thrpt    3   95650882,670 ± 25051896,599  ops/min
 * LeastSquaresSolve.execute      4       EJML  thrpt    3   38847953,493 ± 10209436,813  ops/min
 * LeastSquaresSolve.execute      4        MTJ  thrpt    3   10843210,998 ±   438731,529  ops/min
 * LeastSquaresSolve.execute      4     ojAlgo  thrpt    3   89450809,045 ± 31839374,841  ops/min
 * LeastSquaresSolve.execute      5       EJML  thrpt    3   27650741,713 ± 14773924,768  ops/min
 * LeastSquaresSolve.execute      5        MTJ  thrpt    3   10209235,293 ±  2034715,766  ops/min
 * LeastSquaresSolve.execute      5     ojAlgo  thrpt    3   49795182,180 ± 20101456,774  ops/min
 * LeastSquaresSolve.execute     10       EJML  thrpt    3    8386144,646 ±  6572263,508  ops/min
 * LeastSquaresSolve.execute     10        MTJ  thrpt    3    6429485,417 ±  1323743,182  ops/min
 * LeastSquaresSolve.execute     10     ojAlgo  thrpt    3    8071302,306 ±  1664463,036  ops/min
 * LeastSquaresSolve.execute     20       EJML  thrpt    3    1896344,389 ±  1570987,119  ops/min
 * LeastSquaresSolve.execute     20        MTJ  thrpt    3    2954283,613 ±   179596,950  ops/min
 * LeastSquaresSolve.execute     20     ojAlgo  thrpt    3    1696888,389 ±  1484014,992  ops/min
 * LeastSquaresSolve.execute     50       EJML  thrpt    3     198122,640 ±   118859,299  ops/min
 * LeastSquaresSolve.execute     50        MTJ  thrpt    3     407548,559 ±   310111,945  ops/min
 * LeastSquaresSolve.execute     50     ojAlgo  thrpt    3     187992,582 ±    36233,036  ops/min
 * LeastSquaresSolve.execute    100       EJML  thrpt    3      31056,692 ±     2941,996  ops/min
 * LeastSquaresSolve.execute    100        MTJ  thrpt    3      67250,023 ±    30124,534  ops/min
 * LeastSquaresSolve.execute    100     ojAlgo  thrpt    3      30438,440 ±    10987,816  ops/min
 * LeastSquaresSolve.execute    200       EJML  thrpt    3       4590,459 ±     3595,651  ops/min
 * LeastSquaresSolve.execute    200        MTJ  thrpt    3      13181,946 ±     2832,535  ops/min
 * LeastSquaresSolve.execute    200     ojAlgo  thrpt    3       3646,481 ±     3106,179  ops/min
 * LeastSquaresSolve.execute    500       EJML  thrpt    3        286,467 ±       60,274  ops/min
 * LeastSquaresSolve.execute    500        MTJ  thrpt    3       1310,167 ±      110,984  ops/min
 * LeastSquaresSolve.execute    500     ojAlgo  thrpt    3        317,760 ±       50,871  ops/min
 * LeastSquaresSolve.execute   1000       EJML  thrpt    3         35,342 ±       11,430  ops/min
 * LeastSquaresSolve.execute   1000        MTJ  thrpt    3        201,143 ±       51,818  ops/min
 * LeastSquaresSolve.execute   1000     ojAlgo  thrpt    3         43,572 ±       15,780  ops/min
 * </pre>
 *
 * MacBook Pro: 2018-02-20
 *
 * <pre>
# Run complete. Total time: 00:20:29

Benchmark                   (dim)   (lib)   Mode  Cnt         Score         Error    Units
LeastSquaresSolver.execute     10     ACM  thrpt    3  10193343.006 ± 1860170.160  ops/min
LeastSquaresSolver.execute     10    EJML  thrpt    3  22726883.142 ± 4139184.314  ops/min
LeastSquaresSolver.execute     10  ojAlgo  thrpt    3  11999487.220 ±  467230.953  ops/min
LeastSquaresSolver.execute     10     MTJ  thrpt    3  11766346.823 ±  682688.431  ops/min
LeastSquaresSolver.execute    100     ACM  thrpt    3     41233.337 ±   19916.044  ops/min
LeastSquaresSolver.execute    100    EJML  thrpt    3     44212.043 ±    3905.765  ops/min
LeastSquaresSolver.execute    100  ojAlgo  thrpt    3     36916.294 ±    4893.924  ops/min
LeastSquaresSolver.execute    100     MTJ  thrpt    3    144316.599 ±   18955.382  ops/min
LeastSquaresSolver.execute   1000     ACM  thrpt    3        48.256 ±       9.100  ops/min
LeastSquaresSolver.execute   1000    EJML  thrpt    3        48.372 ±      12.252  ops/min
LeastSquaresSolver.execute   1000  ojAlgo  thrpt    3       103.000 ±      37.009  ops/min
LeastSquaresSolver.execute   1000     MTJ  thrpt    3       648.200 ±      82.129  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class LeastSquaresSolver extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(LeastSquaresSolver.class);
    }

    @Param({ "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000" })
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

        myOperation = library.getOperationEquationSystemSolver(dim + dim, dim, 1, false);

        body = MatrixBenchmarkOperation.makeRandom(dim + dim, dim, library);
        rhs = MatrixBenchmarkOperation.makeRandom(dim + dim, 1, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myLeastSquaresSolver.getClass());

    }

}
