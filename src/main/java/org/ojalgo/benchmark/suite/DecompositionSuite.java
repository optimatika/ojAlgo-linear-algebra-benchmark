package org.ojalgo.benchmark.suite;

import org.ojalgo.benchmark.BenchmarkSuite;
import org.openjdk.jmh.runner.RunnerException;

/**
 * The major decompositions, of square matrices: DecomposeCholesky, DecomposeLU, DecomposeQR, DecomposeEvD
 * (symmetric) and DecomposeSVD. Each decomposes and extracts the factors, from scratch every invocation, and
 * the factors are verified to reconstruct the matrix and to have the required structure (triangular,
 * orthogonal, diagonal…).
 */
public interface DecompositionSuite extends BenchmarkSuite {

    static void main(final String[] args) throws RunnerException {
        BenchmarkSuite.run(DecompositionSuite.class);
    }

}