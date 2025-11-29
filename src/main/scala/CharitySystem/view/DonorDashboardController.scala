package CharitySystem.view

import javafx.fxml.FXML
import javafx.scene.control.Label
import scalafx.Includes._
import CharitySystem.model.{DonationStore, Session}

class DonorDashboardController {
  @FXML private var myDonationsLabel: Label = _
  @FXML private var notProcessedLabel: Label = _
  @FXML private var acceptedToAssignLabel: Label = _
  @FXML private var notCollectedYetLabel: Label = _

  @FXML def initialize(): Unit = {
    refreshTiles()
    DonationStore.all.onChange { (_, _) => refreshTiles() }
  }

  private def refreshTiles(): Unit = {
    val me = Session.currentUser.map(_.email).getOrElse("")
    myDonationsLabel.setText(s"${DonationStore.qtyByDonor(me)} donations by you")
    notProcessedLabel.setText(s"${DonationStore.countPending} donation requests not processed")
    acceptedToAssignLabel.setText(s"${DonationStore.countAccepted} donations accepted and to be assigned")
    notCollectedYetLabel.setText(s"${DonationStore.countNotCollected} donations not collected yet")
  }

  @FXML def goDonate(): Unit = SceneNavigator.goTo("Donate.fxml")
  @FXML def logout(): Unit = { Session.clear(); SceneNavigator.goTo("Login.fxml") }
}
