package CharitySystem.model

import javafx.collections.{FXCollections, ObservableList}
import scala.jdk.CollectionConverters._

/** JavaFX-friendly in-memory store so TableView updates instantly. */
object DonationStore {
  val all: ObservableList[Donation] = FXCollections.observableArrayList[Donation]()
  private var _nextId: Long = 1L

  def insert(donorEmail: String, item: String, qty: Int, addr: String, phone: String): Donation = {
    val d = Donation(_nextId, donorEmail.trim, item.trim, qty, addr.trim, phone.trim, "PENDING")
    _nextId += 1
    all.add(d) // triggers TableView listeners
    d
  }

  def update(d: Donation): Unit = {
    val idx = all.asScala.indexWhere(_.id == d.id)
    if (idx >= 0) all.set(idx, d) // triggers listeners
  }

  def remove(id: Long): Unit = {
    val it = all.asScala.indexWhere(_.id == id)
    if (it >= 0) all.remove(it)
  }

  // helpers
  def countPending: Int = all.asScala.count(_.status == "PENDING")
  def countAccepted: Int = all.asScala.count(_.status == "ACCEPTED")
  def countNotCollected: Int = all.asScala.count(x => x.status == "CLAIMED" || x.status == "NOT_COLLECTED")
  def qtyByDonor(email: String): Int = all.asScala.view.filter(_.donorEmail == email).map(_.quantity).sum
}
