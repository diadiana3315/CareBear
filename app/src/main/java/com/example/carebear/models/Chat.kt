package com.example.carebear.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class Chat {
    var chatId: String = ""
    var chatMembers: List<BaseUser> = listOf()
    var isGroupChat: Boolean = false
    var lastMessage: String = "Send the first message"
    @RequiresApi(Build.VERSION_CODES.O)
    var lastMessageTimestamp: String = LocalDateTime.now().toString()
    var messages: List<ChatMessage> = listOf()
}