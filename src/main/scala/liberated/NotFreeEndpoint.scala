package liberated

import scala.concurrent.Future

trait NotFreeEndpoint {
  def get(k: String): Future[Option[Int]]

  def put(k: String, v: Int): Future[Unit]
}

object NotFreeEndpoint {

  sealed trait NotFreeDTO extends Product with Serializable
  object NotFreeDTO {
    final case class Get(k: String) extends NotFreeDTO
    final case class Put(k: String, v: Int) extends NotFreeDTO
  }

}
