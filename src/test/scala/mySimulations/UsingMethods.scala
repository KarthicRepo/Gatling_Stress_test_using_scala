package mySimulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class UsingMethods extends Simulation{

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8888))

  def getAllVideos()={
    exec(http("Fetch 2nd game and validate its name")
      .get("videogames/2")
      .check(jsonPath("$.name").is("Gran Turismo 3")))
  }
    def getSpecificVideo()={
      exec(http("Fetch 2nd game and validate its name")
        .get("videogames")
        .check(jsonPath("$[1].id").saveAs("gameId")))
    }

def getSpecificVideoById()={
  exec(http("get game by id fetched from previous response")
    .get("videogames/3"))
}

  val scn= scenario("Using reusable methods.")
    .exec(getAllVideos())
    .pause(5)
    .exec(getSpecificVideo())
    .pause(3)
    .exec(getSpecificVideoById())


  setUp(scn.inject(atOnceUsers(3))).protocols(httpConf)
}
