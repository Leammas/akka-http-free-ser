package liberated

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.concurrent.Future

class EndpointImpl
    extends LiberatedEndpoint[Future]
    with FreestyledEndpoint.Handler[Future]
    with NotFreeEndpoint {
  private var map: mutable.Map[String, Int] = TrieMap.empty

  def get(k: String): Future[Option[Int]] = Future.successful(map.get(k))

  def put(k: String, v: Int): Future[Unit] = Future.successful(map.put(k, v))
}

object EndpointImpl {
  def apply(): EndpointImpl = new EndpointImpl()
}
