/*
 * Copyright 1997-2021 Optimatika
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
package org.ojalgo.benchmark.results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

import org.ojalgo.RecoverableCondition;
import org.ojalgo.benchmark.results.BenchmarkResultsParser.ParsedLineData;
import org.ojalgo.netio.BasicParser;
import org.ojalgo.netio.LineSplittingParser;
import org.ojalgo.netio.TableData;
import org.ojalgo.structure.Structure1D.IndexMapper;

public final class BenchmarkResultsParser implements BasicParser<ParsedLineData> {

    static class ParseConsumer implements Consumer<BenchmarkResultsParser.ParsedLineData> {

        private final TableData<Integer> myTable = new TableData<>(new IndexMapper<Integer>() {

            @Override
            public long toIndex(Integer key) {
                return key.longValue();
            }

            @Override
            public Integer toKey(long index) {
                return Integer.valueOf((int) index);
            }
        });

        public void accept(ParsedLineData row) {
            myTable.put(row.getSize(), row.getContestant(), row.getMeassurement());
        }

        void appendTo(Appendable appendable) {
            try {
                appendable.append(myTable.print());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static final class ParsedLineData {

        private final String myJVM;
        private final String myLibrary;
        private final int mySize;
        private final double myValue;

        ParsedLineData(int dim, String lib, String jvm, double value) {
            super();
            mySize = dim;
            myLibrary = lib;
            myJVM = jvm;
            myValue = value;
        }

        public String getContestant() {
            return myLibrary + " on " + myJVM;
        }

        public double getMeassurement() {
            return myValue;
        }

        public int getSize() {
            return mySize;
        }

        String getJVM() {
            return myJVM;
        }

        String getLibrary() {
            return myLibrary;
        }

    }

    public static void main(String[] args) {

        final ParseConsumer consumer = new ParseConsumer();

        File directory = new File(args[0]);
        File outputFile = new File(directory, args[1]);

        for (int i = 2; i < args.length; i += 2) {
            BenchmarkResultsParser parser = new BenchmarkResultsParser(args[i]);
            parser.parse(new File(directory, args[i + 1]), consumer);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            consumer.appendTo(writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final String myJVM;
    private final LineSplittingParser myLineSplittingParser = new LineSplittingParser();
    private boolean myRunComplete = false;

    BenchmarkResultsParser(String jvm) {
        super();
        myJVM = jvm;
    }

    public ParsedLineData parse(String line) throws RecoverableCondition {

        if (!myRunComplete) {
            if (line.startsWith("Benchmark")) {
                myRunComplete = true;
            }
            return null;
        }

        String[] split = myLineSplittingParser.parse(line);

        int dim = Integer.parseInt(split[1]);
        String lib = split[2];
        double throughput = Double.parseDouble(split[5]);

        return new ParsedLineData(dim, lib, myJVM, throughput);
    }

}
