/*
 * Copyright 1997-2017 Optimatika (www.optimatika.se)
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

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public abstract class Benchmarks {

    private static final TimeValue ONE_MINUTE = new TimeValue(1L, TimeUnit.MINUTES);

    public static void run(final Class<?> clazz) throws RunnerException {
        new Runner(Benchmarks.options().include(clazz.getSimpleName()).build()).run();
    }

    protected static ChainedOptionsBuilder options() {
        return new OptionsBuilder().forks(1).measurementIterations(5).warmupIterations(9).mode(Mode.Throughput).timeUnit(TimeUnit.SECONDS).timeout(ONE_MINUTE)
                .jvmArgs("-server", "-Xmx6g",
                        "-Djava.library.path=/Library/gurobi751/mac64/lib:/Users/apete/Applications/IBM/ILOG/CPLEX_Studio_Community1262/cplex/bin/x86-64_osx");
    }

    protected Benchmarks() {
        super();
    }

}
