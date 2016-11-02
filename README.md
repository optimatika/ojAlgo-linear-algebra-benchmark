# oj! Linear Algebra (JMH) Benchmark - ojLAB

Comparing various linear algebra operations between ojAlgo and what we think are the main alternatives to ojAlgo.

Alternative | Reason for inclusion
----------------|----------------
EJML | A pure Java, no dependecies, efficient linear algebra package (just like ojAlgo). The author of EJML is also the author of the Java Matrix Benchmark. EJML is top quality pure Java. ojAlgo should
MTJ | Continuing the fortran tradition of BLAS and LAPACK and bringing it over to Java. MTJ depends on netlib-java that will automatically switch between Java and native code libraries. When used on a Mac it will default to use Apple's built in vecLib library which is very fast. It's included as a reference to what can be achieved with native code.
Apache Commons Math | Essentially all Java linear algebra libraries available are developed by "small" teams. Apache Commons Math is just about the only librrarybacked by something 



Currently no plans to publish any results
