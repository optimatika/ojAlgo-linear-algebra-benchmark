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
package org.ojalgo.benchmark.matrix.suite;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.matrix.MatrixBenchmarkContestant;
import org.ojalgo.benchmark.matrix.MatrixBenchmarkSuite;
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
 * <h2>2017-11-27</h2>
 *
 * <pre>
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class FillByMultiplying extends MatrixBenchmarkSuite {

    @FunctionalInterface
    public static interface TaskDefinition<T> {

        T multiply(T product, T left, T right);

    }

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkSuite.run(FillByMultiplying.class);
    }

    @Param({ "1", "2", "3", "4", "5", "8", "10", "16", "20", "32", "50", "64", "100", "128", "200", "256", "500", "512",
            "1000", /*
                     * "1024", "2000", "2048", "4096", "5000", "8192", "10000"
                     */ })
    public int dim;

    Object left;
    Object product;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String library;

    private TaskDefinition myTaskDefinition;
    Object righ;

    @Override
    @Benchmark
    public Object execute() {
        return myTaskDefinition.multiply(product, left, righ);
    }

    @Setup
    public void setup() {

        contestant = MatrixBenchmarkContestant.CONTESTANTS.get(library);

        myTaskDefinition = contestant.getMatrixMultiplier2();

        final double[][] tmpLeft = new double[dim][dim];
        for (int i = 0; i < tmpLeft.length; i++) {
            final double[] tmpRow = tmpLeft[i];
            for (int j = 0; j < tmpRow.length; j++) {
                tmpRow[j] = Math.random();
            }
        }
        left = contestant.convert(tmpLeft);

        final double[][] tmpRight = new double[dim][dim];
        for (int i = 0; i < tmpRight.length; i++) {
            final double[] tmpRow = tmpRight[i];
            for (int j = 0; j < tmpRow.length; j++) {
                tmpRow[j] = Math.random();
            }
        }
        righ = contestant.convert(tmpRight);

        product = contestant.getMatrixBuilder(dim, dim).get();
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        this.verifyStateless(myTaskDefinition.getClass());

    }

}
