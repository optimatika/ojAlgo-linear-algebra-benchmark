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
 * MacBook Pro (16-inch, 2019): 2022-01-05
 *
 * <pre>
Benchmark               (dim)   (lib)   Mode  Cnt          Score          Error    Units
HermitianSolve.execute     10     ACM  thrpt    3   37228122.022 ± 12540125.916  ops/min
HermitianSolve.execute     10    EJML  thrpt    3  121899947.466 ± 16566274.652  ops/min
HermitianSolve.execute     10  ojAlgo  thrpt    3   99154557.349 ±  5852466.585  ops/min
HermitianSolve.execute     10     MTJ  thrpt    3   41069190.998 ±  3216365.685  ops/min
HermitianSolve.execute     16     ACM  thrpt    3   18611901.215 ±  3342461.299  ops/min
HermitianSolve.execute     16    EJML  thrpt    3   48952914.624 ±  9490781.604  ops/min
HermitianSolve.execute     16  ojAlgo  thrpt    3   43554976.874 ±  6112712.904  ops/min
HermitianSolve.execute     16     MTJ  thrpt    3   27522347.855 ±  3872241.492  ops/min
HermitianSolve.execute     20     ACM  thrpt    3   12528532.278 ±  2217311.236  ops/min
HermitianSolve.execute     20    EJML  thrpt    3   31258539.676 ±  2448487.998  ops/min
HermitianSolve.execute     20  ojAlgo  thrpt    3   30137432.453 ±  2140066.548  ops/min
HermitianSolve.execute     20     MTJ  thrpt    3   19794612.087 ±  4346780.938  ops/min
HermitianSolve.execute     32     ACM  thrpt    3    5217976.997 ±   518098.923  ops/min
HermitianSolve.execute     32    EJML  thrpt    3   10596730.005 ±  1268701.898  ops/min
HermitianSolve.execute     32  ojAlgo  thrpt    3   10618554.769 ±   674626.692  ops/min
HermitianSolve.execute     32     MTJ  thrpt    3    9143209.963 ±  3114142.456  ops/min
HermitianSolve.execute     50     ACM  thrpt    3    2058633.658 ±   242064.172  ops/min
HermitianSolve.execute     50    EJML  thrpt    3    3231353.840 ±   979559.724  ops/min
HermitianSolve.execute     50  ojAlgo  thrpt    3    1562708.373 ±   451208.806  ops/min
HermitianSolve.execute     50     MTJ  thrpt    3    1201019.331 ±    76854.572  ops/min
HermitianSolve.execute     64     ACM  thrpt    3    1152604.789 ±   270520.453  ops/min
HermitianSolve.execute     64    EJML  thrpt    3    1637289.276 ±   110864.731  ops/min
HermitianSolve.execute     64  ojAlgo  thrpt    3     916150.712 ±   257476.229  ops/min
HermitianSolve.execute     64     MTJ  thrpt    3     853996.015 ±    69569.993  ops/min
HermitianSolve.execute    100     ACM  thrpt    3     382714.447 ±    58494.567  ops/min
HermitianSolve.execute    100    EJML  thrpt    3     440987.996 ±   255030.615  ops/min
HermitianSolve.execute    100  ojAlgo  thrpt    3     310420.513 ±    14299.699  ops/min
HermitianSolve.execute    100     MTJ  thrpt    3     402865.026 ±   265548.068  ops/min
HermitianSolve.execute    128     ACM  thrpt    3     202819.073 ±    34839.641  ops/min
HermitianSolve.execute    128    EJML  thrpt    3     215201.048 ±    54570.756  ops/min
HermitianSolve.execute    128  ojAlgo  thrpt    3     165534.920 ±    22765.593  ops/min
HermitianSolve.execute    128     MTJ  thrpt    3     240948.972 ±   215358.342  ops/min
HermitianSolve.execute    200     ACM  thrpt    3      57310.030 ±    14997.123  ops/min
HermitianSolve.execute    200    EJML  thrpt    3      50663.827 ±    41538.868  ops/min
HermitianSolve.execute    200  ojAlgo  thrpt    3      28818.534 ±     6034.703  ops/min
HermitianSolve.execute    200     MTJ  thrpt    3     115365.665 ±    34351.239  ops/min
HermitianSolve.execute    256     ACM  thrpt    3      24623.794 ±    40684.887  ops/min
HermitianSolve.execute    256    EJML  thrpt    3      16378.876 ±    16054.675  ops/min
HermitianSolve.execute    256  ojAlgo  thrpt    3      15161.582 ±    12611.531  ops/min
HermitianSolve.execute    256     MTJ  thrpt    3     104270.195 ±    22907.243  ops/min
HermitianSolve.execute    500     ACM  thrpt    3       4178.786 ±      215.251  ops/min
HermitianSolve.execute    500    EJML  thrpt    3       2857.038 ±     1978.064  ops/min
HermitianSolve.execute    500  ojAlgo  thrpt    3       2792.678 ±      860.732  ops/min
HermitianSolve.execute    500     MTJ  thrpt    3      29257.879 ±     5890.703  ops/min
HermitianSolve.execute    512     ACM  thrpt    3       3817.950 ±     2571.387  ops/min
HermitianSolve.execute    512    EJML  thrpt    3       2513.068 ±      140.563  ops/min
HermitianSolve.execute    512  ojAlgo  thrpt    3       2645.440 ±     2032.684  ops/min
HermitianSolve.execute    512     MTJ  thrpt    3      24126.886 ±    28080.750  ops/min
HermitianSolve.execute   1000     ACM  thrpt    3        534.416 ±       11.359  ops/min
HermitianSolve.execute   1000    EJML  thrpt    3        453.626 ±      138.907  ops/min
HermitianSolve.execute   1000  ojAlgo  thrpt    3        635.535 ±      329.251  ops/min
HermitianSolve.execute   1000     MTJ  thrpt    3       6496.599 ±     3081.295  ops/min
 * </pre>
 *
 * Mac Pro: 2018-02-23
 *
 * <pre>
# Run complete. Total time: 00:20:11

Benchmark               (dim)   (lib)   Mode  Cnt         Score         Error    Units
HermitianSolve.execute     10     ACM  thrpt    3  15232248.591 ±  336213.938  ops/min
HermitianSolve.execute     10    EJML  thrpt    3  54329954.965 ± 2398886.643  ops/min
HermitianSolve.execute     10  ojAlgo  thrpt    3  50836618.152 ±  777386.197  ops/min
HermitianSolve.execute     10     MTJ  thrpt    3  26529812.710 ± 1050002.364  ops/min
HermitianSolve.execute    100     ACM  thrpt    3    153682.126 ±   42031.507  ops/min
HermitianSolve.execute    100    EJML  thrpt    3    274902.489 ±   78191.974  ops/min
HermitianSolve.execute    100  ojAlgo  thrpt    3    167983.043 ±   12615.147  ops/min
HermitianSolve.execute    100     MTJ  thrpt    3    172111.200 ±   47630.643  ops/min
HermitianSolve.execute   1000     ACM  thrpt    3       233.557 ±       3.475  ops/min
HermitianSolve.execute   1000    EJML  thrpt    3       279.627 ±       2.176  ops/min
HermitianSolve.execute   1000  ojAlgo  thrpt    3       221.878 ±     133.507  ops/min
HermitianSolve.execute   1000     MTJ  thrpt    3      2599.969 ±      29.084  ops/min
 * </pre>
 *
 * MacBook Pro: 2018-02-23
 *
 * <pre>
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class HermitianSolve extends MatrixBenchmarkOperation {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(HermitianSolve.class);
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

        myOperation = library.getOperationEquationSystemSolver(dim, dim, 1, true);

        body = MatrixBenchmarkOperation.makeSPD(dim, library);
        rhs = MatrixBenchmarkOperation.makeRandom(dim, 1, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myHermitianSolver.getClass());

    }

}
