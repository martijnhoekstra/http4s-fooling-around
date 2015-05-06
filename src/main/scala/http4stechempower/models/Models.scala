package http4s.techempower.models {

  case class ReturnMessage(message: String)
  case class World(id: Int, randomNumber: Int)
  case class Fortune(id: Int, message: String)
}