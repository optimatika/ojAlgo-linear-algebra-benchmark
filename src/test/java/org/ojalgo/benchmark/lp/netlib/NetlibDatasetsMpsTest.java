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
import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.ojalgo.commons.math3.optim.linear.SolverCommonsMathSimplex;
import org.ojalgo.netio.BasicLogger;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.MathProgSysModel;
import org.ojalgo.optimisation.Optimisation.Result;
import org.ojalgo.optimisation.external.SolverCPLEX;
import org.ojalgo.type.context.NumberContext;

public class NetlibDatasetsMpsTest {

    private static final NumberContext PRECISION = new NumberContext(7, 6);
    private static final String SOLUTION_NOT_VALID = "Solution not valid!";
    private static final boolean DEBUG = true;

    public NetlibDatasetsMpsTest() {
        super();
    }

    @Test
    public void testAFIRO() {

        final BigDecimal maxValue = null;

        this.doTest("AFIRO", new BigDecimal("-.46475314286e+3"), maxValue);
    }

    void doTest(final String problem, final BigDecimal expectedMinimum, final BigDecimal expectedMaximum) {

        final ExpressionsBasedModel model = MathProgSysModel.make(new File(NetlibDatasetsMps.PATH + problem + ".SIF")).getExpressionsBasedModel();

        ExpressionsBasedModel.clearIntegrations();

        this.assertMinMaxVal(model, expectedMinimum, expectedMaximum);

        ExpressionsBasedModel.clearIntegrations();
        ExpressionsBasedModel.addIntegration(SolverCPLEX.INTEGRATION);

        this.assertMinMaxVal(model, expectedMinimum, expectedMaximum);

        ExpressionsBasedModel.clearIntegrations();
        ExpressionsBasedModel.addIntegration(SolverCommonsMathSimplex.INTEGRATION);

        this.assertMinMaxVal(model, expectedMinimum, expectedMaximum);
    }

    private void assertMinMaxVal(final ExpressionsBasedModel model, final BigDecimal expectedMinimum, final BigDecimal expectedMaximum) {

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
}
