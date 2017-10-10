/*
 * Copyright 1997-2017 Optimatika Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions: The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ojalgo.benchmark.lp.netlib;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ojalgo.ProgrammingError;
import org.ojalgo.benchmark.Benchmarks;
import org.ojalgo.commons.math3.optim.linear.SolverCommonsMathSimplex;
import org.ojalgo.netio.BasicLogger;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.ExpressionsBasedModel.Integration;
import org.ojalgo.optimisation.MathProgSysModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Optimisation.Result;
import org.ojalgo.optimisation.external.SolverCPLEX;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

/**
 * <pre>
 * </pre>
 *
 * <pre>
# Run complete. Total time: 00:09:07

Benchmark                 (model)  (solver)   Mode  Cnt     Score     Error  Units
NetlibDatasetsMps.solve  ADLITTLE    ojAlgo  thrpt    5   311.480 ±   3.687  ops/s
NetlibDatasetsMps.solve  ADLITTLE     CPLEX  thrpt    5   386.155 ±  91.114  ops/s
NetlibDatasetsMps.solve     AFIRO    ojAlgo  thrpt    5  4874.084 ±  35.689  ops/s
NetlibDatasetsMps.solve     AFIRO     CPLEX  thrpt    5   971.838 ± 153.114  ops/s
NetlibDatasetsMps.solve     BLEND    ojAlgo  thrpt    5   228.255 ±   2.369  ops/s
NetlibDatasetsMps.solve     BLEND     CPLEX  thrpt    5   390.197 ±  78.885  ops/s
NetlibDatasetsMps.solve   BOEING2    ojAlgo  thrpt    5    15.525 ±   0.091  ops/s
NetlibDatasetsMps.solve   BOEING2     CPLEX  thrpt    5   217.058 ±  36.939  ops/s
NetlibDatasetsMps.solve    ISRAEL    ojAlgo  thrpt    5     8.635 ±   0.100  ops/s
NetlibDatasetsMps.solve    ISRAEL     CPLEX  thrpt    5   210.600 ±  54.473  ops/s
NetlibDatasetsMps.solve       KB2    ojAlgo  thrpt    5   845.047 ±   4.416  ops/s
NetlibDatasetsMps.solve       KB2     CPLEX  thrpt    5   654.401 ±  99.567  ops/s
NetlibDatasetsMps.solve     SC105    ojAlgo  thrpt    5   285.808 ±   2.196  ops/s
NetlibDatasetsMps.solve     SC105     CPLEX  thrpt    5   412.352 ± 135.847  ops/s
NetlibDatasetsMps.solve     SC205    ojAlgo  thrpt    5    41.299 ±   0.794  ops/s
NetlibDatasetsMps.solve     SC205     CPLEX  thrpt    5   210.636 ±  84.878  ops/s
NetlibDatasetsMps.solve     SC50A    ojAlgo  thrpt    5  1859.362 ±  42.104  ops/s
NetlibDatasetsMps.solve     SC50A     CPLEX  thrpt    5   669.061 ± 258.959  ops/s
NetlibDatasetsMps.solve     SC50B    ojAlgo  thrpt    5  1686.845 ±  63.617  ops/s
NetlibDatasetsMps.solve     SC50B     CPLEX  thrpt    5   740.260 ± 230.178  ops/s
NetlibDatasetsMps.solve  SCORPION    ojAlgo  thrpt    5    22.448 ±   0.883  ops/s
NetlibDatasetsMps.solve  SCORPION     CPLEX  thrpt    5   144.545 ±  20.824  ops/s
NetlibDatasetsMps.solve    SCTAP1    ojAlgo  thrpt    5    15.194 ±   0.395  ops/s
NetlibDatasetsMps.solve    SCTAP1     CPLEX  thrpt    5   113.291 ±  61.744  ops/s
NetlibDatasetsMps.solve   SHARE2B    ojAlgo  thrpt    5   206.374 ±   3.741  ops/s
NetlibDatasetsMps.solve   SHARE2B     CPLEX  thrpt    5   302.170 ±  92.957  ops/s
 * </pre>
 *
 * http://www.netlib.org/lp/data/readme
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class NetlibDatasetsMps {

    public static final String PATH = "./src/test/resources/netlib/";

    private static final String SUFFIX = ".SIF";

    static final Map<String, ExpressionsBasedModel.Integration<?>> INTEGRATIONS = new HashMap<>();
    static {
        INTEGRATIONS.put("CPLEX", SolverCPLEX.INTEGRATION);
        INTEGRATIONS.put("CommonsMath", SolverCommonsMathSimplex.INTEGRATION);
    }

    public static void main(final String[] args) throws RunnerException {
        Benchmarks.run(NetlibDatasetsMps.class);
    }

    /**
     * <ol>
     * <li>Start with all models listed at http://www.netlib.org/lp/data/readme</li>
     * <li>Remove all problems with number of rows or columns > 1000 (since we use the community edition of
     * CPLEX as a reference).</li>
     * <li>Remove the problems not available at http://www.numerical.rl.ac.uk/cute/netlib.html (The actual
     * model files used here comes from that site.)</li>
     * </ol>
     */
    @Param({ "ADLITTLE", "AFIRO", "AGG", "AGG2", "AGG3" })
    public String model;

    @Param({ "ojAlgo", "CPLEX" })
    public String solver;
    private MathProgSysModel parsedMPS;

    public NetlibDatasetsMps() {
        super();
    }

    @Setup
    public void setup() {

        final File tmpFile = new File(PATH + model + SUFFIX);
        parsedMPS = MathProgSysModel.make(tmpFile);

        ExpressionsBasedModel.clearIntegrations();
        ExpressionsBasedModel.addIntegration(SolverCPLEX.INTEGRATION);

        final Result expected = parsedMPS.solve();

        ExpressionsBasedModel.clearIntegrations();
        final Integration<?> integration = INTEGRATIONS.get(solver);
        if (integration != null) {
            ExpressionsBasedModel.addIntegration(integration);
        }

        final Result actual = parsedMPS.solve();

        if (Math.abs((actual.getValue() - expected.getValue()) / expected.getValue()) >= 0.01) {
            BasicLogger.debug();
            BasicLogger.debug("Expected: {}", expected);
            BasicLogger.debug("Actual  : {}", actual);
            throw new ProgrammingError("Error too big!");
        }
    }

    @Benchmark
    public Optimisation.Result solve() {
        return parsedMPS.solve();
    }

}
