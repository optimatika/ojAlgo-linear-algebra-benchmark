package org.ojalgo.benchmark.suite;

import org.ojalgo.benchmark.BenchmarkSuite;
import org.openjdk.jmh.runner.RunnerException;

/**
 * A suite corresponding to the Java Matrix Benchmark set of tests.
 * <ul>
 * <li>add</li>
 * <li>scale</li>
 * <li>transpose</li>
 * <li>multiply</li>
 * <li>multiply (transp)</li>
 * <li>invert</li>
 * <li>invert SPD</li>
 * <li>Determinant</li>
 * <li>Solve</li>
 * <li>Solve Least Squares</li>
 * <li>SVD</li>
 * <li>EvD</li>
 * </ul>
 * https://lessthanoptimal.github.io/Java-Matrix-Benchmark/
 * https://github.com/lessthanoptimal/Java-Matrix-Benchmark
 *
 * @author apete
 */
public interface JavaMatrixBenchmark extends BenchmarkSuite {

    static void main(final String[] args) throws RunnerException {
        BenchmarkSuite.run(JavaMatrixBenchmark.class);
    }

}