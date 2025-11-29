package CharitySystem.model

object Session {
  var currentUser: Option[User] = None
  def clear(): Unit = currentUser = None
}
