# ojAlgo Linear Algebra Benchmark (ojLAB)

Benchmarks and internal tuning tools for [ojAlgo](https://github.com/optimatika/ojAlgo) linear algebra operations, using [JMH](https://openjdk.org/projects/code-tools/jmh/) (Java Microbenchmark Harness).

## What's in here

This project serves two purposes:

### Library comparison benchmarks

Comparing ojAlgo linear algebra operations against alternative Java libraries:

| Library | Why it's included |
|---------|-------------------|
| [EJML](https://ejml.org/) | Pure Java, no dependencies, high performance. Top quality pure Java alternative. |
| [MTJ](https://github.com/fommil/matrix-toolkits-java) | Wraps BLAS/LAPACK via netlib-java, automatically switching between Java and native code. On macOS it defaults to Apple's vecLib. Included as a native code performance reference. |
| [Apache Commons Math](https://commons.apache.org/proper/commons-math/) | The only linear algebra library backed by a large organisation (Apache). A baseline that alternatives must justify improving upon. |

Benchmark classes in `org.ojalgo.benchmark.lab` cover: matrix multiplication, addition, scaling, transpose, decompositions (EVD, SVD, Cholesky), solvers (general, Hermitian, least squares), inversion, determinant, and pseudoinverse.

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

Run a specific benchmark:

```sh
java -jar target/ojlab.jar org.ojalgo.benchmark.lab.FillByMultiplying
```

Replace `FillByMultiplying` with whichever benchmark you want to run. Use `-l` to list all available benchmarks:

```sh
java -jar target/ojlab.jar -l
```

## Results

Benchmark results are published at: https://www.ojalgo.org/category/benchmarks/
