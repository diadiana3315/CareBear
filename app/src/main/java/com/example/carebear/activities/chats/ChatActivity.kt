package com.example.carebear.activities.chats

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.adapters.MessageAdapter
import com.example.carebear.fragments.ChatsFragment.CHAT_ID_KEY
import com.example.carebear.models.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var messagesView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        messagesView = findViewById(R.id.recycler_view_messages)

        val chatId = intent.getStringExtra(CHAT_ID_KEY)

        if (chatId != null) {
            initChat(chatId)
        } else {
            endSessionWithError()
        }
    }

    private fun initChat(chatId: String) {
        val chatRef = database.getReference("chats").child(chatId)
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chat = dataSnapshot.getValue(Chat::class.java)
                if (chat == null) {
                    endSessionWithError()
                } else {
                    displayChat(chat)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                endSessionWithError()
            }
        })
    }

    private fun displayChat(chat: Chat) {
        messagesView.layoutManager = LinearLayoutManager(this)
        val messagesAdapter = MessageAdapter(this, chat.messages)
        messagesView.adapter = messagesAdapter
    }

    private fun endSessionWithError() {
        Toast.makeText(this, "Chat initialisation failed", Toast.LENGTH_SHORT).show()
        finish()
    }
}