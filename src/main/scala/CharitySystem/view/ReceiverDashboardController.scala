package CharitySystem.view

import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import javafx.beans.property.{ReadOnlyObjectWrapper, ReadOnlyStringWrapper}
import javafx.collections.{FXCollections, ListChangeListener}
import CharitySystem.model.{Donation, DonationStore, Session}

import scala.jdk.CollectionConverters._

class ReceiverDashboardController {
  @FXML private var donationsTable: TableView[Donation] = _
  @FXML private var colItem: TableColumn[Donation, String] = _
  @FXML private var colQty: TableColumn[Donation, Integer] = _
  @FXML private var colAddress: TableColumn[Donation, String] = _
  @FXML private var colPhone: TableColumn[Donation, String] = _
  @FXML private var colStatus: TableColumn[Donation, String] = _
  @FXML private var statusLabel: Label = _

  @FXML def initialize(): Unit = {
    colItem.setCellValueFactory(cd => new ReadOnlyStringWrapper(cd.getValue.item))
    colQty.setCellValueFactory(cd => new ReadOnlyObjectWrapper[Integer](cd.getValue.quantity: java.lang.Integer))
    colAddress.setCellValueFactory(cd => new ReadOnlyStringWrapper(cd.getValue.address))
    colPhone.setCellValueFactory(cd => new ReadOnlyStringWrapper(cd.getValue.phone))
    colStatus.setCellValueFactory(cd => new ReadOnlyStringWrapper(cd.getValue.status))

    refreshTable()

    DonationStore.all.addListener(
      new ListChangeListener[Donation] {
        override def onChanged(c: ListChangeListener.Change[_ <: Donation]): Unit = refreshTable()
      }
    )
  }

  private def refreshTable(): Unit = {
    val pending = DonationStore.all.asScala.filter(_.status == "PENDING")
    donationsTable.setItems(FXCollections.observableArrayList(pending.asJava))
  }

  // <— this satisfies onAction="#goDashboard" in the FXML
  @FXML def goDashboard(): Unit = refreshTable()

  @FXML def claimSelected(): Unit = {
    val selected = donationsTable.getSelectionModel.getSelectedItem
    if (selected == null) { statusLabel.setText("Select a donation to claim."); return }

    selected.status = "ACCEPTED"
    DonationStore.update(selected)
    refreshTable()
    statusLabel.setText("Donation accepted. Donor will see it as ACCEPTED.")
  }

  @FXML def logout(): Unit = {
    Session.clear()
    SceneNavigator.show("Login.fxml")
  }
}
