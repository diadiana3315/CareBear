package com.example.carebear.services

import com.example.carebear.models.BaseUser
import com.example.carebear.models.Chat
import com.example.carebear.models.ChatMessage
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
        chat.messages = getMockMessages(firstUser, secondUser) // TODO: REMOVE AFTER

        persistNewChat(chat)
        return chat
    }

    private fun persistNewChat(chat: Chat) {
        val newChatRef = database.getReference("chats").push()
        chat.chatId = newChatRef.key.toString()
        newChatRef.setValue(chat)
    }

    private fun getMockMessages(firstUser: BaseUser, secondUser: BaseUser): List<ChatMessage> {
        val message1 = ChatMessage()
        message1.message = "This is first message from " + firstUser.name
        message1.sender = firstUser

        val message2 = ChatMessage()
        message2.message = "This is second message from " + secondUser.name
        message2.sender = secondUser

        return listOf(message1, message2)
    }
}