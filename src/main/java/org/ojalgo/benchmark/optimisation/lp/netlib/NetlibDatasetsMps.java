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
import org.ojalgo.optimisation.external.SolverMosek;
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
Result "org.ojalgo.benchmark.optimisation.lp.netlib.NetlibDatasetsMps.solve":
  351.404 ±(99.9%) 173.328 ops/s [Average]
  (min, avg, max) = (281.136, 351.404, 393.905), stdev = 45.013
  CI (99.9%): [178.076, 524.732] (assumes normal distribution)


# Run complete. Total time: 00:31:23

Benchmark                 (model)  (solver)   Mode  Cnt     Score     Error  Units
NetlibDatasetsMps.solve   FORPLAN    ojAlgo  thrpt    5    18.736 ±   0.669  ops/s
NetlibDatasetsMps.solve   FORPLAN    Gurobi  thrpt    5    74.065 ±  17.320  ops/s
NetlibDatasetsMps.solve   FORPLAN     CPLEX  thrpt    5    98.284 ±  50.223  ops/s
NetlibDatasetsMps.solve    FINNIS    ojAlgo  thrpt    5     0.014 ±   0.001  ops/s
NetlibDatasetsMps.solve    FINNIS    Gurobi  thrpt    5    77.512 ±  30.214  ops/s
NetlibDatasetsMps.solve    FINNIS     CPLEX  thrpt    5    74.862 ±  14.698  ops/s
NetlibDatasetsMps.solve  FFFFF800    ojAlgo  thrpt    5     1.795 ±   0.078  ops/s
NetlibDatasetsMps.solve  FFFFF800    Gurobi  thrpt    5    55.906 ±   8.481  ops/s
NetlibDatasetsMps.solve  FFFFF800     CPLEX  thrpt    5    60.005 ±  27.839  ops/s
NetlibDatasetsMps.solve  ETAMACRO    ojAlgo  thrpt    5     2.547 ±   0.094  ops/s
NetlibDatasetsMps.solve  ETAMACRO    Gurobi  thrpt    5    54.603 ±   3.501  ops/s
NetlibDatasetsMps.solve  ETAMACRO     CPLEX  thrpt    5    44.829 ±  12.830  ops/s
NetlibDatasetsMps.solve      E226    ojAlgo  thrpt    5     7.338 ±   0.231  ops/s
NetlibDatasetsMps.solve      E226    Gurobi  thrpt    5    70.537 ±   2.447  ops/s
NetlibDatasetsMps.solve      E226     CPLEX  thrpt    5    84.089 ±  37.586  ops/s
NetlibDatasetsMps.solve     CAPRI    ojAlgo  thrpt    5     2.916 ±   0.149  ops/s
NetlibDatasetsMps.solve     CAPRI    Gurobi  thrpt    5   109.458 ±  35.618  ops/s
NetlibDatasetsMps.solve     CAPRI     CPLEX  thrpt    5   122.432 ±  25.916  ops/s
NetlibDatasetsMps.solve    BRANDY    ojAlgo  thrpt    5    24.698 ±   0.718  ops/s
NetlibDatasetsMps.solve    BRANDY    Gurobi  thrpt    5   113.142 ±  10.603  ops/s
NetlibDatasetsMps.solve    BRANDY     CPLEX  thrpt    5   119.273 ±  41.354  ops/s
NetlibDatasetsMps.solve    BORE3D    ojAlgo  thrpt    5    96.059 ±   2.708  ops/s
NetlibDatasetsMps.solve    BORE3D    Gurobi  thrpt    5   235.608 ± 103.447  ops/s
NetlibDatasetsMps.solve    BORE3D     CPLEX  thrpt    5   226.258 ±  35.547  ops/s
NetlibDatasetsMps.solve   BOEING2    ojAlgo  thrpt    5    15.326 ±   0.144  ops/s
NetlibDatasetsMps.solve   BOEING2    Gurobi  thrpt    5   203.861 ±  60.684  ops/s
NetlibDatasetsMps.solve   BOEING2     CPLEX  thrpt    5   209.409 ±  32.679  ops/s
NetlibDatasetsMps.solve   BOEING1    ojAlgo  thrpt    5     1.056 ±   0.118  ops/s
NetlibDatasetsMps.solve   BOEING1    Gurobi  thrpt    5    50.268 ±  12.333  ops/s
NetlibDatasetsMps.solve   BOEING1     CPLEX  thrpt    5    51.795 ±  22.695  ops/s
NetlibDatasetsMps.solve     BLEND    ojAlgo  thrpt    5   226.831 ±  19.714  ops/s
NetlibDatasetsMps.solve     BLEND    Gurobi  thrpt    5   407.130 ±  14.473  ops/s
NetlibDatasetsMps.solve     BLEND     CPLEX  thrpt    5   378.324 ±  89.857  ops/s
NetlibDatasetsMps.solve  BEACONFD    ojAlgo  thrpt    5   209.564 ±   4.081  ops/s
NetlibDatasetsMps.solve  BEACONFD    Gurobi  thrpt    5   193.261 ±  23.756  ops/s
NetlibDatasetsMps.solve  BEACONFD     CPLEX  thrpt    5   184.886 ±  34.528  ops/s
NetlibDatasetsMps.solve     BANDM    ojAlgo  thrpt    5     4.606 ±   0.130  ops/s
NetlibDatasetsMps.solve     BANDM    Gurobi  thrpt    5    65.904 ±   1.958  ops/s
NetlibDatasetsMps.solve     BANDM     CPLEX  thrpt    5    51.556 ±  21.445  ops/s
NetlibDatasetsMps.solve      AGG3    ojAlgo  thrpt    5     2.622 ±   0.187  ops/s
NetlibDatasetsMps.solve      AGG3    Gurobi  thrpt    5    91.625 ±  24.865  ops/s
NetlibDatasetsMps.solve      AGG3     CPLEX  thrpt    5    95.836 ±  25.064  ops/s
NetlibDatasetsMps.solve      AGG2    ojAlgo  thrpt    5     2.927 ±   0.164  ops/s
NetlibDatasetsMps.solve      AGG2    Gurobi  thrpt    5    94.510 ±  25.689  ops/s
NetlibDatasetsMps.solve      AGG2     CPLEX  thrpt    5    96.830 ±  18.554  ops/s
NetlibDatasetsMps.solve       AGG    ojAlgo  thrpt    5    15.654 ±   0.582  ops/s
NetlibDatasetsMps.solve       AGG    Gurobi  thrpt    5   158.593 ±  12.538  ops/s
NetlibDatasetsMps.solve       AGG     CPLEX  thrpt    5   142.258 ±  35.565  ops/s
NetlibDatasetsMps.solve     AFIRO    ojAlgo  thrpt    5  4748.837 ± 127.660  ops/s
NetlibDatasetsMps.solve     AFIRO    Gurobi  thrpt    5  1323.479 ± 370.478  ops/s
NetlibDatasetsMps.solve     AFIRO     CPLEX  thrpt    5   959.615 ±  90.741  ops/s
NetlibDatasetsMps.solve  ADLITTLE    ojAlgo  thrpt    5   319.216 ±   7.631  ops/s
NetlibDatasetsMps.solve  ADLITTLE    Gurobi  thrpt    5   410.718 ± 112.237  ops/s
NetlibDatasetsMps.solve  ADLITTLE     CPLEX  thrpt    5   351.404 ± 173.328  ops/s
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
        INTEGRATIONS.put("CommonsMath", SolverCommonsMath.INTEGRATION);
        INTEGRATIONS.put("CPLEX", SolverCPLEX.INTEGRATION);
        INTEGRATIONS.put("Gurobi", SolverGurobi.INTEGRATION);
        INTEGRATIONS.put("JOptimizer", SolverJOptimizer.INTEGRATION);
        INTEGRATIONS.put("Mosek", SolverMosek.INTEGRATION);
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
    @Param({ "STOCFOR1", "STAIR", "SHARE2B", "SHARE1B", "SCTAP1", "SCSD1", "SCORPION", "SCFXM2", "SCFXM1", "SCAGR7", "SCAGR25", "SC50B", "SC50A", "SC205",
            "SC105", "LOTFI", "KB2", "ISRAEL", "GROW7", "GROW22", "FORPLAN", "FINNIS", "FFFFF800", "ETAMACRO", "E226", "CAPRI", "BRANDY", "BORE3D", "BOEING2",
            "BOEING1", "BLEND", "BEACONFD", "BANDM", "AGG3", "AGG2", "AGG", "AFIRO", "ADLITTLE" })
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
