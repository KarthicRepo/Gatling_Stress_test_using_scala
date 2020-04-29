//Copyright 2020 @ karthic-sdet

package mySimulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FirstGatlingTest extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  val scn = scenario("My First Test")
    .exec(http("Retrieve all Games")
      .get("videogames"))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}