package com.example.carebear.services

import android.content.Context
import android.widget.Toast
import com.example.carebear.models.Friend
import com.example.carebear.models.FriendRequest
import com.example.carebear.models.RequestStatus
import com.example.carebear.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class UserService private constructor() {
    private var database = FirebaseDatabase.getInstance()

    companion object {
        @Volatile
        private var instance: UserService? = null

        fun getInstance(): UserService {
            return instance ?: synchronized(this) {
                instance ?: UserService().also {
                    instance = it
                    it.database = FirebaseDatabase.getInstance()
                }
            }
        }
    }

    fun persistUserIfNotPersisted(currentUser: FirebaseUser) {
        if (currentUser.email != null) {
            val displayName = extractAndCapitalizeEmailPrefix(currentUser.email.toString())
            val user = User(
                currentUser.uid,
                displayName,
                currentUser.email.toString(),
                emptyList(),
                emptyList()
            )

            persistUserIfNotPersisted(user)
        }
    }

    fun persistUserIfNotPersisted(user: User) {
        val usersRef = database.getReference("users")
        usersRef.child(user.id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    // User already exists, do nothing
                    println("User already exists in the database.")
                } else {
                    // User does not exist, persist the user
                    usersRef.child(user.id).setValue(user).addOnCompleteListener { persistTask ->
                        if (persistTask.isSuccessful) {
                            println("User persisted successfully!")
                        } else {
                            println("Error persisting user: ${persistTask.exception?.message}")
                        }
                    }
                }
            } else {
                println("Error checking user existence: ${task.exception?.message}")
            }
        }
    }

    fun persistUser(user: User) {
        val usersRef = database.getReference("users")
        usersRef.child(user.id).setValue(user)
    }

    private fun extractAndCapitalizeEmailPrefix(email: String): String {
        // Extract the part of the email before '@'
        val prefix = email.substringBefore("@")

        // Capitalize the first letter and return
        return prefix.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
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

                persistUser(receiver)
                Toast.makeText(context, "Friend request sent successfully!", Toast.LENGTH_SHORT).show()
            }
        }
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

                persistUser(targetUser)
                removeFriendRequest(friendId, targetUserId)
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

                persistUser(targetUser)
            }
        }
    }
}