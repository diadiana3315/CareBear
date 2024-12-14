package com.example.carebear.services

import com.example.carebear.models.BaseUser
import com.example.carebear.models.Chat
import com.google.firebase.database.FirebaseDatabase

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

    private fun persistNewChat(chat: Chat) {
        val newChatRef = database.getReference("chats").push()
        chat.chatId = newChatRef.key.toString()
        newChatRef.setValue(chat)
    }
}