package liberated

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Liberated {

  val endpoint = EndpointImpl()

  implicit val system = ActorSystem("http")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val server = Http()
      .bindAndHandle(LiberatedEndpoint.route(endpoint), "0.0.0.0", 9000)
  }

}
