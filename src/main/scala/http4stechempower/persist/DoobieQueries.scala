import scalaz.concurrent.Task
import doobie.imports._, scalaz._, Scalaz._
import shapeless._

package http4s.techempower.persist {
  import http4s.techempower.models._

  object DoobieQueries {

    def forTransactor(transactor: Transactor[Task]) = new DbQueries {
      //case class World(id: Int, randomNumber: Int)
      def single(id: Int): Task[World] = {
        sql"select id, randomNumber from World where id = $id"
          .query[World]
          .unique
          .transact(transactor)
      }

      def fortunes: Task[Stream[Fortune]] = {
        sql"select id, message from Fortune"
          .query[Fortune]
          .stream
          .transact(transactor)
      }
    }
  }
}