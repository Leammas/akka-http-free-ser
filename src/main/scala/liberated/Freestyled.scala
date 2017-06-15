package liberated

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import io.circe.Decoder
import io.circe.generic.decoding.DerivedDecoder
import shapeless.Lazy

import scala.concurrent.Future
import FreestyledEndpoint._
import akka.http.scaladsl.server.Directives.pathPrefix

object Freestyled {

  // cant move definition into FreestyledEndpoint companion
  object FreestyledEndpointImpl {

    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
    import Marshalling._

    implicit def requestDecoder[A <: FreestyledEndpoint.Op[_]](
      implicit decoder: Lazy[DerivedDecoder[A]]
    ): Decoder[A] =
      decoder.value

    val routeMap = Map("get" -> RequestRoute[GetOP](), "put" -> RequestRoute[PutOP]())

    def route(endpointOps: FreestyledEndpoint.Handler[Future]) =
      pathPrefix("freestyled") {
        ServerRoute(endpointOps, routeMap)
      }
  }

  /**
    * Wiring
    */
  val endpoint = EndpointImpl()

  implicit val system = ActorSystem("http")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val server = Http()
      .bindAndHandle(FreestyledEndpointImpl.route(endpoint), "0.0.0.0", 9000)
  }
}
