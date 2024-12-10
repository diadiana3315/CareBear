package com.example.carebear.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class Chat {
    var id: String = ""
    var name: String = ""
    var lastMessage: String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    var timestamp: LocalDateTime = LocalDateTime.now()
}

