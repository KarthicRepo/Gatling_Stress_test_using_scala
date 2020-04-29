# Gatling_Stress_test_using_scala
sample scala programs for Stress testing of APIs in Gatling tool.

## GATLING FOR STRESS TESTING OF APIs

This project consist of sample programs in scala for Stress testing in Gatling tool.
I've mainly focused on the API stress testing, and included few Web-socket connection testing as well.

### Below are the list of functionality I've tried to cover.
- Simple REST api call and status code validation.
- Stress test along with Correlation and Assertions
- CSV data feeder for test APIs
- Custom data feeder for test APIs.
- Debugging using session.
- Debugging using logback.xml file.
- Simple user injection.
- Complex user injection, rampup times and stress duration and final Assertion.

### Different examples of injection profiles for simulating differnt use case
```
1)
setUp(scn.inject(
    constantConcurrentUsers(10) during(10 seconds),
    rampConcurrentUsers(10) to(20) during(10 seconds)
  )).protocols(httpConf).maxDuration(60 seconds)

2)
setUp(scn.inject(constantUsersPerSec(0.5) during(20 seconds)))
.protocols(httpConf).maxDuration(60)


3)
setUp(scn.inject(rampUsersPerSec(1) to(10) during(20 seconds)))
.protocols(httpConf).maxDuration(60)

4) // heavyside users
setUp(scn.inject(heavisideUsers(50) during(20 seconds))).protocols(httpConf).maxDuration(60 seconds)

5) //heavyside users
setUp(scn.inject(atOnceUsers(10),nothingFor(10 seconds),heavisideUsers(100) during(60 seconds),rampUsers(20) during(60 seconds)))
.protocols(httpConf).maxDuration(100 seconds)
       
6)
setUp(scn.inject(
    atOnceUsers(10)
    ,nothingFor(15 seconds),rampUsers(10)during(10 seconds)
    ,nothingFor(30 seconds),rampUsers(5)during(10 seconds)
    ,nothingFor(15 seconds),rampUsers(10)during(10 seconds)
    ,nothingFor(30 seconds),rampUsers(5)during(10 seconds)
  )).protocols(httpConf).maxDuration(600 seconds)

7) // throttling ( controling the throughput)
setUp(scn.inject(constantUsersPerSec(2) during (3 minutes)))
    .protocols(httpConf).throttle(
    reachRps(50) in (90 seconds),
    holdFor(3 minutes),
    jumpToRps(75),
    holdFor(3 minutes)
  ).maxDuration(7 minutes)

```

### Triggering the gatling tests via commandline
- use the maven gatling plugin in the pom file.
```
      <plugin>
        <groupId>io.gatling</groupId>
        <artifactId>gatling-maven-plugin</artifactId>
        <version>3.0.5</version>
      </plugin>
      
      
   mvn gatling:test -Dgatling.simulationClass=<package>.<simulation class>
   ex: mvn gatling:test -Dgatling.simulationClass=mySimulations.FirstGatlingTest
```
