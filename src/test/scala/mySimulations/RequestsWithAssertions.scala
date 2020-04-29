//Copyright 2020 @ karthic-sdet
package mySimulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class RequestsWithAssertions extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")

  val scn= scenario("With Assertions for status codes")
    .exec(http("Get all games")
    .get("allgames")
    .check(status.is(200)))
    .pause(5)

    .exec(http("Fetch only the second game record")
    .get("allgames/2")
    .check(status.in(200 to 299)))
    .pause(1,5)

    .exec(http("Fetch all games again")
    .get("allgames")
    .check(status.not(400)))
    .pause(4000.milliseconds)
}
