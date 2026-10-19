/*
 * Copyright 1997-2026 Optimatika
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
package org.ojalgo.matrix.decomposition;

import org.ojalgo.BenchmarkUtils;
import org.ojalgo.array.operation.NativeLibrary;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.R064Store;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Mac Pro: 2019-04-25 => 512
 *
 * <pre>
Benchmark                   (dim)   Mode  Cnt       Score      Error    Units
PrimitiveOrRawLU.primitive     50  thrpt    3  301157.789 ± 4491.290  ops/min
PrimitiveOrRawLU.primitive    100  thrpt    3   53757.171 ± 1817.663  ops/min
PrimitiveOrRawLU.primitive    200  thrpt    3    9749.099 ±  129.020  ops/min
PrimitiveOrRawLU.primitive    500  thrpt    3     953.864 ±   43.429  ops/min
PrimitiveOrRawLU.primitive   1000  thrpt    3     166.926 ±    9.213  ops/min
PrimitiveOrRawLU.primitive   2000  thrpt    3      19.061 ±    2.633  ops/min
PrimitiveOrRawLU.raw           50  thrpt    3  358907.961 ± 9528.108  ops/min
PrimitiveOrRawLU.raw          100  thrpt    3   66471.777 ±  503.598  ops/min
PrimitiveOrRawLU.raw          200  thrpt    3   12666.588 ±  826.128  ops/min
PrimitiveOrRawLU.raw          500  thrpt    3    1016.314 ±   58.658  ops/min
PrimitiveOrRawLU.raw         1000  thrpt    3     137.489 ±   42.776  ops/min
PrimitiveOrRawLU.raw         2000  thrpt    3      11.569 ±    3.558  ops/min
 * </pre>
 *
 * Mac Pro: 2015-06-24 => 16
 *
 * <pre>
 * # Run complete. Total time: 00:02:03
 *
 * Benchmark                   (dim)   Mode  Cnt         Score        Error    Units
 * PrimitiveOrRawLU.primitive     10  thrpt    3  13578080,870 ± 802789,675  ops/min
 * PrimitiveOrRawLU.primitive     20  thrpt    3   2920265,006 ±  23322,349  ops/min
 * PrimitiveOrRawLU.primitive     50  thrpt    3    265251,744 ±   3410,323  ops/min
 * PrimitiveOrRawLU.primitive    100  thrpt    3     55651,238 ±   1843,507  ops/min
 * PrimitiveOrRawLU.primitive    200  thrpt    3     10950,428 ±    328,406  ops/min
 * PrimitiveOrRawLU.primitive    500  thrpt    3       956,466 ±     75,056  ops/min
 * PrimitiveOrRawLU.primitive   1000  thrpt    3       157,199 ±     29,044  ops/min
 * PrimitiveOrRawLU.raw           10  thrpt    3  14313960,051 ± 157788,131  ops/min
 * PrimitiveOrRawLU.raw           20  thrpt    3   2636161,879 ±  47762,990  ops/min
 * PrimitiveOrRawLU.raw           50  thrpt    3    249533,910 ±   4837,851  ops/min
 * PrimitiveOrRawLU.raw          100  thrpt    3     55654,408 ±   4362,385  ops/min
 * PrimitiveOrRawLU.raw          200  thrpt    3     10497,027 ±    389,121  ops/min
 * PrimitiveOrRawLU.raw          500  thrpt    3       786,141 ±    145,693  ops/min
 * PrimitiveOrRawLU.raw         1000  thrpt    3       100,325 ±      7,884  ops/min
 * </pre>
 *
 * MacBook Pro, M5 Pro: 2026-10-17
 *
 * <pre>
 * Benchmark                         (dim)   Mode  Cnt        Score        Error  Units
PrimitiveOrRawLU.primitive           10  thrpt    3  1279799.527 ±  45736.664  ops/s
PrimitiveOrRawLU.primitive           20  thrpt    3   274110.874 ±   9679.591  ops/s
PrimitiveOrRawLU.primitive           50  thrpt    3    34063.124 ±    853.417  ops/s
PrimitiveOrRawLU.primitive          100  thrpt    3     6675.727 ±    185.684  ops/s
PrimitiveOrRawLU.primitive          200  thrpt    3     1182.803 ±      7.544  ops/s
PrimitiveOrRawLU.primitive          500  thrpt    3       84.890 ±      4.429  ops/s
PrimitiveOrRawLU.primitive         1000  thrpt    3       18.539 ±      0.138  ops/s
PrimitiveOrRawLU.primitive         2000  thrpt    3        2.486 ±      0.194  ops/s
PrimitiveOrRawLU.primitiveNative     10  thrpt    3  2125610.984 ± 154573.240  ops/s
PrimitiveOrRawLU.primitiveNative     20  thrpt    3   533634.587 ±  46401.918  ops/s
PrimitiveOrRawLU.primitiveNative     50  thrpt    3    65666.457 ±   2976.579  ops/s
PrimitiveOrRawLU.primitiveNative    100  thrpt    3    19340.929 ±    210.053  ops/s
PrimitiveOrRawLU.primitiveNative    200  thrpt    3     5118.441 ±     73.344  ops/s
PrimitiveOrRawLU.primitiveNative    500  thrpt    3      613.253 ±     53.864  ops/s
PrimitiveOrRawLU.primitiveNative   1000  thrpt    3      124.282 ±      7.993  ops/s
PrimitiveOrRawLU.primitiveNative   2000  thrpt    3       18.520 ±      0.223  ops/s
PrimitiveOrRawLU.raw                 10  thrpt    3  1653990.060 ± 104774.470  ops/s
PrimitiveOrRawLU.raw                 20  thrpt    3   361263.144 ±  30544.252  ops/s
PrimitiveOrRawLU.raw                 50  thrpt    3    37701.147 ±    434.691  ops/s
PrimitiveOrRawLU.raw                100  thrpt    3     6405.459 ±     54.513  ops/s
PrimitiveOrRawLU.raw                200  thrpt    3     1122.575 ±     16.812  ops/s
PrimitiveOrRawLU.raw                500  thrpt    3       81.908 ±      5.656  ops/s
PrimitiveOrRawLU.raw               1000  thrpt    3       12.834 ±      0.112  ops/s
PrimitiveOrRawLU.raw               2000  thrpt    3        1.590 ±      0.005  ops/s
Benchmark                    (dim)   Mode  Cnt   Score   Error  Units
PrimitiveOrRawLU.accelerate   2000  thrpt    3  18.418 ± 1.082  ops/s
PrimitiveOrRawLU.accelerate   5000  thrpt    3   1.283 ± 0.212  ops/s
PrimitiveOrRawLU.accelerate  10000  thrpt    3   0.165 ± 0.064  ops/s
PrimitiveOrRawLU.dense        2000  thrpt    3   2.512 ± 0.258  ops/s
PrimitiveOrRawLU.dense        5000  thrpt    3   0.117 ± 0.051  ops/s
PrimitiveOrRawLU.dense       10000  thrpt    3   0.013 ± 0.003  ops/s
PrimitiveOrRawLU.raw          2000  thrpt    3   1.493 ± 0.017  ops/s
PrimitiveOrRawLU.raw          5000  thrpt    3   0.085 ± 0.006  ops/s
PrimitiveOrRawLU.raw         10000  thrpt    3   0.010 ± 0.001  ops/s
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
@Fork(jvmArgsAppend = "--enable-native-access=ALL-UNNAMED")
public class PrimitiveOrRawLU extends AbstractDenseRawOrNative<LU<Double>> {

    public static void main(final String[] args) throws RunnerException {
        BenchmarkUtils.run(PrimitiveOrRawLU.class);
    }

    @Param({ "2000", "5000", "10000" })
    public int dim;

    R064Store matrix;

    R064Store preallocated;

    /**
     * The same as {@link #dense()}, but in a fork where ojAlgo uses native code: DenseLU then calls GETRF,
     * GETRS and GETRI natively, above their thresholds.
     */
    @Override
    @Benchmark
    public MatrixStore<Double> accelerate() {
        dense.compute(matrix);
        return dense.getInverse(preallocated);
    }

    @Override
    @Benchmark
    public MatrixStore<Double> dense() {
        dense.compute(matrix);
        return dense.getInverse(preallocated);
    }

    @Override
    @Benchmark
    public MatrixStore<Double> raw() {
        raw.compute(matrix);
        return raw.getInverse(preallocated);
    }

    /**
     * Each fork runs a single benchmark method. ojAlgo decides whether to use native code once, when
     * {@link NativeLibrary} is initialised, so that is settled first: only {@link #primitiveNative()} may use
     * it.
     */
    @Override
    @Setup
    public void setup(final BenchmarkParams params) {

        super.setup(params);

        matrix = R064Store.FACTORY.makeSPD(dim);

        preallocated = R064Store.FACTORY.make(dim, dim);
    }

    @Override
    protected LU<Double> newDense() {
        return new DenseLU.R064();
    }

    @Override
    protected LU<Double> newRaw() {
        return new RawLU();
    }

}
