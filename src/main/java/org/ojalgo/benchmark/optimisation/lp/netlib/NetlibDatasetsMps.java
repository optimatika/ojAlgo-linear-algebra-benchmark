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
package org.ojalgo.benchmark.optimisation.lp.netlib;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.ojalgo.ProgrammingError;
import org.ojalgo.benchmark.Benchmarks;
import org.ojalgo.commons.math3.optim.linear.SolverCommonsMath;
import org.ojalgo.joptimizer.SolverJOptimizer;
import org.ojalgo.netio.BasicLogger;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.ExpressionsBasedModel.Integration;
import org.ojalgo.optimisation.MathProgSysModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Optimisation.Result;
import org.ojalgo.optimisation.external.SolverCPLEX;
import org.ojalgo.optimisation.external.SolverGurobi;
import org.ojalgo.type.context.NumberContext;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

/**
 * <pre>
# Run complete. Total time: 00:02:57

Benchmark                 (model)     (solver)   Mode  Cnt     Score    Error  Units
NetlibDatasetsMps.solve  ADLITTLE       ojAlgo  thrpt    5   326.738 ±  1.349  ops/s
NetlibDatasetsMps.solve  ADLITTLE       Gurobi  thrpt    5   373.964 ± 84.863  ops/s
NetlibDatasetsMps.solve  ADLITTLE        CPLEX  thrpt    5   381.521 ± 74.064  ops/s
NetlibDatasetsMps.solve  ADLITTLE  CommonsMath  thrpt    5    13.938 ±  0.696  ops/s
NetlibDatasetsMps.solve  ADLITTLE   JOptimizer  thrpt    5     1.094 ±  0.006  ops/s
NetlibDatasetsMps.solve     AFIRO       ojAlgo  thrpt    5  4782.218 ± 35.770  ops/s
NetlibDatasetsMps.solve     AFIRO       Gurobi  thrpt    5  1184.851 ± 83.053  ops/s
NetlibDatasetsMps.solve     AFIRO        CPLEX  thrpt    5   960.374 ± 19.445  ops/s
NetlibDatasetsMps.solve     AFIRO  CommonsMath  thrpt    5   462.964 ±  2.857  ops/s
NetlibDatasetsMps.solve     AFIRO   JOptimizer  thrpt    5     9.138 ±  0.063  ops/s
 * </pre>
 *
 * <pre>
# Run complete. Total time: 00:15:39

Benchmark                 (model)  (solver)   Mode  Cnt     Score     Error  Units
NetlibDatasetsMps.solve    BRANDY    ojAlgo  thrpt    5    29.196 ±   3.059  ops/s
NetlibDatasetsMps.solve    BRANDY    Gurobi  thrpt    5   111.036 ±   5.275  ops/s
NetlibDatasetsMps.solve    BRANDY     CPLEX  thrpt    5   117.789 ±  82.828  ops/s
NetlibDatasetsMps.solve    BORE3D    ojAlgo  thrpt    5   111.338 ±   7.048  ops/s
NetlibDatasetsMps.solve    BORE3D    Gurobi  thrpt    5   231.145 ±  47.081  ops/s
NetlibDatasetsMps.solve    BORE3D     CPLEX  thrpt    5   209.177 ±  81.747  ops/s
NetlibDatasetsMps.solve   BOEING2    ojAlgo  thrpt    5    17.051 ±   4.779  ops/s
NetlibDatasetsMps.solve   BOEING2    Gurobi  thrpt    5   207.382 ±  21.972  ops/s
NetlibDatasetsMps.solve   BOEING2     CPLEX  thrpt    5   189.733 ±  96.891  ops/s
NetlibDatasetsMps.solve   BOEING1    ojAlgo  thrpt    5     0.041 ±   0.001  ops/s
NetlibDatasetsMps.solve   BOEING1    Gurobi  thrpt    5    50.872 ±   2.882  ops/s
NetlibDatasetsMps.solve   BOEING1     CPLEX  thrpt    5    55.084 ±  12.040  ops/s
NetlibDatasetsMps.solve     BLEND    ojAlgo  thrpt    5   228.887 ±  18.924  ops/s
NetlibDatasetsMps.solve     BLEND    Gurobi  thrpt    5   372.114 ±  67.487  ops/s
NetlibDatasetsMps.solve     BLEND     CPLEX  thrpt    5   317.094 ± 201.946  ops/s
NetlibDatasetsMps.solve  BEACONFD    ojAlgo  thrpt    5   221.678 ±  18.431  ops/s
NetlibDatasetsMps.solve  BEACONFD    Gurobi  thrpt    5   186.061 ±  11.842  ops/s
NetlibDatasetsMps.solve  BEACONFD     CPLEX  thrpt    5   176.775 ±  42.661  ops/s
NetlibDatasetsMps.solve     BANDM    ojAlgo  thrpt    5     5.361 ±   0.330  ops/s
NetlibDatasetsMps.solve     BANDM    Gurobi  thrpt    5    66.827 ±   5.738  ops/s
NetlibDatasetsMps.solve     BANDM     CPLEX  thrpt    5    55.624 ±  11.838  ops/s
NetlibDatasetsMps.solve      AGG3    ojAlgo  thrpt    5     2.370 ±   0.362  ops/s
NetlibDatasetsMps.solve      AGG3    Gurobi  thrpt    5    92.931 ±  19.391  ops/s
NetlibDatasetsMps.solve      AGG3     CPLEX  thrpt    5    88.960 ±  40.540  ops/s
NetlibDatasetsMps.solve      AGG2    ojAlgo  thrpt    5     2.689 ±   0.373  ops/s
NetlibDatasetsMps.solve      AGG2    Gurobi  thrpt    5    93.409 ±  14.101  ops/s
NetlibDatasetsMps.solve      AGG2     CPLEX  thrpt    5    89.896 ±  49.458  ops/s
NetlibDatasetsMps.solve       AGG    ojAlgo  thrpt    5    17.623 ±   1.595  ops/s
NetlibDatasetsMps.solve       AGG    Gurobi  thrpt    5   153.219 ±  21.460  ops/s
NetlibDatasetsMps.solve       AGG     CPLEX  thrpt    5   126.240 ±  62.653  ops/s
NetlibDatasetsMps.solve     AFIRO    ojAlgo  thrpt    5  5653.878 ± 350.552  ops/s
NetlibDatasetsMps.solve     AFIRO    Gurobi  thrpt    5  1301.682 ± 224.408  ops/s
NetlibDatasetsMps.solve     AFIRO     CPLEX  thrpt    5   900.559 ± 214.402  ops/s
NetlibDatasetsMps.solve  ADLITTLE    ojAlgo  thrpt    5   352.146 ±  21.066  ops/s
NetlibDatasetsMps.solve  ADLITTLE    Gurobi  thrpt    5   391.054 ±  26.545  ops/s
NetlibDatasetsMps.solve  ADLITTLE     CPLEX  thrpt    5   344.437 ±  90.112  ops/s
 * </pre>
 *
 * http://www.netlib.org/lp/data/readme
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class NetlibDatasetsMps {

    public static final String PATH = "./src/main/resources/netlib/";

    private static final boolean DEBUG = true;

    private static final NumberContext PRECISION = new NumberContext(7, 6);
    private static final String SOLUTION_NOT_VALID = "Solution not valid!";

    private static final String SUFFIX = ".SIF";

    static final Map<String, ExpressionsBasedModel.Integration<?>> INTEGRATIONS = new HashMap<>();

    static {
        INTEGRATIONS.put("CPLEX", SolverCPLEX.INTEGRATION);
        INTEGRATIONS.put("CommonsMath", SolverCommonsMath.INTEGRATION);
        INTEGRATIONS.put("JOptimizer", SolverJOptimizer.INTEGRATION);
        INTEGRATIONS.put("Gurobi", SolverGurobi.INTEGRATION);
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
     * <li>Remove all problems with SIF specific enties in the model files (SIF is a superset of MPS)</li>
     * </ol>
     */
    @Param({ "BRANDY", "BORE3D", "BOEING2", "BOEING1", "BLEND", "BEACONFD", "BANDM", "AGG3", "AGG2", "AGG", "AFIRO", "ADLITTLE" })
    public String model;

    @Param({ "ojAlgo", "Gurobi", "CPLEX" })
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

    @Test
    public void testAFIRO() {

        final BigDecimal maxValue = null;

        this.doTest("AFIRO", new BigDecimal("-.46475314286e+3"), maxValue);
    }

    @Test
    public void testDEGEN2() {

        final BigDecimal maxValue = null;

        this.doTest("CAPRI", new BigDecimal("2690.0129142514993"), maxValue);
    }

    private void assertMinMaxVal(final ExpressionsBasedModel model, final BigDecimal expectedMinimum, final BigDecimal expectedMaximum) {

        //model.options.debug(LinearSolver.class);

        Assert.assertTrue(model.validate());

        if (expectedMinimum != null) {

            final double expected = expectedMinimum.doubleValue();
            final Result minimum = model.minimise();
            if (DEBUG) {
                BasicLogger.debug("Minimum: {}", minimum);
            }
            final double actual = minimum.getValue();

            if (Double.isNaN(expected) && Double.isNaN(actual)) {

            } else if (PRECISION.isDifferent(expected, actual)) {
                Assert.assertEquals("Minimum", expected, actual, PRECISION.epsilon());
            }

            if (!model.validate(PRECISION)) {
                Assert.fail(SOLUTION_NOT_VALID);
            }
        }

        if (expectedMaximum != null) {

            final double expected = expectedMaximum.doubleValue();
            final Result maximum = model.maximise();
            if (DEBUG) {
                BasicLogger.debug("Maximum: {}", maximum);
            }
            final double actual = maximum.getValue();

            if (Double.isNaN(expected) && Double.isNaN(actual)) {

            } else if (PRECISION.isDifferent(expected, actual)) {
                Assert.assertEquals("Maximum", expected, actual, PRECISION.epsilon());
            }

            if (!model.validate(PRECISION)) {
                Assert.fail(SOLUTION_NOT_VALID);
            }
        }
    }

    void doTest(final String problem, final BigDecimal expectedMinimum, final BigDecimal expectedMaximum) {

        final ExpressionsBasedModel model = MathProgSysModel.make(new File(NetlibDatasetsMps.PATH + problem + ".SIF")).getExpressionsBasedModel();

        ExpressionsBasedModel.clearIntegrations();

        this.assertMinMaxVal(model, expectedMinimum, expectedMaximum);

        ExpressionsBasedModel.clearIntegrations();
        ExpressionsBasedModel.addIntegration(SolverCPLEX.INTEGRATION);

        this.assertMinMaxVal(model, expectedMinimum, expectedMaximum);

        ExpressionsBasedModel.clearIntegrations();
        ExpressionsBasedModel.addIntegration(SolverCommonsMath.INTEGRATION);

        this.assertMinMaxVal(model, expectedMinimum, expectedMaximum);

        ExpressionsBasedModel.clearIntegrations();
        ExpressionsBasedModel.addIntegration(SolverJOptimizer.INTEGRATION);

        this.assertMinMaxVal(model, expectedMinimum, expectedMaximum);
    }

}
