package liberated

import akka.http.scaladsl.marshalling.{ GenericMarshallers, ToResponseMarshaller }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import cats.~>
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Encoder

import scala.concurrent.Future

object RequestRoute {

  trait MkRoute[Request] {
    def apply[Response, F[_]]()(
      implicit ev: Request <:< F[Response],
      fromRequest: FromRequestUnmarshaller[Request],
      toResponse: ToResponseMarshaller[Response]
    ): (F ~> Future) => Route =
      interpreter =>
        entity(as[Request]) { request =>
          extractExecutionContext { implicit ec =>
            complete(interpreter(request))
          }
      }
  }

  def apply[Request] = new MkRoute[Request] {}

}

object ServerRoute {

  def apply[F[_]](interpreter: F ~> Future, routes: Map[String, (F ~> Future) => Route]): Route =
    pathPrefix(Segment) { requestRoute =>
      pathEndOrSingleSlash {
        routes.get(requestRoute) match {
          case Some(handler) =>
            handler(interpreter)
          case None =>
            complete {
              StatusCodes.BadRequest -> "Unknown request"
            }
        }
      }
    }

}

object Marshalling {
  implicit def futureMarshaller[A](implicit enc: Encoder[A]): ToResponseMarshaller[Future[A]] =
    GenericMarshallers.futureMarshaller(
      GenericMarshallers
        .liftMarshallerConversion(FailFastCirceSupport.marshaller(enc))
    )
}
