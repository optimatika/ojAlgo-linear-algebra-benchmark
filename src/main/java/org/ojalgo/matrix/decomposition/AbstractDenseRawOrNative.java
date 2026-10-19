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

import org.ojalgo.array.operation.NativeLibrary;
import org.ojalgo.matrix.store.MatrixStore;
import org.openjdk.jmh.infra.BenchmarkParams;

abstract class AbstractDenseRawOrNative<T extends MatrixDecomposition<Double>> {

    protected T dense;
    protected T raw;

    public abstract MatrixStore<Double> accelerate();

    public abstract MatrixStore<Double> dense();

    public abstract MatrixStore<Double> raw();

    protected abstract T newDense();

    protected abstract T newRaw();

    protected void setup(final BenchmarkParams params) {

        boolean nativeCode = params.getBenchmark().endsWith(".accelerate");

        if (!nativeCode) {
            System.setProperty(NativeLibrary.PROPERTY_LIBRARY, NativeLibrary.NONE);
        }

        if (NativeLibrary.isAvailable() != nativeCode) {
            throw new IllegalStateException(
                    nativeCode ? "No native BLAS/LAPACK available to ojAlgo!" : "ojAlgo's native code already initialised – run with forks!");
        }

        dense = this.newDense();
        raw = this.newRaw();
    }

}
