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
package org.ojalgo.benchmark;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public abstract class MatrixBenchmarkOperation {

    @FunctionalInterface
    public interface DecompositionOperation<I, T extends I> {

        public abstract I[] operate(I arg) throws Exception;

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg) {
            try {
                return this.operate((I) arg);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }

    }

    @FunctionalInterface
    public interface MutatingBinaryMatrixMatrixOperation<I, T extends I> {

        public abstract void operate(I arg1, I arg2, T ret) throws Exception;

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg1, final Object arg2, final Object ret) {
            try {
                this.operate((I) arg1, (I) arg2, (T) ret);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
            return ret;
        }

    }

    @FunctionalInterface
    public interface MutatingBinaryMatrixScalarOperation<I, T extends I> {

        public abstract void operate(I arg1, double arg2, T ret) throws Exception;

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg1, final double arg2, final Object ret) {
            try {
                this.operate((I) arg1, arg2, (T) ret);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
            return ret;
        }

    }

    @FunctionalInterface
    public interface MutatingUnaryMatrixOperation<I, T extends I> {

        public abstract void operate(I arg, T ret) throws Exception;

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg, final Object ret) {
            try {
                this.operate((I) arg, (T) ret);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
            return ret;
        }

    }

    @FunctionalInterface
    public interface ProducingBinaryMatrixMatrixOperation<I, T extends I> {

        public abstract I operate(I arg1, I arg2) throws Exception;

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg1, final Object arg2) {
            try {
                return this.operate((I) arg1, (I) arg2);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }

    @FunctionalInterface
    public interface ProducingUnaryMatrixOperation<I, T extends I> {

        public abstract I operate(I arg) throws Exception;

        @SuppressWarnings("unchecked")
        default Object execute(final Object arg) {
            try {
                return this.operate((I) arg);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }

    }

    @FunctionalInterface
    public interface PropertyOperation<I, T extends I> {

        public abstract double operate(I arg) throws Exception;

        @SuppressWarnings("unchecked")
        default double execute(final Object arg) {
            try {
                return this.operate((I) arg);
            } catch (final Exception exception) {
                exception.printStackTrace();
                return Double.NaN;
            }
        }

    }

    static final TimeValue ITERATION_TIME = new TimeValue(10L, TimeUnit.SECONDS);
    static final TimeValue TIMEOUT = new TimeValue(1L, TimeUnit.MINUTES);

    protected static ChainedOptionsBuilder options() {
        return new OptionsBuilder().forks(1).warmupIterations(7).measurementIterations(3).mode(Mode.Throughput).timeUnit(TimeUnit.MINUTES)
                .warmupTime(ITERATION_TIME).measurementTime(ITERATION_TIME).timeout(TIMEOUT).jvmArgs("-Xmx6g").resultFormat(ResultFormatType.CSV);
    }

    protected static void run(final Class<?> clazz) throws RunnerException {
        new Runner(MatrixBenchmarkOperation.options().include(clazz.getSimpleName()).build()).run();
    }

    protected MatrixBenchmarkLibrary<?, ?> library;

    protected MatrixBenchmarkOperation() {
        super();
    }

    public abstract Object execute();

    public abstract void setup();

    /**
     * Verify that the tested library/functionality conforms with the benchmark specifications. Annotate the
     * implementation with <code>@TearDown(Level.Iteration)</code>
     */
    public abstract void verify() throws BenchmarkRequirementsException;

    protected final Object makeRandom(final int numberOfRows, final int numberOfColumns, final MatrixBenchmarkLibrary<?, ?> contestant) {
        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder tmpSupplier = contestant.getMatrixBuilder(numberOfRows, numberOfColumns);
        for (int j = 0; j < numberOfColumns; j++) {
            for (int i = 0; i < numberOfRows; i++) {
                tmpSupplier.set(i, j, Math.random());
            }
        }
        return tmpSupplier.get();
    }

    protected final Object makeSPD(final int size, final MatrixBenchmarkLibrary<?, ?> contestant) {

        final double[] random = new double[size];

        final MatrixBenchmarkLibrary<?, ?>.MatrixBuilder builder = contestant.getMatrixBuilder(size, size);

        for (int i = 0; i < size; i++) {

            random[i] = Math.random();

            for (int j = 0; j < i; j++) {
                builder.set(i, j, random[i] * random[j]);
                builder.set(j, i, random[j] * random[i]);
            }
            builder.set(i, i, random[i] + 1.0);
        }

        return builder.get();
    }

    protected final Object makeZero(final int numberOfRows, final int numberOfColumns, final MatrixBenchmarkLibrary<?, ?> contestant) {
        return contestant.getMatrixBuilder(numberOfRows, numberOfColumns).get();
    }

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
