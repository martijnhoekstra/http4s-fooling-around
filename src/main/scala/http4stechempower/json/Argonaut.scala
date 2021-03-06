import http4s.techempower.models._

import org.http4s.EntityEncoder
import org.http4s.argonaut._

import _root_.argonaut._
import Argonaut._

package http4s.techempower.json {

  object ArgonautEncoder {
    implicit def messageEncoding: EncodeJson[ReturnMessage] = jencode1L((m: ReturnMessage) => (m.message))("message")
    implicit def worldEncoding: EncodeJson[World] = jencode2L((w: World) => (w.id, w.randomNumber))("id", "message")

    implicit def jsonEncoder[A](implicit ev: EncodeJson[A]): EntityEncoder[A] = jsonEncoderOf[A]
  }
}