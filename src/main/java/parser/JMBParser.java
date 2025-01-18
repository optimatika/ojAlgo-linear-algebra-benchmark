/*
 * Copyright 1997-2025 Optimatika
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
package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.ojalgo.netio.BasicLogger;
import org.ojalgo.random.SampleSet;
import org.ojalgo.structure.Access1D;

public class JMBParser {

    public JMBParser() {
    }

    public static void main(String[] args) {
        File file1 = new File(
                "/Users/apete/Documents/Optimatika/Project/ojAlgo/TheBigBenchmark/instance-2/Java-Matrix-Benchmark/results/runtime_04-04-2018-17_45_49/ojAlgo/invert.csv");
        File file2 = new File(
                "/Users/apete/Documents/Optimatika/Project/ojAlgo/TheBigBenchmark/instance-1/Java-Matrix-Benchmark/results/runtime_04-04-2018-17_46_33/ojAlgo/invert.csv");

        Map<String, Map<Integer, Double>> mapper = new HashMap<>();

        JMBParser.parseOneFile(file1, mapper, "Oracle 8");

        JMBParser.parseOneFile(file2, mapper, "Zing 8");

        BasicLogger.debug(mapper);

        Set<Integer> dims = mapper.get("Oracle 8").keySet();
        for (Integer integer : dims) {
            double ora8 = mapper.get("Oracle 8").get(integer);
            double zng8 = mapper.get("Zing 8").get(integer);
            BasicLogger.debug("{} = Oracle 8: {} Zing 8: {} Quotient: {}", integer, ora8, zng8, zng8 / ora8);

        }
    }

    static void parseOneFile(final File file, Map<String, Map<Integer, Double>> collector, String jvm) {

        Map<Integer, Double> map = new TreeMap<>();

        String library = null;
        String operation = null;

        int dim;
        int ops;

        if (file.exists() && file.isFile() && file.canRead()) {

            String line = null;

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split("\\s+");
                    if (library == null) {
                        library = split[0];
                        operation = split[1];
                        collector.put(jvm, map);
                    } else {
                        dim = Integer.parseInt(split[0]);
                        ops = Integer.parseInt(split[1]);
                        if (ops > 0) {
                            double[] values = new double[ops];
                            for (int i = 0; i < ops; i++) {
                                line = reader.readLine();
                                if (line != null) {
                                    values[i] = Double.parseDouble(line.trim());
                                }
                            }
                            double max = SampleSet.wrap(Access1D.wrap(values)).getMaximum();
                            map.put(dim, max);
                        }
                    }
                }
            } catch (final IOException exception) {
                exception.printStackTrace();
            }

        }

        BasicLogger.debug(map);
    }

}
