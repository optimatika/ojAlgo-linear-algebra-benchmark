/*
 * Copyright 1997-2020 Optimatika (www.optimatika.se)
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
 * MacBook Pro 2018-04-01
 * 
 * <pre>
# Run complete. Total time: 00:20:06

Benchmark          (dim)   (lib)   Mode  Cnt          Score          Error    Units
Transpose.execute     10     ACM  thrpt    3  115920350.392 ±  7994978.542  ops/min
Transpose.execute     10    EJML  thrpt    3  608689114.495 ± 11885046.082  ops/min
Transpose.execute     10  ojAlgo  thrpt    3  457287028.536 ± 31025052.989  ops/min
Transpose.execute     10     MTJ  thrpt    3  237444189.161 ±   945483.879  ops/min
Transpose.execute    100     ACM  thrpt    3    2923089.174 ±   269666.993  ops/min
Transpose.execute    100    EJML  thrpt    3   10207132.877 ±  6079726.029  ops/min
Transpose.execute    100  ojAlgo  thrpt    3    6272691.366 ±   190029.914  ops/min
Transpose.execute    100     MTJ  thrpt    3    2261270.585 ±   131585.007  ops/min
Transpose.execute   1000     ACM  thrpt    3       4631.933 ±      141.196  ops/min
Transpose.execute   1000    EJML  thrpt    3      37644.012 ±    25317.898  ops/min
Transpose.execute   1000  ojAlgo  thrpt    3      34557.537 ±     3316.361  ops/min
Transpose.execute   1000     MTJ  thrpt    3      15193.998 ±     1819.946  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class Transpose extends MatrixBenchmarkOperation implements BenchmarkSuite.JavaMatrixBenchmark {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(Transpose.class);
    }

    @Param({ "10", "100", "1000" })
    public int dim;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

    private MutatingUnaryMatrixOperation<?, ?> myOperation;

    Object original;
    Object result;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(original, result);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        myOperation = library.getOperationTranspose();

        original = this.makeRandom(dim, dim, library);
        result = this.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
