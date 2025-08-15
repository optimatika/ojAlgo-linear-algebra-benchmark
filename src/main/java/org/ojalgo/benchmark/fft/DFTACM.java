package org.ojalgo.benchmark.fft;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.ojalgo.benchmark.MatrixBenchmarkOperation;
import org.ojalgo.data.transform.DiscreteFourierTransform;
import org.ojalgo.function.special.PowerOf2;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.R064Store;
import org.ojalgo.random.Uniform;
import org.ojalgo.scalar.ComplexNumber;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

/**
 * <pre>
Benchmark      (power)   Mode  Cnt           Score            Error    Units
DFTACM.ACM           0  thrpt    3  1992513164.319 ±  392936091.333  ops/min
DFTACM.ACM           1  thrpt    3  3125368136.277 ±  366726416.068  ops/min
DFTACM.ACM           2  thrpt    3  1979889077.962 ±  121525193.354  ops/min
DFTACM.ACM           3  thrpt    3  1032946642.667 ±  205406789.513  ops/min
DFTACM.ACM           4  thrpt    3   529857473.699 ±  211279392.823  ops/min
DFTACM.ACM           5  thrpt    3   238406341.600 ±    1656551.682  ops/min
DFTACM.ACM           6  thrpt    3   109661473.580 ±     190272.772  ops/min
DFTACM.ACM           7  thrpt    3    47975178.295 ±     301716.187  ops/min
DFTACM.ACM           8  thrpt    3    21811648.084 ±    1076649.381  ops/min
DFTACM.ACM           9  thrpt    3     9663209.107 ±    2032596.678  ops/min
DFTACM.ACM          10  thrpt    3     4053059.968 ±    2468844.188  ops/min
DFTACM.ACM          11  thrpt    3     1872370.827 ±      41984.759  ops/min
DFTACM.ACM          12  thrpt    3      865882.372 ±      67685.218  ops/min
DFTACM.ACM          13  thrpt    3      415401.686 ±      82144.607  ops/min
DFTACM.ACM          14  thrpt    3      182559.807 ±      13072.555  ops/min
DFTACM.ACM          15  thrpt    3       80237.961 ±      19724.278  ops/min
DFTACM.ACM          16  thrpt    3       36930.281 ±      15429.991  ops/min
DFTACM.ojAlgo        0  thrpt    3  2284364665.774 ± 3727463285.221  ops/min
DFTACM.ojAlgo        1  thrpt    3  2313443142.580 ± 2366416914.349  ops/min
DFTACM.ojAlgo        2  thrpt    3  1705149853.812 ±  250641427.185  ops/min
DFTACM.ojAlgo        3  thrpt    3   950827304.599 ±   61155208.289  ops/min
DFTACM.ojAlgo        4  thrpt    3   494535077.824 ±    5486357.169  ops/min
DFTACM.ojAlgo        5  thrpt    3   235019904.764 ±   38364784.023  ops/min
DFTACM.ojAlgo        6  thrpt    3   108900353.691 ±   23867068.045  ops/min
DFTACM.ojAlgo        7  thrpt    3    50859150.397 ±    1466289.052  ops/min
DFTACM.ojAlgo        8  thrpt    3    23692354.810 ±     283790.262  ops/min
DFTACM.ojAlgo        9  thrpt    3    10646597.884 ±      35124.282  ops/min
DFTACM.ojAlgo       10  thrpt    3     5084060.300 ±      43035.291  ops/min
DFTACM.ojAlgo       11  thrpt    3     2351560.879 ±     874561.180  ops/min
DFTACM.ojAlgo       12  thrpt    3     1136107.482 ±       5224.156  ops/min
DFTACM.ojAlgo       13  thrpt    3      519503.825 ±       4787.014  ops/min
DFTACM.ojAlgo       14  thrpt    3      242618.893 ±       1356.116  ops/min
DFTACM.ojAlgo       15  thrpt    3      115932.800 ±       1950.431  ops/min
DFTACM.ojAlgo       16  thrpt    3       51346.528 ±        803.834  ops/min
 * </pre>
 */
@State(Scope.Benchmark)
public class DFTACM {

    //  "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(DFTACM.class);
    }

    @Param({ "0", "1", "2", "3" })
    public int power;

    R064Store input;
    FastFourierTransformer transformerACM;
    DiscreteFourierTransform transformerOjAlgo;

    @Benchmark
    public Complex[] ACM() {
        return transformerACM.transform(input.data, TransformType.FORWARD);
    }

    @Benchmark
    public MatrixStore<ComplexNumber> ojAlgo() {
        return transformerOjAlgo.transform(input.data);
    }

    @Setup
    public void setup() {
        int size = PowerOf2.powerOfInt2(power);
        input = R064Store.FACTORY.makeFilled(size, 1, Uniform.of(-2, 4));
        transformerACM = new FastFourierTransformer(DftNormalization.STANDARD);
        transformerOjAlgo = DiscreteFourierTransform.newInstance(size);

    }

}
