import scalaz.concurrent.Task
import doobie.imports._
import scalaz._
import Scalaz._

package http4s.techempower.persist {
  import http4s.techempower.models._

  object DoobieQueries {

    def forTransactor(transactor: Transactor[Task]) = new DbQueries {
      def single(id: Int): Task[World] = {
        sql"select id, randomNumber from World where id = $id"
          .query[World]
          .unique
          .transact(transactor)
      }

      def fortunes: Task[List[Fortune]] = {
        sql"select id, message from Fortune"
          .query[Fortune]
          .list
          .transact(transactor)
      }

      def updates(worlds: Stream[World]): Task[Stream[World]] = {
        val sql = "update World set randomNumber = ? where id = ?"
        val done = Update[World](sql).updateMany(worlds)
          .transact(transactor)
        done.map(_ => worlds)
      }

    }
  }
}