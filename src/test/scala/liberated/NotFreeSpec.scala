package liberated

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.Specs2RouteTest
import io.circe.Json
import org.specs2.mutable.Specification
import io.circe.syntax._
import liberated.Marshalling._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

class NotFreeSpec extends Specification with Specs2RouteTest {

  "Endpoint should" >> {
    "respond" >> {
      val endpoint = NotFree.route(EndpointImpl())

      Post("/notfree/put", Json.obj("k" -> "key".asJson, "v" -> 1.asJson)) ~> endpoint ~> check {
        status shouldEqual StatusCodes.OK
      }

      Post("/notfree/get", Json.obj("k" -> "key".asJson)) ~> endpoint ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[Json] shouldEqual 1.asJson
      }
    }
  }

}
