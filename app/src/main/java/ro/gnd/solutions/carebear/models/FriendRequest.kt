package ro.gnd.solutions.carebear.models

data class FriendRequest(
    var requesterId: String = "",
    var status: RequestStatus = RequestStatus.PENDING,
    var requesterEmail: String = ""
)