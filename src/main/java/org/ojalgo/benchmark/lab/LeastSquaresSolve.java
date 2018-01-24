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
package org.ojalgo.benchmark.lab;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
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
 * @author apete
 */
@State(Scope.Benchmark)
public class LeastSquaresSolve extends MatrixBenchmarkOperation {

    @FunctionalInterface
    public static interface TaskDefinition<T> {

        int doThThing();

    }

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(LeastSquaresSolve.class);
    }

    Object body;
    @Param({ "2", "3", "4", "5"/*
                                * , "10", "20", "50", "100", "200", "500", "1000" , "2000", "5000", "10000"
                                */ })
    public int dim;

    @Param({ "EJML", "MTJ", "ojAlgo" })
    public String library;

    private MatrixBenchmarkLibrary<?, ?>.LeastSquaresSolver myLeastSquaresSolver;
    Object rhs;

    @Override
    @Benchmark
    public Object execute() {
        return myLeastSquaresSolver.solve(body, rhs);
    }

    @Setup
    public void setup() {

        contestant = MatrixBenchmarkLibrary.LIBRARIES.get(library);

        myLeastSquaresSolver = contestant.getLeastSquaresSolver();

        body = this.makeRandom(dim + dim, dim, contestant);
        rhs = this.makeRandom(dim + dim, 1, contestant);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        this.verifyStateless(myLeastSquaresSolver.getClass());

    }

}
