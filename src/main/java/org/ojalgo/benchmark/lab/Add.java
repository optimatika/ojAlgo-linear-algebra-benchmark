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
 * MacBook Pro (16-inch, 2019): 2022-01-04
 *
 * <pre>
Benchmark    (dim)   (lib)   Mode  Cnt           Score           Error    Units
Add.execute     10     ACM  thrpt    3   111273520.837 ±  14015852.292  ops/min
Add.execute     10    EJML  thrpt    3  3196131072.066 ± 215532473.495  ops/min
Add.execute     10  ojAlgo  thrpt    3  2507281426.940 ± 506520001.645  ops/min
Add.execute     10     MTJ  thrpt    3    80189773.904 ±   4197678.647  ops/min
Add.execute     16     ACM  thrpt    3    48830701.246 ±   4866974.130  ops/min
Add.execute     16    EJML  thrpt    3  1327632571.749 ±  90703414.985  ops/min
Add.execute     16  ojAlgo  thrpt    3  1711518129.324 ± 132533808.126  ops/min
Add.execute     16     MTJ  thrpt    3    31289752.999 ±   6364448.280  ops/min
Add.execute     20     ACM  thrpt    3    33860943.160 ±   6144351.773  ops/min
Add.execute     20    EJML  thrpt    3  1076308457.548 ± 192875217.967  ops/min
Add.execute     20  ojAlgo  thrpt    3  1048179111.617 ± 296630849.600  ops/min
Add.execute     20     MTJ  thrpt    3    49526446.903 ±  20846162.222  ops/min
Add.execute     32     ACM  thrpt    3    17005834.200 ±    537747.005  ops/min
Add.execute     32    EJML  thrpt    3   202658073.105 ±  21215400.974  ops/min
Add.execute     32  ojAlgo  thrpt    3   202458989.276 ±  14322643.358  ops/min
Add.execute     32     MTJ  thrpt    3    18811752.770 ±   2371455.686  ops/min
Add.execute     50     ACM  thrpt    3     7734225.109 ±   1448158.560  ops/min
Add.execute     50    EJML  thrpt    3    95044934.376 ±  14576819.925  ops/min
Add.execute     50  ojAlgo  thrpt    3    96344783.009 ±  19369107.705  ops/min
Add.execute     50     MTJ  thrpt    3     7807241.064 ±   1299974.217  ops/min
Add.execute     64     ACM  thrpt    3     5588008.827 ±    380729.102  ops/min
Add.execute     64    EJML  thrpt    3    55453374.038 ±  27956581.197  ops/min
Add.execute     64  ojAlgo  thrpt    3    48659180.065 ±  18443978.633  ops/min
Add.execute     64     MTJ  thrpt    3     4567908.740 ±    294880.876  ops/min
Add.execute    100     ACM  thrpt    3     2308669.293 ±    906468.469  ops/min
Add.execute    100    EJML  thrpt    3    22991751.959 ±   7721364.265  ops/min
Add.execute    100  ojAlgo  thrpt    3    23498722.962 ±   3250583.717  ops/min
Add.execute    100     MTJ  thrpt    3     1832335.346 ±    415949.273  ops/min
Add.execute    128     ACM  thrpt    3     1443567.272 ±    412715.078  ops/min
Add.execute    128    EJML  thrpt    3     8394731.229 ±   1292817.775  ops/min
Add.execute    128  ojAlgo  thrpt    3     8500997.767 ±   2715355.857  ops/min
Add.execute    128     MTJ  thrpt    3     1149755.102 ±    443333.291  ops/min
Add.execute    200     ACM  thrpt    3      563752.514 ±     47801.192  ops/min
Add.execute    200    EJML  thrpt    3     3304766.662 ±    505097.087  ops/min
Add.execute    200  ojAlgo  thrpt    3     3269466.028 ±   2032201.204  ops/min
Add.execute    200     MTJ  thrpt    3      452410.214 ±    403056.533  ops/min
Add.execute    256     ACM  thrpt    3      347232.981 ±    149795.730  ops/min
Add.execute    256    EJML  thrpt    3     1943090.391 ±    811651.470  ops/min
Add.execute    256  ojAlgo  thrpt    3     2017827.874 ±    629064.805  ops/min
Add.execute    256     MTJ  thrpt    3      280078.241 ±    142405.384  ops/min
Add.execute    500     ACM  thrpt    3       90340.480 ±     54096.031  ops/min
Add.execute    500    EJML  thrpt    3      288707.445 ±    263398.377  ops/min
Add.execute    500  ojAlgo  thrpt    3      243562.366 ±     16717.405  ops/min
Add.execute    500     MTJ  thrpt    3       33115.905 ±     28628.229  ops/min
Add.execute    512     ACM  thrpt    3       49445.860 ±    113895.811  ops/min
Add.execute    512    EJML  thrpt    3      512680.092 ±    229974.844  ops/min
Add.execute    512  ojAlgo  thrpt    3      691499.050 ±    154044.883  ops/min
Add.execute    512     MTJ  thrpt    3       70006.047 ±      5457.294  ops/min
Add.execute   1000     ACM  thrpt    3       20256.938 ±       932.196  ops/min
Add.execute   1000    EJML  thrpt    3       64234.568 ±      1463.778  ops/min
Add.execute   1000  ojAlgo  thrpt    3       66442.948 ±      5833.659  ops/min
Add.execute   1000     MTJ  thrpt    3       14779.312 ±      2742.627  ops/min
 * </pre>
 */
@State(Scope.Benchmark)
public class Add extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(Add.class);
    }

    @Param({ "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000" })
    public int dim;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

    private MutatingBinaryMatrixMatrixOperation<?, ?> myOperation;

    Object left;
    Object result;
    Object right;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(left, right, result);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        myOperation = library.getOperationAdd();

        left = MatrixBenchmarkOperation.makeRandom(dim, dim, library);
        right = MatrixBenchmarkOperation.makeRandom(dim, dim, library);
        result = MatrixBenchmarkOperation.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
