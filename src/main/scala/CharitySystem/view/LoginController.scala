package CharitySystem.view

import javafx.fxml.FXML
import javafx.scene.control.{Label, PasswordField, TextField}
import CharitySystem.model.{Session, UserStore}

class LoginController {
  @FXML private var emailField: TextField = _
  @FXML private var passwordField: PasswordField = _
  @FXML private var statusLabel: Label = _

  @FXML def login(): Unit = {
    val email = Option(emailField.getText).map(_.trim).getOrElse("")
    val pw    = Option(passwordField.getText).getOrElse("")
    if (email.isEmpty || pw.isEmpty) { statusLabel.setText("Please fill in both fields."); return }

    UserStore.findByEmail(email) match {
      case Some(u) if u.password == pw =>
        Session.currentUser = Some(u)
        // Donor goes to Donate page (main page now)
        if (u.role == "Receiver") SceneNavigator.show("ReceiverDashboard.fxml")
        else                      SceneNavigator.show("Donate.fxml")
      case _ =>
        statusLabel.setText("Invalid email or password.")
    }
  }

  @FXML def goRegister(): Unit = SceneNavigator.show("Register.fxml")
}
