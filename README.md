# ojAlgo Linear Algebra Benchmark (ojLAB)

Benchmarks and internal tuning tools for [ojAlgo](https://github.com/optimatika/ojAlgo) linear algebra operations, using [JMH](https://openjdk.org/projects/code-tools/jmh/) (Java Microbenchmark Harness).

## What's in here

This project serves two purposes:

### Library comparison benchmarks

Comparing ojAlgo linear algebra operations against alternative Java libraries. The `lib` parameter of each benchmark selects the library:

| `lib` | Library | Why it's included |
|-------|---------|-------------------|
| `EJML` | [EJML](https://ejml.org/) | Pure Java, no dependencies, high performance. Top quality pure Java alternative. |
| `Hipparchus` | [Hipparchus](https://hipparchus.org/) | Maintained fork of Apache Commons Math (essentially the same linear algebra code). A baseline that alternatives must justify improving upon. |
| `ojAlgo` | [ojAlgo](https://github.com/optimatika/ojAlgo) | Pure Java – native code switched off. |
| `ojAlgoNative` | [ojAlgo](https://github.com/optimatika/ojAlgo) | ojAlgo delegating to a native BLAS/LAPACK (through the FFM API) where it can. Accelerate on macOS, OpenBLAS, MKL… elsewhere. The native code performance reference. |

Benchmark classes in `org.ojalgo.benchmark.lab` cover: matrix multiplication, addition, scaling, transpose, decompositions (EVD, SVD, Cholesky), solvers (general, Hermitian, least squares), inversion, determinant, and pseudoinverse.

Every benchmark verifies the results after each iteration (outside the measured time), independently of the library being benchmarked – products and inverses with random vectors, solutions by their residuals, determinants against a plain Java calculation. A library that produces wrong results is reported as a failure, and gets no score.

### Internal tuning benchmarks

Benchmarks used to tune ojAlgo's internal implementation parameters:

- **Threshold tuners** (`org.ojalgo.matrix.operation`) -- determine at what matrix size operations should switch between serial and parallel execution, and between different multiplication strategies.
- **Parallelism tuners** (`org.ojalgo.matrix.operation`) -- find the optimal degree of parallelism for various matrix operations and Householder transformations.
- **Decomposition tuners** (`org.ojalgo.matrix.decomposition`) -- compare implementation variants (primitive vs raw arrays) for Cholesky, LU, QR, Eigenvalue, SVD, and Tridiagonal decompositions, and benchmark sparse LU operations.
- **Array operation tuners** (`org.ojalgo.array.operation`) -- tune DOT product unrolling, AXPY ordering, and norm calculations.
- **Other** -- DFT benchmarks, number parsing benchmarks, special function benchmarks, sparse array performance, and solver strategy selection.

## Building and running

```sh
mvn clean install
```

Each benchmark class has a `main` method that runs it with the settings defined in code – `MatrixBenchmarkOperation.options()` for the library comparison benchmarks (1 fork, 2 warmup and 3 measurement iterations of 10s, ops/min, results written to `jmh-result.csv`), `BenchmarkUtils.options()` for most of the others. This is the normal way to run a benchmark:

```sh
java -cp target/ojlab.jar org.ojalgo.benchmark.lab.FillByMultiplying
```

Replace `FillByMultiplying` with whichever benchmark you want to run. The sizes and libraries are the benchmark's `@Param` values – edit them to run a subset. The full sets of sizes are kept as constants in `MatrixBenchmarkOperation`, for reference.

The jar's main class is JMH's own command line runner. Use it to list the available benchmarks, or for ad hoc runs:

```sh
java -jar target/ojlab.jar -l
```

Note that it does not use the settings defined in code, but JMH's defaults (5 forks, 5 warmup and 5 measurement iterations of 10s, ops/s) unless you specify otherwise. Restrict the parameters with `-p`, e.g. to compare ojAlgo with and without native code:

```sh
java -jar target/ojlab.jar org.ojalgo.benchmark.lab.FillByMultiplying -p lib=ojAlgo,ojAlgoNative -p dim=100,1000 -f 1 -wi 2 -i 3 -tu m
```

### Native code

The forked JVMs of the library comparison benchmarks are started with `--enable-native-access=ALL-UNNAMED` (an `@Fork` annotation on `MatrixBenchmarkOperation`). Each fork benchmarks a single library, and instantiating it decides whether ojAlgo uses native code: `ojAlgo` switches it off, `ojAlgoNative` fails if there is no native library to use. Don't run without forks (`-f 0`).

To use a specific native library, set the `ojalgo.native.library` system property in the forked JVMs. Use `-jvmArgsPrepend` – `-jvmArgsAppend` would replace the `--enable-native-access` argument:

```sh
java -jar target/ojlab.jar org.ojalgo.benchmark.lab.FillByMultiplying -p lib=ojAlgoNative -jvmArgsPrepend -Dojalgo.native.library=/opt/homebrew/opt/openblas/lib/libopenblas.dylib
```

The internal tuning benchmarks don't grant native access, and tune the pure Java implementations.

## Results

Benchmark results are published at: https://www.ojalgo.org/category/benchmarks/
