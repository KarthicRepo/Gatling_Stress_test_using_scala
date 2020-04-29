package mySimulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SessionDebug extends Simulation {
  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8888))

  val scn = scenario("With Json path validation")
    .exec(http("Fetch 2nd game and validate its name")
      .get("videogames/2")
      .check(jsonPath("$.name").is("Gran Turismo 3")))
    .exec {
      session =>
        print(session);
        session
    }

    .exec(http("Fetch 2nd game and validate its name")
      .get("videogames")
      .check(jsonPath("$[1].id").saveAs("gameId")))
    .exec {
      session =>
        print(session);
        session
    }
    .exec(http("get game by id fetched from previous response")
      .get("videogames/${gameId}")
      .check(jsonPath("$.name").is("Gran Turismo 3"))
      .check(bodyString.saveAs("responseBody")))
    .exec {
      session =>
        println("ResponseBody:" + session("responseBody").as[String]);
        session
    }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
