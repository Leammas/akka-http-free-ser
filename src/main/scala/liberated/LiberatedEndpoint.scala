package liberated

import akka.http.scaladsl.server.Directives._
import io.circe.Decoder
import io.circe.generic.decoding.DerivedDecoder
import shapeless.Lazy
import liberated.LiberatedEndpoint.LiberatedEndpointOp.{ Get, Put }

import scala.concurrent.Future

import io.aecor.liberator.macros.algebra

@algebra
trait LiberatedEndpoint[F[_]] {
  def get(k: String): F[Option[Int]]

  def put(k: String, v: Int): F[Unit]
}

object LiberatedEndpoint {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import Marshalling._

  implicit def requestDecoder[A <: LiberatedEndpointOp[_]](
    implicit decoder: Lazy[DerivedDecoder[A]]
  ): Decoder[A] =
    decoder.value

  val routeMap = Map("get" -> RequestRoute[Get](), "put" -> RequestRoute[Put]())

  def route(endpointOps: LiberatedEndpoint[Future]) =
    pathPrefix("liberated") {
      ServerRoute(LiberatedEndpoint.toFunctionK(endpointOps), routeMap)
    }
}
