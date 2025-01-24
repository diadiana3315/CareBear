package ro.gnd.solutions.carebear.models

class ChatMessage {
    var sender: BaseUser = BaseUser()
    var message: String = ""
    var timestamp: Long = System.currentTimeMillis()
}
