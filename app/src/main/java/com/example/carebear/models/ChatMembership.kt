package com.example.carebear.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class ChatMembership {
    var chatId: String = ""
    var recipientId: String = ""
    var recipientName: String = ""
    var lastMessage: String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    var timestamp: String = LocalDateTime.now().toString()
}

