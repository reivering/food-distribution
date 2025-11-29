package CharitySystem.view

import javafx.fxml.FXML
import javafx.scene.control.{ComboBox, Label, PasswordField, TextField}
import CharitySystem.model.{User, UserStore}
import CharitySystem.view.SceneNavigator

class RegisterController {
  @FXML private var nameField: TextField = _
  @FXML private var emailField: TextField = _
  @FXML private var passwordField: PasswordField = _
  @FXML private var roleCombo: ComboBox[String] = _
  @FXML private var statusLabel: Label = _

  @FXML def submit(): Unit = {
    val name  = Option(nameField.getText).map(_.trim).getOrElse("")
    val email = Option(emailField.getText).map(_.trim).getOrElse("")
    val pw    = Option(passwordField.getText).getOrElse("")
    val role  = Option(roleCombo.getValue).getOrElse("Donor") match {
      case "Receiver" => "Receiver"
      case _          => "Donor"
    }

    if (name.isEmpty || email.isEmpty || pw.isEmpty) {
      statusLabel.setText("Please fill in all fields.")
      return
    }

    // Simple duplicate check
    if (UserStore.findByEmail(email).isDefined) {
      statusLabel.setText("Email already registered.")
      return
    }

    UserStore.create(User(email, name, pw, role))
    statusLabel.setText("Account created. Redirecting to login...")
    SceneNavigator.show("Login.fxml")
  }

  @FXML def goLogin(): Unit = SceneNavigator.show("Login.fxml")
}
