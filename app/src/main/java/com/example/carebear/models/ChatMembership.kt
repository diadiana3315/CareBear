package com.example.carebear.models

class ChatMembership {
    var chatId: String = ""
    var recipientId: String = ""
    var recipientName: String = ""
    var lastMessage: String = ""
    var timestamp: Long = System.currentTimeMillis()
}
