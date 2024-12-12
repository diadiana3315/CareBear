package com.example.carebear.services

import android.content.Context
import android.widget.Toast
import com.example.carebear.models.ChatMembership
import com.example.carebear.models.Friend
import com.example.carebear.models.FriendRequest
import com.example.carebear.models.RequestStatus
import com.example.carebear.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class FriendService private constructor() {
    private var database = FirebaseDatabase.getInstance()
    private var userService: UserService = UserService.getInstance()

    companion object {
    @Volatile
    private var instance: FriendService? = null

        fun getInstance(): FriendService {
            return instance ?: synchronized(this) {
                instance ?: FriendService().also {
                    instance = it
                    it.database = FirebaseDatabase.getInstance()
                    it.userService = UserService.getInstance()
                }
            }
        }
    }

    fun sendFriendRequest(context: Context, senderId: String, receiverId: String, senderEmail: String) {
        val friendRequest = FriendRequest(requesterId = senderId, status = RequestStatus.PENDING, requesterEmail = senderEmail)
        val usersRef = database.getReference("users")
        usersRef.child(receiverId).get().addOnCompleteListener { task ->
            task.result.getValue(User::class.java)?.let { receiver ->
                val friendsRequests = ArrayList(receiver.friendRequests)
                if (!friendsRequests.any { it.requesterId == senderId }) {
                    friendsRequests.add(friendRequest)
                }
                receiver.friendRequests = friendsRequests

                userService.persistUser(receiver)
                Toast.makeText(context, "Friend request sent successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteFriend(context: Context, friendId: String) {
        val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (loggedUserId == null) {
            Toast.makeText(context, "Friend request deny", Toast.LENGTH_SHORT).show()
            return
        }

        deleteFriend(loggedUserId, friendId)
        deleteFriend(friendId, loggedUserId)
        Toast.makeText(context, "Friend deleted successfully!", Toast.LENGTH_SHORT).show()
    }

    fun denyFriendRequest(context: Context, friendId: String) {
        val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (loggedUserId == null) {
            Toast.makeText(context, "Friend request deny", Toast.LENGTH_SHORT).show()
            return
        }
        removeFriendRequest(loggedUserId, friendId)
        removeFriendRequest(friendId, loggedUserId)
    }

    fun acceptFriendRequest(context: Context, friendId: String, friendEmail: String) {
        val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
        val loggedUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (loggedUserId == null || loggedUserEmail == null) {
            Toast.makeText(context, "Friend request accept failed", Toast.LENGTH_SHORT).show()
            return
        }
        addFriendTo(loggedUserId, friendId, friendEmail)
        addFriendTo(friendId, loggedUserId, loggedUserEmail)
    }

    fun startChat(context: Context, chatId: String, userIdToStartChatWith: String, userEmailToStartChatWith: String) {
        val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
        val loggedUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (loggedUserId == null || loggedUserEmail == null) {
            Toast.makeText(context, "New chat failed", Toast.LENGTH_SHORT).show()
            return
        }

        startChat(chatId, loggedUserId, userIdToStartChatWith, userEmailToStartChatWith)
        startChat(chatId, userIdToStartChatWith, loggedUserId, loggedUserEmail)
    }

    private fun startChat(chatId: String, targetUserId: String, userIdToStartChatWith: String, userNameToStartChatWith: String) {
        val chatMembership = ChatMembership()
        chatMembership.chatId = chatId
        chatMembership.recipientId = userIdToStartChatWith
        chatMembership.recipientName = userNameToStartChatWith
        chatMembership.lastMessage = "Send the first message"
        val usersRef = database.getReference("users")
        usersRef.child(targetUserId).get().addOnCompleteListener { task ->
            task.result.getValue(User::class.java)?.let { targetUser ->
                val chats = ArrayList(targetUser.chats)
                if (!chats.any { it.recipientId == userIdToStartChatWith }) {
                    chats.add(chatMembership)
                }
                targetUser.chats = chats

                userService.persistUser(targetUser)
                removeFriendRequest(userIdToStartChatWith, targetUserId)
            }
        }
    }

    private fun addFriendTo(targetUserId: String, friendId: String, friendEmail: String) {
        val friend = Friend(friendId, friendEmail)
        val usersRef = database.getReference("users")
        usersRef.child(targetUserId).get().addOnCompleteListener { task ->
            task.result.getValue(User::class.java)?.let { targetUser ->
                val friends = ArrayList(targetUser.friends)
                if (!friends.any { it.friendId == friendId }) {
                    friends.add(friend)
                }
                targetUser.friends = friends

                userService.persistUser(targetUser)
                removeFriendRequest(friendId, targetUserId)
            }
        }
    }

    private fun deleteFriend(targetUserId: String, friendId: String) {
        val usersRef = database.getReference("users")
        usersRef.child(targetUserId).get().addOnCompleteListener { task ->
            task.result.getValue(User::class.java)?.let { targetUser ->
                val friends = ArrayList(targetUser.friends)
                    .filter { it.friendId != friendId }
                targetUser.friends = friends

                userService.persistUser(targetUser)
            }
        }
    }

    private fun removeFriendRequest(senderId: String, receiverId: String) {
        val usersRef = database.getReference("users")
        usersRef.child(receiverId).get().addOnCompleteListener { task ->
            task.result.getValue(User::class.java)?.let { targetUser ->
                val friendsRequests = ArrayList(targetUser.friendRequests)
                    .filter { it.requesterId != senderId }
                targetUser.friendRequests = friendsRequests

                userService.persistUser(targetUser)
            }
        }
    }
}