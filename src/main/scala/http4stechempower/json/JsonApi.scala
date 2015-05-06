import org.http4s.EntityEncoder
import http4s.techempower.models._

package http4s.techempower.json {

  trait JsonApi {

    type MessageEncoder = EntityEncoder[ReturnMessage]
    type FortuneEncoder = EntityEncoder[Fortune]
    type WorldEncoder = EntityEncoder[World]
    type ListEncoder[A] = EntityEncoder[List[A]]

    implicit def messageEncoder: MessageEncoder
    implicit def worldEncoder: WorldEncoder

    implicit def worldlistEncoder: ListEncoder[World]

  }
}