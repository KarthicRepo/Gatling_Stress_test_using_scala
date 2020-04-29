//Copyright 2020 @ karthic-sdet
package mySimulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class RequestsWithPause extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost",8888))

  val scn = scenario("Scenario:Fetch with pauses")
    .exec(http("Get all games")
      .get("videogames"))
    .pause(5)

    .exec(http("Get first game")
      .get("videogames/1"))
    .pause(1, 5)

    .exec(http("Again retrieve all games")
      .get("videogames"))
    .pause(5000.milliseconds)

  setUp(
    scn.inject(atOnceUsers(2))
  ).protocols(httpConf)

}

