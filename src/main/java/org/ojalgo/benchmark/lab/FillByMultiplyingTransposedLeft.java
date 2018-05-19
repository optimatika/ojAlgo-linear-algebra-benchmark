/*
* Copyright 1997-2018 Optimatika (www.optimatika.se)
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
 * <h1>Mac Pro (Early 2009)</h1>
 * <h2>2018-05-03</h2>
 *
 * <pre>
# Run complete. Total time: 02:09:19

Benchmark                                (dim)   (lib)   Mode  Cnt           Score           Error    Units
FillByMultiplyingTransposedLeft.execute      1     ACM  thrpt    3   172940287.873 ±   4037875.071  ops/min
FillByMultiplyingTransposedLeft.execute      1    EJML  thrpt    3  3760743366.975 ±  50427039.715  ops/min
FillByMultiplyingTransposedLeft.execute      1  ojAlgo  thrpt    3  1192524735.114 ±  37674927.675  ops/min
FillByMultiplyingTransposedLeft.execute      1     MTJ  thrpt    3    69730211.138 ±   3525734.286  ops/min
FillByMultiplyingTransposedLeft.execute      2     ACM  thrpt    3   114074019.587 ±   2914205.656  ops/min
FillByMultiplyingTransposedLeft.execute      2    EJML  thrpt    3  1349290270.415 ±  58081085.882  ops/min
FillByMultiplyingTransposedLeft.execute      2  ojAlgo  thrpt    3   550860054.711 ±  38784843.733  ops/min
FillByMultiplyingTransposedLeft.execute      2     MTJ  thrpt    3    63618655.909 ±   1262910.869  ops/min
FillByMultiplyingTransposedLeft.execute      3     ACM  thrpt    3    84703153.563 ±   2567055.027  ops/min
FillByMultiplyingTransposedLeft.execute      3    EJML  thrpt    3   623048746.461 ± 131949876.354  ops/min
FillByMultiplyingTransposedLeft.execute      3  ojAlgo  thrpt    3   259703335.150 ±   2503832.433  ops/min
FillByMultiplyingTransposedLeft.execute      3     MTJ  thrpt    3    65212692.535 ±   4238543.006  ops/min
FillByMultiplyingTransposedLeft.execute      4     ACM  thrpt    3    61886409.013 ±   1036478.210  ops/min
FillByMultiplyingTransposedLeft.execute      4    EJML  thrpt    3   352048317.234 ±   4437576.276  ops/min
FillByMultiplyingTransposedLeft.execute      4  ojAlgo  thrpt    3   182998726.728 ±  12610515.321  ops/min
FillByMultiplyingTransposedLeft.execute      4     MTJ  thrpt    3    66749356.946 ±   1393515.952  ops/min
FillByMultiplyingTransposedLeft.execute      5     ACM  thrpt    3    48348113.132 ±   6780184.273  ops/min
FillByMultiplyingTransposedLeft.execute      5    EJML  thrpt    3   191250859.076 ±  11842192.328  ops/min
FillByMultiplyingTransposedLeft.execute      5  ojAlgo  thrpt    3   114341596.107 ±  41087117.389  ops/min
FillByMultiplyingTransposedLeft.execute      5     MTJ  thrpt    3    57065704.419 ±  21367741.222  ops/min
FillByMultiplyingTransposedLeft.execute      8     ACM  thrpt    3    19809346.888 ±   1937865.345  ops/min
FillByMultiplyingTransposedLeft.execute      8    EJML  thrpt    3    60703311.493 ±  10311702.664  ops/min
FillByMultiplyingTransposedLeft.execute      8  ojAlgo  thrpt    3     7595641.535 ±     32480.798  ops/min
FillByMultiplyingTransposedLeft.execute      8     MTJ  thrpt    3    52038808.707 ±   3506267.064  ops/min
FillByMultiplyingTransposedLeft.execute     10     ACM  thrpt    3    13892282.899 ±    122812.923  ops/min
FillByMultiplyingTransposedLeft.execute     10    EJML  thrpt    3    33260682.717 ±   1853883.124  ops/min
FillByMultiplyingTransposedLeft.execute     10  ojAlgo  thrpt    3     3821458.401 ±    392431.018  ops/min
FillByMultiplyingTransposedLeft.execute     10     MTJ  thrpt    3    43856721.148 ±   1337159.919  ops/min
FillByMultiplyingTransposedLeft.execute     16     ACM  thrpt    3     4992011.605 ±    514571.745  ops/min
FillByMultiplyingTransposedLeft.execute     16    EJML  thrpt    3    10655993.523 ±    331510.387  ops/min
FillByMultiplyingTransposedLeft.execute     16  ojAlgo  thrpt    3     3986282.251 ±     20201.665  ops/min
FillByMultiplyingTransposedLeft.execute     16     MTJ  thrpt    3    10949180.154 ±    124864.027  ops/min
FillByMultiplyingTransposedLeft.execute     20     ACM  thrpt    3     2904475.882 ±     64414.981  ops/min
FillByMultiplyingTransposedLeft.execute     20    EJML  thrpt    3     5864065.233 ±    179109.533  ops/min
FillByMultiplyingTransposedLeft.execute     20  ojAlgo  thrpt    3     2318116.375 ±     28463.908  ops/min
FillByMultiplyingTransposedLeft.execute     20     MTJ  thrpt    3     9050308.015 ±    423418.049  ops/min
FillByMultiplyingTransposedLeft.execute     32     ACM  thrpt    3      849874.447 ±      6929.895  ops/min
FillByMultiplyingTransposedLeft.execute     32    EJML  thrpt    3     1656941.031 ±     16681.546  ops/min
FillByMultiplyingTransposedLeft.execute     32  ojAlgo  thrpt    3      716305.350 ±     46376.962  ops/min
FillByMultiplyingTransposedLeft.execute     32     MTJ  thrpt    3     4464285.257 ±    320672.736  ops/min
FillByMultiplyingTransposedLeft.execute     50     ACM  thrpt    3      245456.864 ±     23349.327  ops/min
FillByMultiplyingTransposedLeft.execute     50    EJML  thrpt    3      469043.698 ±      6603.547  ops/min
FillByMultiplyingTransposedLeft.execute     50  ojAlgo  thrpt    3      405223.553 ±      7542.088  ops/min
FillByMultiplyingTransposedLeft.execute     50     MTJ  thrpt    3     1541571.971 ±    135453.317  ops/min
FillByMultiplyingTransposedLeft.execute     64     ACM  thrpt    3      117858.172 ±      3707.640  ops/min
FillByMultiplyingTransposedLeft.execute     64    EJML  thrpt    3      214588.258 ±      2228.083  ops/min
FillByMultiplyingTransposedLeft.execute     64  ojAlgo  thrpt    3      229855.471 ±     11934.372  ops/min
FillByMultiplyingTransposedLeft.execute     64     MTJ  thrpt    3      804249.632 ±    435094.567  ops/min
FillByMultiplyingTransposedLeft.execute    100     ACM  thrpt    3       25990.018 ±      1965.458  ops/min
FillByMultiplyingTransposedLeft.execute    100    EJML  thrpt    3       60359.021 ±      8155.426  ops/min
FillByMultiplyingTransposedLeft.execute    100  ojAlgo  thrpt    3      111872.986 ±       767.628  ops/min
FillByMultiplyingTransposedLeft.execute    100     MTJ  thrpt    3      236239.636 ±      1390.114  ops/min
FillByMultiplyingTransposedLeft.execute    128     ACM  thrpt    3       11649.896 ±       217.615  ops/min
FillByMultiplyingTransposedLeft.execute    128    EJML  thrpt    3       27328.617 ±      1905.028  ops/min
FillByMultiplyingTransposedLeft.execute    128  ojAlgo  thrpt    3       62338.882 ±      1582.473  ops/min
FillByMultiplyingTransposedLeft.execute    128     MTJ  thrpt    3      267940.519 ±       977.372  ops/min
FillByMultiplyingTransposedLeft.execute    200     ACM  thrpt    3        1951.626 ±       376.658  ops/min
FillByMultiplyingTransposedLeft.execute    200    EJML  thrpt    3        6944.087 ±       663.034  ops/min
FillByMultiplyingTransposedLeft.execute    200  ojAlgo  thrpt    3       28304.586 ±      1397.256  ops/min
FillByMultiplyingTransposedLeft.execute    200     MTJ  thrpt    3      121091.724 ±      1113.705  ops/min
FillByMultiplyingTransposedLeft.execute    256     ACM  thrpt    3         886.631 ±         4.690  ops/min
FillByMultiplyingTransposedLeft.execute    256    EJML  thrpt    3        3090.146 ±        63.849  ops/min
FillByMultiplyingTransposedLeft.execute    256  ojAlgo  thrpt    3       13964.988 ±      3083.615  ops/min
FillByMultiplyingTransposedLeft.execute    256     MTJ  thrpt    3       71520.527 ±     41372.029  ops/min
FillByMultiplyingTransposedLeft.execute    500     ACM  thrpt    3          63.597 ±         0.707  ops/min
FillByMultiplyingTransposedLeft.execute    500    EJML  thrpt    3         470.317 ±        18.206  ops/min
FillByMultiplyingTransposedLeft.execute    500  ojAlgo  thrpt    3        1988.204 ±       596.212  ops/min
FillByMultiplyingTransposedLeft.execute    500     MTJ  thrpt    3       11878.181 ±      3234.281  ops/min
FillByMultiplyingTransposedLeft.execute    512     ACM  thrpt    3          45.417 ±         5.575  ops/min
FillByMultiplyingTransposedLeft.execute    512    EJML  thrpt    3         332.046 ±        42.089  ops/min
FillByMultiplyingTransposedLeft.execute    512  ojAlgo  thrpt    3        1931.025 ±       122.519  ops/min
FillByMultiplyingTransposedLeft.execute    512     MTJ  thrpt    3       12137.983 ±      1645.595  ops/min
FillByMultiplyingTransposedLeft.execute   1000     ACM  thrpt    3           3.253 ±         1.489  ops/min
FillByMultiplyingTransposedLeft.execute   1000    EJML  thrpt    3          47.230 ±         8.771  ops/min
FillByMultiplyingTransposedLeft.execute   1000  ojAlgo  thrpt    3         264.904 ±        36.853  ops/min
FillByMultiplyingTransposedLeft.execute   1000     MTJ  thrpt    3        1387.058 ±       257.305  ops/min
 * </pre>
 *
 * <h2>2018-01-23</h2>
 *
 * <pre>
 * </pre>
 *
 * <h1>MacBook Pro</h1>
 * <h2>2018-02-18</h2>
 *
 * <pre>
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class FillByMultiplyingTransposedLeft extends MatrixBenchmarkOperation {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(FillByMultiplyingTransposedLeft.class);
    }

    @Param({ "1", "2", "3", "4", "5", "8", "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000" })
    public int dim;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

    private MutatingBinaryMatrixMatrixOperation<?, ?> myOperation;

    Object left;
    Object product;
    Object right;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(left, right, product);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        myOperation = library.getOperationFillByMultiplying(true, false);

        left = this.makeRandom(dim, dim, library);
        right = this.makeRandom(dim, dim, library);
        product = this.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
