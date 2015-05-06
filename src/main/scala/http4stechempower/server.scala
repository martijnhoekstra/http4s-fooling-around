
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
    import DummyQueries._
    import org.http4s.twirl._

    val rand = new scala.util.Random()

    def fullfortunes = {
      val newfortune = Fortune(0, "fortune added at runtime")
      fortunes.map(fetched => {
        val all = (newfortune #:: fetched)
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
      single(randomid)
    }

    def randomWorlds(count: Int): Task[Stream[World]] = {
      import scalaz._
      import std.stream._
      val tasks = Stream.continually(randomWorld).take(count)
      Traverse[Stream].sequence(tasks)
    }

    def doupdates: Task[Stream[World]] = {
      ???
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
      case GET -> Root / "updates" => Ok(doupdates)
      case GET -> Root / "plaintext" => Ok("Hello, World")
    }

    BlazeBuilder.bindHttp(3456)
      .mountService(techempowerservice, "/")
      .run
      .awaitShutdown()
  }

}