/*
 * Copyright 1997-2022 Optimatika (www.optimatika.se)
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
Result "org.ojalgo.benchmark.lab.Scale.execute":
  8049.585 ±(99.9%) 357.424 ops/min [Average]
  (min, avg, max) = (8033.363, 8049.585, 8071.352), stdev = 19.592
  CI (99.9%): [7692.161, 8407.010] (assumes normal distribution)


# Run complete. Total time: 00:20:09

Benchmark      (dim)   (lib)   Mode  Cnt           Score          Error    Units
Scale.execute     10     ACM  thrpt    3    60360633.924 ±   403272.522  ops/min
Scale.execute     10    EJML  thrpt    3  1232129771.831 ± 20741535.237  ops/min
Scale.execute     10  ojAlgo  thrpt    3  1303631722.966 ± 13381499.544  ops/min
Scale.execute     10     MTJ  thrpt    3    89332488.188 ±   685338.931  ops/min
Scale.execute    100     ACM  thrpt    3     1779845.361 ±  1077399.245  ops/min
Scale.execute    100    EJML  thrpt    3    10915172.743 ±   255068.223  ops/min
Scale.execute    100  ojAlgo  thrpt    3    10608399.014 ±    81233.538  ops/min
Scale.execute    100     MTJ  thrpt    3      924558.751 ±     8998.151  ops/min
Scale.execute   1000     ACM  thrpt    3       10935.585 ±     3746.852  ops/min
Scale.execute   1000    EJML  thrpt    3       30127.826 ±      341.113  ops/min
Scale.execute   1000  ojAlgo  thrpt    3       69883.968 ±    28191.452  ops/min
Scale.execute   1000     MTJ  thrpt    3        8049.585 ±      357.424  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class Scale extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(Scale.class);
    }

    @Param({ "10", "100", "1000" })
    public int dim;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

    private MutatingBinaryMatrixScalarOperation<?, ?> myOperation;

    Object original;
    Object result;
    double scale;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(original, scale, result);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        myOperation = library.getOperationScale();

        original = this.makeRandom(dim, dim, library);
        scale = Math.random();
        result = this.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
