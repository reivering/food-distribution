package CharitySystem.model

case class Donation(
                     id: Long,
                     donorEmail: String,
                     item: String,
                     quantity: Int,
                     address: String,
                     phone: String,
                     var status: String // "PENDING", "ACCEPTED", "CLAIMED", "COLLECTED", "NOT_COLLECTED"
                   )
