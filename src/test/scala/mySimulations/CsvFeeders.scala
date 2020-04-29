package mySimulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class CsvFeeders extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost",8888))

  val csvFeeder = csv("data/movies.csv").circular

  def fetchGamesUsingCsvFile() = {
    repeat(20) {
      feed(csvFeeder)
        .exec(http("repeating request")
          .get("videogames/${gameId}")
          .check(status.is(200))
          .check(jsonPath("$.name").is("${gameName}")))
        .pause(500.milliseconds)
    }
  }

  val scn = scenario("Looping using csv file")
    .exec(fetchGamesUsingCsvFile())

  setUp(scn.inject(atOnceUsers(2))).protocols(httpConf)
}
