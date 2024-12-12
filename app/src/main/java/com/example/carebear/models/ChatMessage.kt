package com.example.carebear.models

class ChatMessage {
    var message: String = ""
    var lastMessageTimestamp: java.sql.Timestamp = java.sql.Timestamp(System.currentTimeMillis())
}
