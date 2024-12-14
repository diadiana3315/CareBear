package com.example.carebear.activities.chats

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.adapters.MessageAdapter
import com.example.carebear.fragments.ChatsFragment.CHAT_ID_KEY
import com.example.carebear.models.Chat
import com.example.carebear.models.ChatMessage
import com.example.carebear.services.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
    private var messages: List<ChatMessage> = listOf()
    private var chatId: String = ""

    private lateinit var chatRef: DatabaseReference
    private lateinit var messagesView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        // Send message
        messageInput = findViewById(R.id.et_message)
        sendButton = findViewById(R.id.btn_send)
        initSendMessage()

        // Messages display
        messagesView = findViewById(R.id.recycler_view_messages)
        val chatId = intent.getStringExtra(CHAT_ID_KEY)
        if (chatId != null) {
            this.chatId = chatId
            initChat(chatId)
        } else {
            endSessionWithError()
        }
    }

    private fun initSendMessage() {
        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                messageInput.text.clear()
            }
        }
    }

    private fun sendMessage(message: String) {
        val messages = this.messages.toMutableList()
        messages.add(buildNewChatMessage(message))
        chatRef.child("messages").setValue(messages)
        chatRef.child("lastMessage").setValue(message)
        chatRef.child("lastMessageTimestamp").setValue(System.currentTimeMillis())
    }

    private fun buildNewChatMessage(message: String): ChatMessage {
        val chatMessage = ChatMessage()
        chatMessage.message = message
        chatMessage.sender = UserService.getInstance().getBaseLoggedUser()

        return chatMessage
    }

    private fun initChat(chatId: String) {
        val chatRef = database.getReference("chats").child(chatId)
        this.chatRef = chatRef
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chat = dataSnapshot.getValue(Chat::class.java)
                if (chat == null) {
                    endSessionWithError()
                } else {
                    messages = chat.messages
                    displayChat()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                endSessionWithError()
            }
        })
    }

    private fun displayChat() {
        messagesView.layoutManager = LinearLayoutManager(this)
        val messagesAdapter = MessageAdapter(this, messages)
        messagesView.adapter = messagesAdapter
    }

    private fun endSessionWithError() {
        Toast.makeText(this, "Chat initialisation failed", Toast.LENGTH_SHORT).show()
        finish()
    }
}