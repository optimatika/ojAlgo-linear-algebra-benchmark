# ojAlgo Linear Algebra Benchmark (ojLAB)

Benchmarks comparing [ojAlgo](https://github.com/optimatika/ojAlgo) linear algebra operations with alternative Java libraries, using [JMH](https://openjdk.org/projects/code-tools/jmh/) (Java Microbenchmark Harness).

## What's in here

The `lib` parameter of each benchmark selects the library:

| `lib` | Library | Why it's included |
|-------|---------|-------------------|
| `EJML` | [EJML](https://ejml.org/) | Pure Java, no dependencies, high performance. Top quality pure Java alternative. |
| `Hipparchus` | [Hipparchus](https://hipparchus.org/) | Maintained fork of Apache Commons Math (essentially the same linear algebra code). A baseline that alternatives must justify improving upon. |
| `ojAlgo-JVM` | [ojAlgo](https://github.com/optimatika/ojAlgo) | Pure Java – native code switched off. |
| `ojAlgo-Native` | [ojAlgo](https://github.com/optimatika/ojAlgo) | ojAlgo delegating to a native BLAS/LAPACK (through the FFM API) where it can. Accelerate on macOS, OpenBLAS, MKL… elsewhere. The native code performance reference. |
| `SMILE-JVM` | [SMILE](https://haifengl.github.io/) | Pure Java – SMILE's linear algebra backend set to `java`. Widely used machine learning library, with its own (dense) linear algebra. |
| `SMILE-Native` | [SMILE](https://haifengl.github.io/) | SMILE's `native` backend – BLAS/LAPACK through the FFM API, fails if there is none. The system's libblas/liblapack on macOS (Accelerate's legacy interface), OpenBLAS elsewhere. The alternative to ojAlgo calling the same native code. |

Benchmark classes in `org.ojalgo.benchmark.lab` cover: matrix multiplication, addition, scaling, transpose, decompositions (EvD, SVD), solvers (general, Hermitian, least squares), inversion, determinant, and pseudoinverse.

Every benchmark verifies the results after each iteration (outside the measured time), independently of the library being benchmarked – products and inverses with random vectors, solutions by their residuals, decompositions by reconstructing the matrix, determinants against a plain Java calculation. A library that produces wrong results is reported as a failure, and gets no score.

## Building and running

```sh
mvn clean install
```

Each benchmark class has a `main` method that runs it with the settings defined in code – `MatrixBenchmarkOperation.options()` (1 fork, throughput, a 16 GB heap) configured for one of three size ranges, `MatrixBenchmarkOperation.Size`:

| `Size` | Sizes | Libraries | Unit | Iteration time | Warmup + measurement iterations | Timeout |
|--------|-------|-----------|------|----------------|---------------------------------|---------|
| `SMALL` | 1 – 100 | all | ops/ms | 1 s | 8 + 5 | 10 s |
| `MEDIUM` | 10 – 1000 | all | ops/s | 1 s | 4 + 5 | 10 s |
| `LARGE` | 100 – 10000 | `ojAlgo-JVM`, `ojAlgo-Native`, `SMILE-Native` | ops/min | 1 s | 2 + 3 | 30 min |

An iteration lasts at least one invocation, however long that takes. The timeout counts from the start of an iteration, so it must exceed the iteration time plus one invocation.

The `main` methods run `MatrixBenchmarkOperation.SIZE` (`MEDIUM`) – change it to run another range. The results are written to a CSV file named after the benchmark and the size range, e.g. `FillByMultiplying-MEDIUM.csv`. This is the normal way to run a benchmark:

```sh
java -cp target/ojlab.jar org.ojalgo.benchmark.lab.FillByMultiplying
```

Replace `FillByMultiplying` with whichever benchmark you want to run. The sizes and libraries come from the `Size` – edit its constants to run a subset.

The jar's main class is JMH's own command line runner. Use it to list the available benchmarks, or for ad hoc runs:

```sh
java -jar target/ojlab.jar -l
```

Note that it does not use the settings defined in code, but JMH's defaults (5 forks, 5 warmup and 5 measurement iterations of 10s, ops/s) unless you specify otherwise. Restrict the parameters with `-p`, e.g. to compare ojAlgo with and without native code:

```sh
java -jar target/ojlab.jar org.ojalgo.benchmark.lab.FillByMultiplying -p lib=ojAlgo-JVM,ojAlgo-Native -p dim=100,1000 -f 1 -wi 2 -i 3 -tu m
```

### Native code

The forked JVMs are started with `--enable-native-access=ALL-UNNAMED` (an `@Fork` annotation on `MatrixBenchmarkOperation`). Each fork benchmarks a single library, and instantiating it decides whether ojAlgo or SMILE uses native code: `ojAlgo-JVM` and `SMILE-JVM` switch it off, `ojAlgo-Native` and `SMILE-Native` fail if there is no native library to use. Don't run without forks (`-f 0`).

To use a specific native library, set the `ojalgo.native.library` system property in the forked JVMs. Use `-jvmArgsPrepend` – `-jvmArgsAppend` would replace the `--enable-native-access` argument:

```sh
java -jar target/ojlab.jar org.ojalgo.benchmark.lab.FillByMultiplying -p lib=ojAlgo-Native -jvmArgsPrepend -Dojalgo.native.library=/opt/homebrew/opt/openblas/lib/libopenblas.dylib
```

SMILE has system properties of its own for that (`smile.lib.blas`, `blas.native.path`… – see `smile.linalg.NativeLibrary`).

## Results

Benchmark results are published at: https://www.ojalgo.org/category/benchmarks/
