package CharitySystem.model


final case class Donor(user: User)

object Donor {
  // Migration helper to match old constructor call sites:
  def apply(id: Int, email: String, firstName: String, lastName: String, password: String): Donor =
    Donor(User(email = email, name = s"$firstName $lastName", password = password, role = "Donor"))

  // Simple constructor
  def apply(email: String, name: String, password: String): Donor =
    Donor(User(email, name, password, role = "Donor"))
}
