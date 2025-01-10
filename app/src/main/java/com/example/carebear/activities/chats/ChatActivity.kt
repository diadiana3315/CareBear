package com.example.carebear.activities.chats

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
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
import com.example.carebear.models.BaseUser
import com.example.carebear.models.Chat
import com.example.carebear.models.ChatMessage
import com.example.carebear.models.ChatNotification
import com.example.carebear.services.NotificationService
import com.example.carebear.services.UserService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.security.AccessController.getContext

class ChatActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val notificationService: NotificationService = NotificationService.getInstance()
    private val loggedUser = UserService.getInstance().getBaseLoggedUser()

    private var isGroupChat = false
    private var chatName = ""

    private lateinit var chatId: String
    private lateinit var messages: List<ChatMessage>
    private lateinit var chatMembers: List<BaseUser>
    private lateinit var chatRef: DatabaseReference
    private lateinit var messagesView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var attachMediaButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        // Send message
        messageInput = findViewById(R.id.et_message)
        sendButton = findViewById(R.id.btn_send)
        initSendMessage()

        // Media
        attachMediaButton = findViewById(R.id.btn_attach_media)
        attachMediaButton.setOnClickListener{
            intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/* video/*");
            this.startActivity(intent)
        };

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
        sendNotification(message)
    }

    private fun buildNewChatMessage(message: String): ChatMessage {
        val chatMessage = ChatMessage()
        chatMessage.message = message
        chatMessage.sender = loggedUser

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
                    chatMembers = chat.chatMembers
                    isGroupChat = chat.isGroupChat
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

    private fun sendNotification(message: String) {
        val targetUsers = chatMembers.filter { user -> user.id != loggedUser.id }
        val chatNotification = ChatNotification()

        chatNotification.chatId = this.chatId
        chatNotification.message = message

        if (isGroupChat) {
            chatNotification.chatName = this.chatName
        } else {
            chatNotification.chatName = loggedUser.name
        }

        notificationService.sendChatNotification(targetUsers, chatNotification)
    }

    private fun endSessionWithError() {
        Toast.makeText(this, "Chat initialisation failed", Toast.LENGTH_SHORT).show()
        finish()
    }
}