import http4s.techempower.models._

import org.http4s.EntityEncoder
import org.http4s.argonaut._

import _root_.argonaut._
import Argonaut._

package http4s.techempower.json {

  object ArgonautEncoder extends JsonApi {

    implicit def messageEncoding: EncodeJson[ReturnMessage] = jencode1L((m: ReturnMessage) => (m.message))("message")
    implicit def worldEncoding: EncodeJson[World] = jencode2L((w: World) => (w.id, w.randomNumber))("id", "message")

    implicit def messageEncoder: EntityEncoder[ReturnMessage] = jsonEncoderOf[ReturnMessage]
    implicit def worldEncoder: EntityEncoder[World] = jsonEncoderOf[World]
    implicit def worldlistEncoder: ListEncoder[World] = jsonEncoderOf[List[World]]
    implicit def worldstreamEncode = jsonEncoderOf[Stream[World]]

  }
}