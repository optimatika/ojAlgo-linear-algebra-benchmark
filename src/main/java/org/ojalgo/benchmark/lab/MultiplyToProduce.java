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
package org.ojalgo.benchmark.lab;

import org.ojalgo.benchmark.BenchmarkRequirementsException;
import org.ojalgo.benchmark.MatrixBenchmarkLibrary;
import org.ojalgo.benchmark.MatrixBenchmarkOperation;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.RunnerException;

/**
 * <h1>Mac Pro (Early 2009)</h1>
 * <h2>2017-11-27</h2>
 *
 * <pre>
Result "org.ojalgo.benchmark.matrix.suite.Square3Multiply.execute":
  212,359 ±(99.9%) 5,171 ops/min [Average]
  (min, avg, max) = (212,087, 212,359, 212,653), stdev = 0,283
  CI (99.9%): [207,188, 217,531] (assumes normal distribution)


# Run complete. Total time: 00:54:41

Benchmark                (dim)  (library)   Mode  Cnt           Score          Error    Units
Square3Multiply.execute      1        ACM  thrpt    3   254923632,405 ±  1314836,835  ops/min
Square3Multiply.execute      1       EJML  thrpt    3  2500753621,500 ± 78817879,533  ops/min
Square3Multiply.execute      1     ojAlgo  thrpt    3   728514474,369 ± 14262656,373  ops/min
Square3Multiply.execute      1        MTJ  thrpt    3    58014815,314 ±  4648269,211  ops/min
Square3Multiply.execute      2        ACM  thrpt    3   155190106,022 ± 23912485,977  ops/min
Square3Multiply.execute      2       EJML  thrpt    3  1138062984,439 ± 22470058,902  ops/min
Square3Multiply.execute      2     ojAlgo  thrpt    3   671971441,655 ± 15911558,485  ops/min
Square3Multiply.execute      2        MTJ  thrpt    3    52876978,818 ± 17800560,300  ops/min
Square3Multiply.execute      3        ACM  thrpt    3   106512669,418 ±  5369324,972  ops/min
Square3Multiply.execute      3       EJML  thrpt    3   508622972,387 ±  2678019,055  ops/min
Square3Multiply.execute      3     ojAlgo  thrpt    3   524220281,914 ±  9675485,448  ops/min
Square3Multiply.execute      3        MTJ  thrpt    3    50096363,636 ± 15843984,950  ops/min
Square3Multiply.execute      4        ACM  thrpt    3    80183256,133 ±  1336210,416  ops/min
Square3Multiply.execute      4       EJML  thrpt    3   279659724,193 ± 13601635,557  ops/min
Square3Multiply.execute      4     ojAlgo  thrpt    3   404210764,832 ± 10621559,325  ops/min
Square3Multiply.execute      4        MTJ  thrpt    3    52239956,444 ±  5820864,253  ops/min
Square3Multiply.execute      5        ACM  thrpt    3    59167173,032 ±  3978791,816  ops/min
Square3Multiply.execute      5       EJML  thrpt    3   184000448,543 ± 97436340,890  ops/min
Square3Multiply.execute      5     ojAlgo  thrpt    3   286566596,176 ±  8822935,637  ops/min
Square3Multiply.execute      5        MTJ  thrpt    3    47162928,357 ± 11114329,421  ops/min
Square3Multiply.execute      8        ACM  thrpt    3    26414221,835 ±  2452259,131  ops/min
Square3Multiply.execute      8       EJML  thrpt    3    59412081,070 ± 27516531,608  ops/min
Square3Multiply.execute      8     ojAlgo  thrpt    3    85173662,895 ±  1856858,630  ops/min
Square3Multiply.execute      8        MTJ  thrpt    3    42677050,328 ±  8787178,328  ops/min
Square3Multiply.execute     10        ACM  thrpt    3    17141411,631 ±  1581589,998  ops/min
Square3Multiply.execute     10       EJML  thrpt    3    34369482,925 ±  5586231,462  ops/min
Square3Multiply.execute     10     ojAlgo  thrpt    3    46858546,216 ±  1143289,381  ops/min
Square3Multiply.execute     10        MTJ  thrpt    3    34851479,436 ±  4795575,084  ops/min
Square3Multiply.execute     16        ACM  thrpt    3     5636173,330 ±   559813,545  ops/min
Square3Multiply.execute     16       EJML  thrpt    3     8114357,377 ±   399565,775  ops/min
Square3Multiply.execute     16     ojAlgo  thrpt    3     6784038,936 ±   203454,487  ops/min
Square3Multiply.execute     16        MTJ  thrpt    3    12963026,344 ±   433471,593  ops/min
Square3Multiply.execute     20        ACM  thrpt    3     3215814,367 ±   225774,576  ops/min
Square3Multiply.execute     20       EJML  thrpt    3     4327959,426 ±   425564,568  ops/min
Square3Multiply.execute     20     ojAlgo  thrpt    3     4781362,014 ±   319495,067  ops/min
Square3Multiply.execute     20        MTJ  thrpt    3     9939781,606 ±   203335,383  ops/min
Square3Multiply.execute     32        ACM  thrpt    3      898526,788 ±    89997,385  ops/min
Square3Multiply.execute     32       EJML  thrpt    3     1282061,625 ±    52718,507  ops/min
Square3Multiply.execute     32     ojAlgo  thrpt    3     1344815,075 ±    56949,822  ops/min
Square3Multiply.execute     32        MTJ  thrpt    3     4304806,265 ±   264865,780  ops/min
Square3Multiply.execute     50        ACM  thrpt    3      257661,112 ±   112584,263  ops/min
Square3Multiply.execute     50       EJML  thrpt    3      386493,188 ±   568671,712  ops/min
Square3Multiply.execute     50     ojAlgo  thrpt    3      544612,125 ±    52191,510  ops/min
Square3Multiply.execute     50        MTJ  thrpt    3     1501190,538 ±    62281,125  ops/min
Square3Multiply.execute     64        ACM  thrpt    3      126207,483 ±      644,825  ops/min
Square3Multiply.execute     64       EJML  thrpt    3      194290,342 ±    12174,269  ops/min
Square3Multiply.execute     64     ojAlgo  thrpt    3      388631,374 ±    41363,149  ops/min
Square3Multiply.execute     64        MTJ  thrpt    3      785752,323 ±    41497,111  ops/min
Square3Multiply.execute    100        ACM  thrpt    3       25556,521 ±    20298,376  ops/min
Square3Multiply.execute    100       EJML  thrpt    3       55023,655 ±     5158,811  ops/min
Square3Multiply.execute    100     ojAlgo  thrpt    3      127988,025 ±    26763,907  ops/min
Square3Multiply.execute    100        MTJ  thrpt    3      226765,283 ±     2674,363  ops/min
Square3Multiply.execute    128        ACM  thrpt    3       14982,081 ±     6601,730  ops/min
Square3Multiply.execute    128       EJML  thrpt    3       26580,042 ±     8341,526  ops/min
Square3Multiply.execute    128     ojAlgo  thrpt    3       92891,864 ±    12526,132  ops/min
Square3Multiply.execute    128        MTJ  thrpt    3      223227,275 ±    16219,521  ops/min
Square3Multiply.execute    200        ACM  thrpt    3        2588,314 ±     1188,978  ops/min
Square3Multiply.execute    200       EJML  thrpt    3        6873,519 ±     1557,445  ops/min
Square3Multiply.execute    200     ojAlgo  thrpt    3       36914,429 ±     5533,042  ops/min
Square3Multiply.execute    200        MTJ  thrpt    3      102790,291 ±    39867,801  ops/min
Square3Multiply.execute    256        ACM  thrpt    3         983,619 ±        5,487  ops/min
Square3Multiply.execute    256       EJML  thrpt    3        3396,727 ±      283,919  ops/min
Square3Multiply.execute    256     ojAlgo  thrpt    3       20025,949 ±     1486,603  ops/min
Square3Multiply.execute    256        MTJ  thrpt    3       67420,027 ±     5711,454  ops/min
Square3Multiply.execute    500        ACM  thrpt    3          53,291 ±       10,825  ops/min
Square3Multiply.execute    500       EJML  thrpt    3         455,094 ±        9,277  ops/min
Square3Multiply.execute    500     ojAlgo  thrpt    3        2796,945 ±      504,576  ops/min
Square3Multiply.execute    500        MTJ  thrpt    3       11484,187 ±      693,510  ops/min
Square3Multiply.execute    512        ACM  thrpt    3          51,758 ±        4,563  ops/min
Square3Multiply.execute    512       EJML  thrpt    3         426,652 ±        6,959  ops/min
Square3Multiply.execute    512     ojAlgo  thrpt    3        2636,663 ±      461,709  ops/min
Square3Multiply.execute    512        MTJ  thrpt    3        9527,006 ±    17623,000  ops/min
Square3Multiply.execute   1000        ACM  thrpt    3           3,451 ±        0,298  ops/min
Square3Multiply.execute   1000       EJML  thrpt    3          48,942 ±        3,935  ops/min
Square3Multiply.execute   1000     ojAlgo  thrpt    3         281,303 ±       32,419  ops/min
Square3Multiply.execute   1000        MTJ  thrpt    3        1512,186 ±      423,352  ops/min
Square3Multiply.execute   1024        ACM  thrpt    3           3,434 ±        1,734  ops/min
Square3Multiply.execute   1024       EJML  thrpt    3          45,120 ±        1,260  ops/min
Square3Multiply.execute   1024     ojAlgo  thrpt    3         261,811 ±      131,884  ops/min
Square3Multiply.execute   1024        MTJ  thrpt    3        1371,107 ±     1479,895  ops/min
Square3Multiply.execute   2000        ACM  thrpt    3           0,318 ±        0,102  ops/min
Square3Multiply.execute   2000       EJML  thrpt    3           5,392 ±        2,494  ops/min
Square3Multiply.execute   2000     ojAlgo  thrpt    3          14,289 ±        0,195  ops/min
Square3Multiply.execute   2000        MTJ  thrpt    3         212,359 ±        5,171  ops/min
 * </pre>
 *
 * <h2>2016-06-16</h2>
 *
 * <pre>
Benchmark                         (dim)  (library)   Mode  Cnt           Score          Error    Units
SquareMultiply.execute                2       EJML  thrpt   15  1115025722,165 ±  2185660,139  ops/min
SquareMultiply.execute                2        MTJ  thrpt   15    68903433,462 ±  1017964,935  ops/min
SquareMultiply.execute                2     ojAlgo  thrpt   15   749118803,207 ±  1101357,259  ops/min
SquareMultiply.execute                3       EJML  thrpt   15   525499968,364 ± 12822243,964  ops/min
SquareMultiply.execute                3        MTJ  thrpt   15    64091505,436 ±   253494,269  ops/min
SquareMultiply.execute                3     ojAlgo  thrpt   15   607068982,868 ± 19334929,203  ops/min
SquareMultiply.execute                4       EJML  thrpt   15   290721400,796 ±  5505098,300  ops/min
SquareMultiply.execute                4        MTJ  thrpt   15    64213979,990 ±  1569457,469  ops/min
SquareMultiply.execute                4     ojAlgo  thrpt   15   431863499,150 ±  1219953,745  ops/min
SquareMultiply.execute                5       EJML  thrpt   15   207672663,239 ±   283047,224  ops/min
SquareMultiply.execute                5        MTJ  thrpt   15    58103475,106 ±   526325,218  ops/min
SquareMultiply.execute                5     ojAlgo  thrpt   15   295220540,058 ±   621850,718  ops/min
SquareMultiply.execute               10       EJML  thrpt   15    37466146,807 ±   109065,459  ops/min
SquareMultiply.execute               10        MTJ  thrpt   15    39134087,920 ±   459558,234  ops/min
SquareMultiply.execute               10     ojAlgo  thrpt   15    44095203,535 ±   330452,705  ops/min
SquareMultiply.execute               20       EJML  thrpt   15     4825252,992 ±     6117,057  ops/min
SquareMultiply.execute               20        MTJ  thrpt   15    12974567,585 ±   105762,572  ops/min
SquareMultiply.execute               20     ojAlgo  thrpt   15     5429933,327 ±     9153,587  ops/min
SquareMultiply.execute               50       EJML  thrpt   15      392668,430 ±     4252,602  ops/min
SquareMultiply.execute               50        MTJ  thrpt   15     1550932,505 ±     9020,498  ops/min
SquareMultiply.execute               50     ojAlgo  thrpt   15      599785,177 ±     2137,223  ops/min
SquareMultiply.execute              100       EJML  thrpt   15       53032,006 ±      485,635  ops/min
SquareMultiply.execute              100        MTJ  thrpt   15      226246,179 ±     1207,405  ops/min
SquareMultiply.execute              100     ojAlgo  thrpt   15      130688,485 ±     5980,605  ops/min
SquareMultiply.execute              200       EJML  thrpt   15        7319,607 ±       13,978  ops/min
SquareMultiply.execute              200        MTJ  thrpt   15      104275,723 ±     1055,315  ops/min
SquareMultiply.execute              200     ojAlgo  thrpt   15       40537,583 ±      393,264  ops/min
SquareMultiply.execute              500       EJML  thrpt   15         492,080 ±        2,428  ops/min
SquareMultiply.execute              500        MTJ  thrpt   15       11098,306 ±      153,936  ops/min
SquareMultiply.execute              500     ojAlgo  thrpt   15        2838,004 ±      125,937  ops/min
SquareMultiply.execute             1000       EJML  thrpt   15          50,340 ±        3,281  ops/min
SquareMultiply.execute             1000        MTJ  thrpt   15        1598,225 ±      117,480  ops/min
SquareMultiply.execute             1000     ojAlgo  thrpt   15         418,851 ±       16,176  ops/min
 * </pre>
 *
 * <h1>MacBook Air</h1>
 * <h2>2015-06-18</h2>
 *
 * <pre>
 * # Run complete. Total time: 00:45:32
 *
 * Benchmark               (dim)  (library)   Mode  Cnt           Score           Error    Units
 * SquareMultiply.execute      2       EJML  thrpt    3  1195467779,203 ± 226472548,806  ops/min
 * SquareMultiply.execute      2        MTJ  thrpt    3    68093507,462 ±  28552442,060  ops/min
 * SquareMultiply.execute      2     ojAlgo  thrpt    3   791236224,141 ±  74420268,703  ops/min
 * SquareMultiply.execute      3       EJML  thrpt    3   597660733,292 ± 210147150,484  ops/min
 * SquareMultiply.execute      3        MTJ  thrpt    3    59198262,305 ±   8731345,406  ops/min
 * SquareMultiply.execute      3     ojAlgo  thrpt    3   635231267,538 ±  41113421,087  ops/min
 * SquareMultiply.execute      4       EJML  thrpt    3   320325320,379 ± 162160440,911  ops/min
 * SquareMultiply.execute      4        MTJ  thrpt    3    66918434,519 ±  15460325,103  ops/min
 * SquareMultiply.execute      4     ojAlgo  thrpt    3   502872013,149 ± 246595742,019  ops/min
 * SquareMultiply.execute      5       EJML  thrpt    3   229686848,134 ± 276451890,246  ops/min
 * SquareMultiply.execute      5        MTJ  thrpt    3    60015171,918 ±  30914476,116  ops/min
 * SquareMultiply.execute      5     ojAlgo  thrpt    3   337204180,362 ± 241545524,599  ops/min
 * SquareMultiply.execute     10       EJML  thrpt    3    43385012,345 ±  19612952,742  ops/min
 * SquareMultiply.execute     10        MTJ  thrpt    3    43092775,029 ±   4934355,681  ops/min
 * SquareMultiply.execute     10     ojAlgo  thrpt    3    62519765,560 ±   4420042,551  ops/min
 * SquareMultiply.execute     20       EJML  thrpt    3     5408286,250 ±   1521352,504  ops/min
 * SquareMultiply.execute     20        MTJ  thrpt    3    13886250,404 ±  14548216,378  ops/min
 * SquareMultiply.execute     20     ojAlgo  thrpt    3     5964922,895 ±    837405,145  ops/min
 * SquareMultiply.execute     50       EJML  thrpt    3      420594,691 ±     93068,640  ops/min
 * SquareMultiply.execute     50        MTJ  thrpt    3     2476871,010 ±   1462376,949  ops/min
 * SquareMultiply.execute     50     ojAlgo  thrpt    3      417283,841 ±     90941,821  ops/min
 * SquareMultiply.execute    100       EJML  thrpt    3       56811,214 ±     38292,556  ops/min
 * SquareMultiply.execute    100        MTJ  thrpt    3      379821,916 ±     72654,788  ops/min
 * SquareMultiply.execute    100     ojAlgo  thrpt    3       90464,045 ±     24162,344  ops/min
 * SquareMultiply.execute    200       EJML  thrpt    3        8181,786 ±      6394,668  ops/min
 * SquareMultiply.execute    200        MTJ  thrpt    3       68437,831 ±     51847,412  ops/min
 * SquareMultiply.execute    200     ojAlgo  thrpt    3       12869,488 ±      4879,337  ops/min
 * SquareMultiply.execute    500       EJML  thrpt    3         544,871 ±       278,519  ops/min
 * SquareMultiply.execute    500        MTJ  thrpt    3        5140,720 ±       720,539  ops/min
 * SquareMultiply.execute    500     ojAlgo  thrpt    3         996,341 ±       318,266  ops/min
 * SquareMultiply.execute   1000       EJML  thrpt    3          56,580 ±        21,000  ops/min
 * SquareMultiply.execute   1000        MTJ  thrpt    3         572,412 ±      2156,684  ops/min
 * SquareMultiply.execute   1000     ojAlgo  thrpt    3         109,272 ±        41,031  ops/min
 * SquareMultiply.execute   2000       EJML  thrpt    3           7,106 ±         2,328  ops/min
 * SquareMultiply.execute   2000        MTJ  thrpt    3          87,722 ±       134,475  ops/min
 * SquareMultiply.execute   2000     ojAlgo  thrpt    3          13,237 ±         5,812  ops/min
 * SquareMultiply.execute   5000       EJML  thrpt    3           0,449 ±         0,068  ops/min
 * SquareMultiply.execute   5000        MTJ  thrpt    3           6,140 ±         0,781  ops/min
 * SquareMultiply.execute   5000     ojAlgo  thrpt    3           0,845 ±         0,024  ops/min
 * </pre>
 *
 * <h1>MacBook Pro</h1>
 * <h2>2017-11-26</h2>
 *
 * <pre>
Result "org.ojalgo.benchmark.matrix.Square3Multiply.execute":
  317,437 ±(99.9%) 36,033 ops/min [Average]
  (min, avg, max) = (316,274, 317,437, 319,717), stdev = 1,975
  CI (99.9%): [281,404, 353,469] (assumes normal distribution)


# Run complete. Total time: 00:30:46

Benchmark                (dim)  (library)   Mode  Cnt           Score           Error    Units
Square3Multiply.execute      1        ACM  thrpt    3   418074584,968 ±  58104767,765  ops/min
Square3Multiply.execute      1       EJML  thrpt    3  4821345974,820 ± 707860810,282  ops/min
Square3Multiply.execute      1     ojAlgo  thrpt    3  1162913958,890 ± 350722224,977  ops/min
Square3Multiply.execute      1        MTJ  thrpt    3    80687278,814 ±   9624929,020  ops/min
Square3Multiply.execute      2        ACM  thrpt    3   274574102,404 ±  52586552,897  ops/min
Square3Multiply.execute      2       EJML  thrpt    3  2197312329,413 ± 205533365,299  ops/min
Square3Multiply.execute      2     ojAlgo  thrpt    3  1246064601,274 ± 467707697,922  ops/min
Square3Multiply.execute      2        MTJ  thrpt    3    79858965,992 ±   7174038,703  ops/min
Square3Multiply.execute      3        ACM  thrpt    3   180494762,127 ±  48445396,256  ops/min
Square3Multiply.execute      3       EJML  thrpt    3   869354569,708 ± 228229090,449  ops/min
Square3Multiply.execute      3     ojAlgo  thrpt    3   899497528,666 ± 536928082,949  ops/min
Square3Multiply.execute      3        MTJ  thrpt    3    77157786,664 ±  10167752,190  ops/min
Square3Multiply.execute      4        ACM  thrpt    3   133132154,176 ±  42421813,484  ops/min
Square3Multiply.execute      4       EJML  thrpt    3   523477332,225 ± 142641408,340  ops/min
Square3Multiply.execute      4     ojAlgo  thrpt    3   704425766,294 ±  27982908,637  ops/min
Square3Multiply.execute      4        MTJ  thrpt    3    79120279,666 ±  12744410,368  ops/min
Square3Multiply.execute      5        ACM  thrpt    3   106531345,329 ±   3373312,241  ops/min
Square3Multiply.execute      5       EJML  thrpt    3   346921635,192 ± 137500662,770  ops/min
Square3Multiply.execute      5     ojAlgo  thrpt    3   491462379,034 ±  76911480,611  ops/min
Square3Multiply.execute      5        MTJ  thrpt    3    72377081,639 ±  14356489,387  ops/min
Square3Multiply.execute      8        ACM  thrpt    3    51133737,751 ±   6555206,187  ops/min
Square3Multiply.execute      8       EJML  thrpt    3   113505594,979 ±  21928204,331  ops/min
Square3Multiply.execute      8     ojAlgo  thrpt    3   202955888,890 ±  28236584,868  ops/min
Square3Multiply.execute      8        MTJ  thrpt    3    67876261,149 ±  25147085,933  ops/min
Square3Multiply.execute     10        ACM  thrpt    3    31525476,495 ±   1674387,246  ops/min
Square3Multiply.execute     10       EJML  thrpt    3    66389490,089 ±  16207564,267  ops/min
Square3Multiply.execute     10     ojAlgo  thrpt    3   113331314,011 ±  24305969,775  ops/min
Square3Multiply.execute     10        MTJ  thrpt    3    62530465,238 ±   1600244,175  ops/min
Square3Multiply.execute     16        ACM  thrpt    3    10638520,075 ±   1192129,492  ops/min
Square3Multiply.execute     16       EJML  thrpt    3    15447295,934 ±   1764380,970  ops/min
Square3Multiply.execute     16     ojAlgo  thrpt    3    14107103,056 ±    910071,785  ops/min
Square3Multiply.execute     16        MTJ  thrpt    3    27514707,370 ±   8557154,413  ops/min
Square3Multiply.execute     20        ACM  thrpt    3     5886795,129 ±   1986434,283  ops/min
Square3Multiply.execute     20       EJML  thrpt    3     8788305,967 ±    215364,814  ops/min
Square3Multiply.execute     20     ojAlgo  thrpt    3     9625288,752 ±    424697,121  ops/min
Square3Multiply.execute     20        MTJ  thrpt    3    22402619,761 ±   2431939,916  ops/min
Square3Multiply.execute     32        ACM  thrpt    3     1649134,829 ±    413016,235  ops/min
Square3Multiply.execute     32       EJML  thrpt    3     2482131,477 ±    668520,524  ops/min
Square3Multiply.execute     32     ojAlgo  thrpt    3     2633378,563 ±    310634,688  ops/min
Square3Multiply.execute     32        MTJ  thrpt    3    12522448,474 ±   1169770,562  ops/min
Square3Multiply.execute     50        ACM  thrpt    3      425192,571 ±     87096,729  ops/min
Square3Multiply.execute     50       EJML  thrpt    3      484532,192 ±     46144,821  ops/min
Square3Multiply.execute     50     ojAlgo  thrpt    3      847300,741 ±    397981,987  ops/min
Square3Multiply.execute     50        MTJ  thrpt    3     5604657,224 ±    676508,342  ops/min
Square3Multiply.execute     64        ACM  thrpt    3      194622,424 ±     56335,366  ops/min
Square3Multiply.execute     64       EJML  thrpt    3      354909,525 ±     66961,555  ops/min
Square3Multiply.execute     64     ojAlgo  thrpt    3      518954,456 ±    491830,421  ops/min
Square3Multiply.execute     64        MTJ  thrpt    3     3005056,341 ±   1149861,309  ops/min
Square3Multiply.execute    100        ACM  thrpt    3       50216,767 ±     10900,892  ops/min
Square3Multiply.execute    100       EJML  thrpt    3      100463,429 ±     43446,262  ops/min
Square3Multiply.execute    100     ojAlgo  thrpt    3      183199,293 ±     44988,806  ops/min
Square3Multiply.execute    100        MTJ  thrpt    3      971414,859 ±    245167,254  ops/min
Square3Multiply.execute    128        ACM  thrpt    3       23141,638 ±      3259,874  ops/min
Square3Multiply.execute    128       EJML  thrpt    3       47158,349 ±     14274,979  ops/min
Square3Multiply.execute    128     ojAlgo  thrpt    3       94914,959 ±     70338,554  ops/min
Square3Multiply.execute    128        MTJ  thrpt    3      759591,770 ±    112546,538  ops/min
Square3Multiply.execute    200        ACM  thrpt    3        5476,404 ±      3108,478  ops/min
Square3Multiply.execute    200       EJML  thrpt    3       12800,527 ±      1709,811  ops/min
Square3Multiply.execute    200     ojAlgo  thrpt    3       27023,482 ±      8897,423  ops/min
Square3Multiply.execute    200        MTJ  thrpt    3      215994,274 ±     20072,654  ops/min
Square3Multiply.execute    256        ACM  thrpt    3        2502,465 ±       590,023  ops/min
Square3Multiply.execute    256       EJML  thrpt    3        6171,562 ±      1183,491  ops/min
Square3Multiply.execute    256     ojAlgo  thrpt    3       12796,924 ±       562,196  ops/min
Square3Multiply.execute    256        MTJ  thrpt    3      115229,974 ±      3073,246  ops/min
Square3Multiply.execute    500        ACM  thrpt    3         293,504 ±        77,931  ops/min
Square3Multiply.execute    500       EJML  thrpt    3         822,323 ±       165,030  ops/min
Square3Multiply.execute    500     ojAlgo  thrpt    3        1799,174 ±       334,040  ops/min
Square3Multiply.execute    500        MTJ  thrpt    3       18151,273 ±      2796,108  ops/min
Square3Multiply.execute    512        ACM  thrpt    3         262,921 ±        20,655  ops/min
Square3Multiply.execute    512       EJML  thrpt    3         769,553 ±        34,252  ops/min
Square3Multiply.execute    512     ojAlgo  thrpt    3        1643,904 ±       141,788  ops/min
Square3Multiply.execute    512        MTJ  thrpt    3       16910,937 ±      2287,888  ops/min
Square3Multiply.execute   1000        ACM  thrpt    3          10,548 ±         1,698  ops/min
Square3Multiply.execute   1000       EJML  thrpt    3          91,851 ±        11,253  ops/min
Square3Multiply.execute   1000     ojAlgo  thrpt    3         217,624 ±        31,949  ops/min
Square3Multiply.execute   1000        MTJ  thrpt    3        2406,211 ±       104,719  ops/min
Square3Multiply.execute   1024        ACM  thrpt    3           8,784 ±         1,862  ops/min
Square3Multiply.execute   1024       EJML  thrpt    3          86,516 ±         9,632  ops/min
Square3Multiply.execute   1024     ojAlgo  thrpt    3         201,347 ±        89,386  ops/min
Square3Multiply.execute   1024        MTJ  thrpt    3        2225,515 ±       634,327  ops/min
Square3Multiply.execute   2000        ACM  thrpt    3           0,786 ±         0,213  ops/min
Square3Multiply.execute   2000       EJML  thrpt    3          10,989 ±         0,612  ops/min
Square3Multiply.execute   2000     ojAlgo  thrpt    3          22,405 ±        84,483  ops/min
Square3Multiply.execute   2000        MTJ  thrpt    3         317,437 ±        36,033  ops/min
 * </pre>
 *
 * @author apete
 */
@State(Scope.Benchmark)
public class MultiplyToProduce extends MatrixBenchmarkOperation {

    public static void main(final String[] args) throws RunnerException {
        MatrixBenchmarkOperation.run(MultiplyToProduce.class);
    }

    @Param({ "10", "100", "1000" })
    public int dim;

    @Param({ "ACM", "EJML", "ojAlgo", "MTJ" })
    public String lib;

    private ProducingBinaryOperation<?, ?> myOperation;

    Object left;
    Object right;

    @Override
    @Benchmark
    public Object execute() {
        return myOperation.execute(left, right);
    }

    @Override
    @Setup
    public void setup() {

        library = MatrixBenchmarkLibrary.LIBRARIES.get(lib);

        myOperation = library.getOperationMultiplyToProduce();

        left = this.makeRandom(dim, dim, library);
        right = this.makeRandom(dim, dim, library);
    }

    @Override
    @TearDown(Level.Iteration)
    public void verify() throws BenchmarkRequirementsException {

        // this.verifyStateless(myOperation.getClass());

    }

}
