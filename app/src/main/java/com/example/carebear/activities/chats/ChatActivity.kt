package com.example.carebear.activities.chats

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.carebear.R
import com.example.carebear.fragments.ChatsFragment.CHAT_ID_KEY

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        val chatId = intent.getStringExtra(CHAT_ID_KEY)

    }
}