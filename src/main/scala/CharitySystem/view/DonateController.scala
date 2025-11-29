package CharitySystem.view

import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView, TextField}
import javafx.beans.property.{ReadOnlyObjectWrapper, ReadOnlyStringWrapper}
import javafx.collections.{FXCollections, ListChangeListener}
import CharitySystem.model.{Donation, DonationStore, Session}

import scala.jdk.CollectionConverters._
import scala.util.Try

class DonateController {
  // form
  @FXML private var itemField: TextField = _
  @FXML private var qtyField: TextField = _
  @FXML private var addressField: TextField = _
  @FXML private var phoneField: TextField = _
  @FXML private var successLabel: Label = _

  // table + actions
  @FXML private var yourDonationsTable: TableView[Donation] = _
  @FXML private var colItem: TableColumn[Donation, String] = _
  @FXML private var colQty: TableColumn[Donation, Integer] = _
  @FXML private var colAddress: TableColumn[Donation, String] = _
  @FXML private var colPhone: TableColumn[Donation, String] = _
  @FXML private var colStatus: TableColumn[Donation, String] = _
  @FXML private var actionStatus: Label = _

  private def meEmail: String = Session.currentUser.map(_.email).getOrElse("")

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
    val mine = DonationStore.all.asScala.filter(_.donorEmail == meEmail)
    yourDonationsTable.setItems(FXCollections.observableArrayList(mine.asJava))
  }

  @FXML def submitDonation(): Unit = {
    val donorEmail = meEmail
    val item  = Option(itemField.getText).map(_.trim).getOrElse("")
    val qty   = Option(qtyField.getText).flatMap(s => Try(s.trim.toInt).toOption).getOrElse(0)
    val addr  = Option(addressField.getText).map(_.trim).getOrElse("")
    val phone = Option(phoneField.getText).map(_.trim).getOrElse("")

    if (donorEmail.isEmpty) { successLabel.setText("Please login again."); return }
    if (item.isEmpty || qty <= 0) { successLabel.setText("Enter a valid item and positive quantity."); return }

    DonationStore.insert(donorEmail, item, qty, addr, phone)  // status = PENDING
    successLabel.setText("Donation posted!")
    itemField.clear(); qtyField.clear(); addressField.clear(); phoneField.clear()
  }

  @FXML def markCollectedSelected(): Unit = {
    val sel = yourDonationsTable.getSelectionModel.getSelectedItem
    if (sel == null) { actionStatus.setText("Select a row first."); return }
    sel.status = "COLLECTED"
    DonationStore.update(sel)
    actionStatus.setText("Marked as COLLECTED.")
  }

  @FXML def cancelSelected(): Unit = {
    val sel = yourDonationsTable.getSelectionModel.getSelectedItem
    if (sel == null) { actionStatus.setText("Select a row first."); return }
    if (sel.status != "PENDING") {
      actionStatus.setText("Only PENDING donations can be cancelled.")
      return
    }
    DonationStore.remove(sel.id)
    actionStatus.setText("Donation cancelled.")
  }

  @FXML def logout(): Unit = {
    Session.clear()
    SceneNavigator.show("Login.fxml")
  }
}
