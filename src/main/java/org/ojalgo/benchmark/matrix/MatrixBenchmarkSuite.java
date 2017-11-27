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
package org.ojalgo.benchmark.matrix;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.matrix.MatrixUtils;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public abstract class MatrixBenchmarkSuite {

    protected static ChainedOptionsBuilder options() {
        return new OptionsBuilder().forks(1).measurementIterations(3).warmupIterations(7).mode(Mode.Throughput).timeUnit(TimeUnit.MINUTES)
                .timeout(new TimeValue(1L, TimeUnit.HOURS)).jvmArgs("-server", "-Xmx6g");
    }

    public static void run(final Class<?> clazz) throws RunnerException {
        new Runner(MatrixBenchmarkSuite.options().include(clazz.getSimpleName()).build()).run();
    }

    protected MatrixBenchmarkContestant<?> contestant;

    protected MatrixBenchmarkSuite() {
        super();
    }

    public abstract Object execute();

    protected final Object makeRandom(final int numberOfRows, final int numberOfColumns, final MatrixBenchmarkContestant<?> contestant) {
        final MatrixBenchmarkContestant<?>.MatrixBuilder tmpSupplier = contestant.getMatrixBuilder(numberOfRows, numberOfColumns);
        for (int j = 0; j < numberOfColumns; j++) {
            for (int i = 0; i < numberOfRows; i++) {
                tmpSupplier.set(i, j, Math.random());
            }
        }
        return tmpSupplier.get();
    }

    protected double[][] makeSPD(final int size) {
        return MatrixUtils.makeSPD(size).toRawCopy2D();
    }

    protected final Object makeSPD(final int size, final MatrixBenchmarkContestant<?> contestant) {

        final double[] tmpRandom = new double[size];

        final MatrixBenchmarkContestant<?>.MatrixBuilder tmpSupplier = contestant.getMatrixBuilder(size, size);

        for (int i = 0; i < size; i++) {

            tmpRandom[i] = Math.random();

            for (int j = 0; j < i; j++) {
                tmpSupplier.set(i, j, tmpRandom[i] + tmpRandom[j]);
                tmpSupplier.set(j, i, tmpRandom[j] + tmpRandom[i]);
            }
            tmpSupplier.set(i, i, tmpRandom[i] + 1.0);
        }

        return tmpSupplier.get();
    }

    /**
     * Verify that the tested library/functionality conforms with the benchmark specifications. Annotate the
     * implementation with <code>@TearDown(Level.Iteration)</code>
     */
    public abstract void verify() throws BenchmarkRequirementsException;

    protected void verifyStateless(final Class<?> clazz) throws BenchmarkRequirementsException {

        for (final Field tmpField : clazz.getDeclaredFields()) {
            if (!tmpField.getName().equals("this$0")) {
                throw new BenchmarkRequirementsException(tmpField.toString());
            }
        }

        final Class<?> tmpSuperclazz = clazz.getSuperclass();
        if (tmpSuperclazz != null) {
            this.verifyStateless(tmpSuperclazz);
        }

        for (final Class<?> tmpInterface : clazz.getInterfaces()) {
            this.verifyStateless(tmpInterface);
        }
    }

}
