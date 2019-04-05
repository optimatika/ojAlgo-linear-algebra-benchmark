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

    @Param({ "10", "100", "1000" })
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

        body = this.makeSPD(dim, library);
        rhs = this.makeRandom(dim, 1, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myHermitianSolver.getClass());

    }

}
