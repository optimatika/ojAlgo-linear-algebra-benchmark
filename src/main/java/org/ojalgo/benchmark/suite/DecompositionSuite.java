package org.ojalgo.benchmark.suite;

import org.ojalgo.benchmark.BenchmarkSuite;
import org.openjdk.jmh.runner.RunnerException;

public interface DecompositionSuite extends BenchmarkSuite {

    static void main(final String[] args) throws RunnerException {
        BenchmarkSuite.run(DecompositionSuite.class);
    }

}