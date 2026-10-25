/*
 * Copyright 1997-2026 Optimatika (www.optimatika.se)
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
package org.ojalgo.benchmark.operation;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation;
import org.ojalgo.benchmark.suite.WorkloadSuite;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * One step of a Kalman filter: predict, [x]<sup>-</sup> = [F][x] and [P]<sup>-</sup> = [F][P][F]<sup>T</sup> +
 * [Q], then update with the measurement [z]: [S] = [H][P]<sup>-</sup>[H]<sup>T</sup> + [R], [K] =
 * [P]<sup>-</sup>[H]<sup>T</sup>[S]<sup>-1</sup>, [x]<sup>+</sup> = [x]<sup>-</sup> + [K]([z] -
 * [H][x]<sup>-</sup>) and [P]<sup>+</sup> = [P]<sup>-</sup> - [K][H][P]<sup>-</sup>.
 * <dl>
 * <dt>Input</dt>
 * <dd>dim states and m = max(1, dim/2) measurements. The transition [F], dim x dim, and the observation [H], m
 * x dim, uniform in [-1/&radic;dim, 1/&radic;dim) – so their norms don't grow with dim. The covariances [P] and
 * [Q], dim x dim, [a][a]<sup>T</sup>/dim + [I] with [a] uniform in [-1, 1) – eigenvalues in [1, 2). The
 * measurement noise [R], m x m, diagonal, uniform in [1, 2). The state [x], dim x 1, and the measurement [z], m
 * x 1, uniform in [-1, 1).</dd>
 * <dt>Measured</dt>
 * <dd>The step, into the preallocated [x]<sup>+</sup> and [P]<sup>+</sup>, every invocation from the same [x]
 * and [P]. Workspace for the intermediate results is preallocated in setup, as it would be in a filter loop.
 * [S]<sup>-1</sup> is not calculated – [K] is solved for, typically with a Cholesky decomposition.</dd>
 * <dt>Required</dt>
 * <dd>With [R] diagonal, the update can be checked without inverting anything: [P]<sup>-</sup>[v] =
 * [P]<sup>+</sup>([v] + [H]<sup>T</sup>[R]<sup>-1</sup>[H][P]<sup>-</sup>[v]) for a random vector [v], and
 * [x]<sup>+</sup> = [x]<sup>-</sup> + [P]<sup>+</sup>[H]<sup>T</sup>[R]<sup>-1</sup>([z] -
 * [H][x]<sup>-</sup>) ({@code verifyKalmanUpdate}).</dd>
 * <dt>Java Matrix Benchmark</dt>
 * <dd>Not part of it.</dd>
 * </dl>
 * The rules that apply to all benchmarks are described in {@link MatrixBenchmarkOperation}.
 */
public class KalmanFilter extends MatrixBenchmarkOperation implements WorkloadSuite {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(KalmanFilter.class, SIZE);
    }

    private KalmanFilterOperation<?, ?> myOperation;

    Object covariance;
    Object measurement;
    Object measurementNoise;
    Object observation;
    Object processNoise;
    Object result;
    Object state;
    Object transition;
    Object updatedCovariance;
    Object updatedState;

    @Override
    @Benchmark
    public Object execute() {
        return result = myOperation.execute(transition, processNoise, observation, measurementNoise, state, covariance, measurement, updatedState,
                updatedCovariance);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.make(lib);

        int nbMeasurements = Math.max(1, dim / 2);

        myOperation = library.getOperationKalmanFilter(dim, nbMeasurements);

        double scale = 1.0 / Math.sqrt(dim);

        transition = MatrixBenchmarkOperation.makeRandom(dim, dim, scale, library);
        processNoise = MatrixBenchmarkOperation.makeCovariance(dim, library);
        observation = MatrixBenchmarkOperation.makeRandom(nbMeasurements, dim, scale, library);
        measurementNoise = MatrixBenchmarkOperation.makeDiagonal(nbMeasurements, library);

        state = MatrixBenchmarkOperation.makeRandom(dim, 1, library);
        covariance = MatrixBenchmarkOperation.makeCovariance(dim, library);
        measurement = MatrixBenchmarkOperation.makeRandom(nbMeasurements, 1, library);

        updatedState = MatrixBenchmarkOperation.makeZero(dim, 1, library);
        updatedCovariance = MatrixBenchmarkOperation.makeZero(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {
        if (result == null) {
            throw new BenchmarkRequirementsException("Kalman filter step failed!");
        }
        this.verifyKalmanUpdate(transition, processNoise, observation, measurementNoise, state, covariance, measurement, updatedState, updatedCovariance);
    }

}
