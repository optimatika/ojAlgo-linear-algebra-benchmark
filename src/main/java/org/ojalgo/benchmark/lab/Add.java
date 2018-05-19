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
 * <pre>
Result "org.ojalgo.benchmark.lab.Add.execute":
  7772.240 ±(99.9%) 325.264 ops/min [Average]
  (min, avg, max) = (7760.034, 7772.240, 7792.700), stdev = 17.829
  CI (99.9%): [7446.976, 8097.503] (assumes normal distribution)


# Run complete. Total time: 00:20:09

Benchmark    (dim)   (lib)   Mode  Cnt           Score          Error    Units
Add.execute     10     ACM  thrpt    3    57261727.549 ±   101775.440  ops/min
Add.execute     10    EJML  thrpt    3   845073015.540 ± 27622912.728  ops/min
Add.execute     10  ojAlgo  thrpt    3  1048812255.182 ± 20700437.805  ops/min
Add.execute     10     MTJ  thrpt    3    31601661.093 ±  1461547.503  ops/min
Add.execute    100     ACM  thrpt    3     1594535.318 ±   428617.690  ops/min
Add.execute    100    EJML  thrpt    3     6601048.216 ±    57540.081  ops/min
Add.execute    100  ojAlgo  thrpt    3     7048501.734 ±   154649.433  ops/min
Add.execute    100     MTJ  thrpt    3      948478.021 ±     6896.721  ops/min
Add.execute   1000     ACM  thrpt    3        9226.428 ±     3579.453  ops/min
Add.execute   1000    EJML  thrpt    3       20889.401 ±      243.172  ops/min
Add.execute   1000  ojAlgo  thrpt    3       20778.833 ±      290.211  ops/min
Add.execute   1000     MTJ  thrpt    3        7772.240 ±      325.264  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class Add extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(Add.class);
    }

    @Param({ "1", "2", "3", "4", "5", "8", "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512", "1000", "1024", "2000", "2048", "4096",
            "5000", "8192", "10000" })
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

        left = this.makeRandom(dim, dim, library);
        right = this.makeRandom(dim, dim, library);
        result = this.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
