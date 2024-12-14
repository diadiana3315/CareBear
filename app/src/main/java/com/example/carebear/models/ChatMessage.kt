package com.example.carebear.models

class ChatMessage {
    var sender: BaseUser = BaseUser()
    var message: String = ""
    var timestamp: Long = System.currentTimeMillis()
}
