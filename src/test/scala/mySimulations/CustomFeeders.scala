package mySimulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class CustomFeeders extends Simulation{

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost",8888))

  val numItr= (1 to 10).iterator
  val customFeeder = Iterator.continually(Map("gameId"->numItr.next()))

  def fetchGamesUsingCsvFile() = {
    repeat(10) {
      feed(customFeeder)
        .exec(http("repeating request")
          .get("videogames/${gameId}")
          .check(status.is(200)))
        .pause(500.milliseconds)
    }
  }

  val scn = scenario("Looping using csv file")
    .exec(fetchGamesUsingCsvFile())

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
