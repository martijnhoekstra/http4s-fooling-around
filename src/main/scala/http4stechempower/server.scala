
import org.http4s.Http4s._
import org.http4s.server.blaze.BlazeBuilder

import org.http4s._
import org.http4s.server._
import org.http4s.dsl._

import scalaz.concurrent.Task

package http4s.techempower {
  import models._
  import persist._
  import http4s.techempower.json._

  object TechEmpowerServer extends App {
    import ArgonautEncoder._
    import org.http4s.twirl._

    val backend: DbQueries = DummyQueries
    //backend = DoobieQueries.forTransactor(sometransactor)

    val rand = new scala.util.Random()

    def fullfortunes = {
      val newfortune = Fortune(0, "Additional fortune added at request time.")
      backend.fortunes.map(fetched => {
        val all = (newfortune :: fetched)
        val sorted = all.sortBy(_.message)
        //Note on sorting: The requirement reads: "The list of Fortune objects must be sorted by the order of the message field."
        //no collation is supplied, so just go for the easiest possibility. This uses String.compareTo() which 
        //is an alphabetical collation based on the UTF-16 codepoint per character. This is wrong in a number of ways
        //but apparently acceptable
        html.fortunes(sorted)
      })
    }

    def randomWorld: Task[World] = {
      val randomid = rand.nextInt(10000) + 1
      backend.single(randomid)
    }

    def randomWorlds(count: Int): Task[Stream[World]] = {
      import scalaz._
      import std.stream._
      val tasks = Stream.continually(randomWorld).take(count)
      Traverse[Stream].sequence(tasks)
    }

    def doupdates(worlds: Task[Stream[World]]): Task[Stream[World]] = {
      worlds.flatMap(ws => backend.updates(ws))
    }

    def toIntOption(str: String) = {
      import scala.util.control.Exception._
      catching(classOf[NumberFormatException]) opt str.toInt
    }

    val techempowerservice = HttpService {
      case GET -> Root / "json" => Ok(ReturnMessage("Hello, World!"))
      case GET -> Root / "db" => Ok(randomWorld)
      case GET -> Root / "queries" :? params => {
        val count = params.get("queries").flatMap(vals => vals.headOption.flatMap(head => toIntOption(head))) match {
          case Some(i) if i > 500 => 500
          case Some(i) if i > 1 => i
          case _ => 1
        }
        Ok(randomWorlds(count))
      }
      case GET -> Root / "fortunes" => Ok(fullfortunes)
      case GET -> Root / "updates" :? params => {
        val count = params.get("queries").flatMap(vals => vals.headOption.flatMap(head => toIntOption(head))) match {
          case Some(i) if i > 500 => 500
          case Some(i) if i > 1 => i
          case _ => 1
        }
        val tworlds = randomWorlds(count)
        val updated_worlds = tworlds.map(_.map(_.copy(randomNumber = rand.nextInt(10000) + 1)))
        Ok(doupdates(updated_worlds))
      }
      case GET -> Root / "plaintext" => Ok("Hello, World")
    }

    BlazeBuilder.bindHttp(3456)
      .mountService(techempowerservice, "/")
      .run
      .awaitShutdown()
  }

}