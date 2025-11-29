package CharitySystem.model

import scala.collection.mutable

/** Minimal in-memory user store for demo. Replace with DAO later if needed. */
object UserStore {
  private val users = mutable.Map[String, User]()

  users += "donor@example.com"    -> User("donor@example.com",    "Donor Demo",    "donor123",    "Donor")
  users += "receiver@example.com" -> User("receiver@example.com", "Receiver Demo", "receiver123", "Receiver")

  def create(user: User): Unit = users.update(user.email.toLowerCase, user)
  def findByEmail(email: String): Option[User] = users.get(email.toLowerCase)
}
