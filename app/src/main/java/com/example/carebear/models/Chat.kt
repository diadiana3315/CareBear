package com.example.carebear.models

class Chat {
    var chatId: String = ""
    var chatName: String = ""
    var chatMembers: List<BaseUser> = listOf()
    var isGroupChat: Boolean = false
    var lastMessage: String = "Send the first message"
    var lastMessageTimestamp: Long = System.currentTimeMillis()
    var messages: List<ChatMessage> = listOf()
}