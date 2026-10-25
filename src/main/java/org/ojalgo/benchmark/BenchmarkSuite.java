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
package org.ojalgo.benchmark;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.ojalgo.benchmark.MatrixBenchmarkOperation.Size;
import org.openjdk.jmh.runner.BenchmarkList;
import org.openjdk.jmh.runner.BenchmarkListEntry;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.format.OutputFormatFactory;
import org.openjdk.jmh.runner.options.VerboseMode;

/**
 * A suite is a sub-interface of this one. A benchmark (a {@link MatrixBenchmarkOperation} subclass) belongs
 * to the suites whose interfaces it implements.
 * <p>
 * Running a suite runs each of its benchmarks for each of its size ranges ({@link #SIZES}), and writes one
 * result file per combination, "operation-size" (e.g. "FillByMultiplying-MEDIUM.csv"), to the suite's
 * directory ({@link #RESULTS}). Only the result files that are missing are (re)computed: a run that is
 * terminated early leaves a subset of the files, and the next run continues with the rest. To recompute some
 * results, delete those files. A result file is only written when its run is complete, with a result for
 * every dim/lib combination, so an interrupted, or partly failed, run never leaves a file that counts as
 * done.
 * <p>
 * A suite can redeclare {@link #RESULTS} and {@link #SIZES} to get its own directory and size ranges.
 */
public interface BenchmarkSuite {

    /**
     * One result file of a suite – a benchmark run for a size range.
     */
    record Result(Class<? extends MatrixBenchmarkOperation> benchmark, Size size, Path file) {

        boolean exists() {
            return Files.exists(file);
        }

    }

    /**
     * The directory where a suite's result files are – by default {@link MatrixBenchmarkOperation#RESULTS},
     * where all benchmarks write their results, shared by all suites. Redeclare it in a suite to give it a
     * directory of its own.
     */
    Path RESULTS = MatrixBenchmarkOperation.RESULTS;

    /**
     * The size ranges a suite is run for, in the order they're run. Redeclare it in a suite to change it.
     */
    List<Size> SIZES = List.of(Size.MEDIUM, Size.SMALL, Size.LARGE);

    /**
     * The value of a suite's constant – the most specific declaration, the suite's own or an inherited one.
     */
    private static Object constant(final Class<? extends BenchmarkSuite> suite, final String name) {
        try {
            return suite.getField(name).get(null);
        } catch (ReflectiveOperationException cause) {
            throw new IllegalStateException(cause);
        }
    }

    private static List<Result> expected(final Class<? extends BenchmarkSuite> suite, final Path directory) {

        List<Class<? extends MatrixBenchmarkOperation>> benchmarks = BenchmarkSuite.benchmarks(suite);

        List<Result> retVal = new ArrayList<>();
        for (Size size : BenchmarkSuite.sizes(suite)) {
            for (Class<? extends MatrixBenchmarkOperation> benchmark : benchmarks) {
                retVal.add(new Result(benchmark, size, directory.resolve(MatrixBenchmarkOperation.resultFileName(benchmark, size))));
            }
        }
        return retVal;
    }

    /**
     * The benchmarks that belong to the suite – the benchmark classes (in the JMH generated benchmark list)
     * that implement the suite's interface, sorted by name.
     */
    static List<Class<? extends MatrixBenchmarkOperation>> benchmarks(final Class<? extends BenchmarkSuite> suite) {

        List<Class<? extends MatrixBenchmarkOperation>> retVal = new ArrayList<>();

        for (BenchmarkListEntry entry : BenchmarkList.defaultList().getAll(OutputFormatFactory.createFormatInstance(System.out, VerboseMode.SILENT),
                List.of())) {
            try {
                Class<?> clazz = Class.forName(entry.getUserClassQName());
                if (suite.isAssignableFrom(clazz) && MatrixBenchmarkOperation.class.isAssignableFrom(clazz) && !retVal.contains(clazz)) {
                    retVal.add(clazz.asSubclass(MatrixBenchmarkOperation.class));
                }
            } catch (ClassNotFoundException cause) {
                throw new IllegalStateException(cause);
            }
        }

        retVal.sort(Comparator.comparing(Class::getSimpleName));

        return retVal;
    }

    /**
     * The suite's directory – {@link #RESULTS} as declared by the suite, or inherited.
     */
    static Path directory(final Class<? extends BenchmarkSuite> suite) {
        return (Path) BenchmarkSuite.constant(suite, "RESULTS");
    }

    /**
     * The result files the suite's directory should contain – one per benchmark and size range, the size
     * ranges in the order they're run.
     */
    static List<Result> expected(final Class<? extends BenchmarkSuite> suite) {
        return BenchmarkSuite.expected(suite, BenchmarkSuite.directory(suite));
    }

    /**
     * The result files that are missing from the suite's directory, in the order they would be run.
     */
    static List<Result> missing(final Class<? extends BenchmarkSuite> suite) {
        return BenchmarkSuite.expected(suite).stream().filter(result -> !result.exists()).toList();
    }

    /**
     * Runs the benchmark/size combinations of the suite whose result files are missing from its directory.
     */
    static void run(final Class<? extends BenchmarkSuite> suite) throws RunnerException {
        BenchmarkSuite.run(suite, BenchmarkSuite.directory(suite));
    }

    /**
     * As {@link #run(Class)}, but with another directory than the suite's own.
     */
    static void run(final Class<? extends BenchmarkSuite> suite, final Path directory) throws RunnerException {

        List<Result> expected = BenchmarkSuite.expected(suite, directory);
        List<Result> missing = expected.stream().filter(result -> !result.exists()).toList();

        System.out.printf("%s: %d of %d result files in %s, %d missing%n", suite.getSimpleName(), expected.size() - missing.size(), expected.size(),
                directory.toAbsolutePath(), missing.size());
        for (Result result : missing) {
            System.out.println("    " + result.file().getFileName());
        }

        int count = 0;
        for (Result result : missing) {
            System.out.printf("%s: running %s (%d of %d)%n", suite.getSimpleName(), result.file().getFileName(), ++count, missing.size());
            if (!MatrixBenchmarkOperation.run(result.benchmark(), result.size(), directory)) {
                System.out.printf("%s: %s is incomplete – not saved%n", suite.getSimpleName(), result.file().getFileName());
            }
        }
    }

    /**
     * The size ranges the suite is run for – {@link #SIZES} as declared by the suite, or inherited.
     */
    static List<Size> sizes(final Class<? extends BenchmarkSuite> suite) {
        return ((List<?>) BenchmarkSuite.constant(suite, "SIZES")).stream().map(Size.class::cast).toList();
    }

}
