package com.example.carebear.services

import android.util.Log
import com.example.carebear.models.BaseUser
import com.example.carebear.models.Chat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatService private constructor() {
    private var database = FirebaseDatabase.getInstance()
    private var userService: UserService = UserService.getInstance()

    companion object {
        @Volatile
        private var instance: ChatService? = null

        fun getInstance(): ChatService {
            return instance ?: synchronized(this) {
                instance ?: ChatService().also {
                    instance = it
                    it.database = FirebaseDatabase.getInstance()
                    it.userService = UserService.getInstance()
                }
            }
        }
    }

    fun createPrivateChat(firstUser: BaseUser, secondUser: BaseUser): Chat {
        val chat = Chat()
        chat.chatMembers = listOf(firstUser, secondUser)

        persistNewChat(chat)
        return chat
    }

    fun createGroupChat(chatName: String): Chat {
        val chat = Chat()
        chat.chatMembers = listOf()
        chat.isGroupChat = true
        chat.chatName = chatName

        persistNewChat(chat)
        return chat
    }

    fun joinGroupChat(user: BaseUser, chatId: String) {
        val chatRef = database.getReference("chats").child(chatId)

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val chat = snapshot.getValue(Chat::class.java)

                    val chatMemberIds = chat?.chatMembers?.map { chatMember -> chatMember.id }
                    if (chatMemberIds != null && !chatMemberIds.contains(user.id)) {
                        val updatedMembers = chat.chatMembers.toMutableList()
                        updatedMembers.add(user)
                        chatRef.child("chatMembers").setValue(updatedMembers)
                            .addOnSuccessListener {
                                Log.d("JoinGroupChat", "User added to the chat successfully.")
                            }
                            .addOnFailureListener { e ->
                                Log.e(
                                    "JoinGroupChat",
                                    "Failed to add user to the chat: ${e.message}"
                                )
                            }
                    }
                } else {
                    // Chat with the given ID does not exist
                    Log.d("JoinGroupChat", "Chat with ID $chatId does not exist.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("JoinGroupChat", "Database error: ${error.message}")
            }
        })
    }


    private fun persistNewChat(chat: Chat) {
        val newChatRef = database.getReference("chats").push()
        chat.chatId = newChatRef.key.toString()
        newChatRef.setValue(chat)
    }
}