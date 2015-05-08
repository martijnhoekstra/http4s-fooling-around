import scalaz.concurrent.Task
package http4s.techempower.persist {
  import http4s.techempower.models._

  trait DbQueries {
    def single(index: Int): Task[World]
    def fortunes: Task[List[Fortune]]
    def updates(worlds: Stream[World]): Task[Stream[World]]
  }

  object DummyQueries extends DbQueries {
    type SingleResult = Task[World]
    type FortunesResult = Task[List[Fortune]]

    def single(index: Int): Task[World] = Task.delay(World(index, index))

    def fortunes: Task[List[Fortune]] = Task.delay(List(
      Fortune(2, "aadummy"),
      Fortune(1, "zdummy"),
      Fortune(3, """<script>alert("This should not be displayed in a browser alert box.");</script>""")
    ))

    def updates(worlds: Stream[World]): Task[Stream[World]] = Task.delay(worlds)
  }
}