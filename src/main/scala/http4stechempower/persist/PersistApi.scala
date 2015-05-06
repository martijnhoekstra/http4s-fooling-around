import scalaz.concurrent.Task
package http4s.techempower.persist {
  import http4s.techempower.models._

  trait DbQueries {

    def single(index: Int): Task[World]
    //def multi(numqueries: Int): Task[List[World]]
    def fortunes: Task[Stream[Fortune]]
  }

  object DummyQueries extends DbQueries {
    def single(index: Int): Task[World] = Task.delay(World(index, index))

    def fortunes: Task[Stream[Fortune]] = Task.delay(Stream(
      Fortune(2, "aadummy"),
      Fortune(1, "zdummy"),
      Fortune(3, """<script>alert("This should not be displayed in a browser alert box.");</script>""")
    ))

  }
}