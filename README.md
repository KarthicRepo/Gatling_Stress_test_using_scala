# Gatling_Stress_test_using_scala
sample scala programs for Stress testing of APIs in Gatling tool.

### GATLING FOR STRESS TESTING OF APIs

- This project consist of sample programs in scala for Stress testing in Gatling tool.
- I've mainly focused on the API stress testing, and included few Web-socket connection testing as well.
- Also I've built a custom influxdb docker image, where the graphite is pre-enabled, this will be useful for docker platform.

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

### Integrating gatling with Grafana using Influxdb
Docker comes very handy while using Grafana with Influxdb, but the default Influxdb doesn't come with Graphite enabled.
And its not that straight to use docker-compose with Grafana and Influxdb on Windows. So I've built a custom docker image having Graphite pre-enabled.
- Use this image while using docker-compose.

#### docker-compose file for reference.
```
version: "3"
services:
  influxdb:
    image: katsdocker/influxdbwithgraphite
    container_name: influxdb_datasource
    restart: always
    ports:
      - 8086:8086
      - 2003:2003
    networks:
      - metrics_network
    volumes:
      - influxdb-data:/var/lib/influxdb
    command: influxd

  grafana:
    image: grafana/grafana
    container_name: grafana_metrics
    restart: always
    ports:
      - 3000:3000
    networks:
      - metrics_network
    volumes:
      - grafana-data:/var/lib/grafana

networks:
  metrics_network:

volumes:
  grafana-volume:
    external: true
  influxdb-volume:
    external: true
```
- Running above docker-compose file will quickly bring the Grafana and Influx with graphite enabled already.
- Now make config changes in gatling.conf file to enable Graphite writer as shown below.
```
graphite {
      light = false              # only send the all* stats
      host = "localhost"         # The host where the Carbon server is located
      port = 2003                # The port to which the Carbon server listens to (2003 is default for plaintext, 2004 is default for pickle)
      protocol = "tcp"           # The protocol used to send data to Carbon (currently supported : "tcp", "udp")
      rootPathPrefix = "gatling" # The common prefix of all metrics sent to Graphite
      bufferSize = 8192          # Internal data buffer size, in bytes
      writePeriod = 1            # Write period, in seconds
    }
```
-Now execute the Gatling test, and once completed, login into influxdb container,
```
show databases;
use gatlingdb;

do a select * query on the tags to see all the metrics which are written into influxdb.
```
- Now load the grafana: http://localhost:8086, login using(admin/admin).
- Add influxdb datasource to grafana.
- Create Dashboard and create a graph using the predefined set of queries.
