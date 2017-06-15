package liberated
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.Decoder
import io.circe.generic.decoding.DerivedDecoder
import liberated.NotFreeEndpoint.NotFreeDTO
import shapeless.Lazy

object NotFree {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import Marshalling._

  implicit def requestDecoder[A <: NotFreeDTO](
    implicit decoder: Lazy[DerivedDecoder[A]]
  ): Decoder[A] =
    decoder.value

  def route(endpointOps: NotFreeEndpoint): Route =
    pathPrefix("notfree") {
      post {
        pathPrefix("put") {
          entity(as[NotFreeDTO.Put]) { r =>
            complete {
              endpointOps.put(r.k, r.v)
            }
          }
        } ~ pathPrefix("get") {
          entity(as[NotFreeDTO.Get]) { r =>
            complete {
              endpointOps.get(r.k)
            }
          }
        }
      }
    }

}
