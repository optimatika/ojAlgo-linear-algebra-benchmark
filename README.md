# oj! Linear Algebra (JMH) Benchmark - ojLAB

Comparing various linear algebra operations between ojAlgo and what we think are the main alternatives to ojAlgo.

Alternative | Reason for inclusion
----------------|----------------
EJML | A pure Java, no dependecies, efficient linear algebra package (just like ojAlgo). The author of EJML is also the author of the Java Matrix Benchmark. EJML is top quality pure Java.
MTJ | Continuing the fortran tradition of BLAS and LAPACK and bringing it over to Java. MTJ depends on netlib-java that will automatically switch between Java and native code libraries. When used on a Mac it will default to use Apple's built in vecLib library which is very fast. It's included as a reference to what can be achieved with native code.
Apache Commons Math | Essentially all Java linear algebra libraries available are developed by "small" teams. Apache Commons Math is the only library backed by an organisation that could be described as "large" (I conclude that simply because it is an Apache project). Other libraries must offer some benfit over using Commons Math to justify their existence.



## Results

Various benchmark results are sporadically published at the ojAlgo web site: https://www.ojalgo.org/category/benchmarks/

Running these benchmarks is easy. They're based on JMH. Just clone the repository. Do `mvn clean install` and then
```
java -jar ojlab.jar org.ojalgo.benchmark.lab.FillByMultiplying
```

Replace FillByMultiplying with which ever benchmark you want to run.
