package org.ojalgo.benchmark.suite;

import org.ojalgo.benchmark.BenchmarkSuite;
import org.openjdk.jmh.runner.RunnerException;

/**
 * A suite of the operations real world code performs. Each benchmark is defined by a problem and the result
 * it must deliver, not by an algorithm – the library uses whatever it has that suits the problem. Each
 * computational kernel is covered about once, LU only by GeneralSolve, and nothing asks for an inverse or for
 * the factors of a decomposition unless the application needs them.
 * <ul>
 * <li>Kernels: FillByMultiplying, MultiplyTransposedLeft, MultiplyMatrixVector, MultiplyTransposedMatrixVector,
 * Gram</li>
 * <li>Solve: GeneralSolve, SymmetricSolve, SymmetricIndefiniteSolve, LeastSquaresSolver, MinimumNormSolve,
 * RepeatedSolve</li>
 * <li>Spectral: SymmetricEigenvalues, DecomposeEvD, SingularValues, ThinSVD</li>
 * <li>Composite: KalmanFilter, RidgeRegression</li>
 * </ul>
 *
 * @author apete
 */
public interface WorkloadSuite extends BenchmarkSuite {

    static void main(final String[] args) throws RunnerException {
        BenchmarkSuite.run(WorkloadSuite.class);
    }

}
