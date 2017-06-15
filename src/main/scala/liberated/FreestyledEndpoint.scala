package liberated

import freestyle._

@free
trait FreestyledEndpoint {
  def get(k: String): FS[Option[Int]]

  def put(k: String, v: Int): FS[Unit]
}

