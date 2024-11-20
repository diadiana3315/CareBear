package com.example.carebear.services

import android.content.Context
import android.widget.Toast
import com.example.carebear.models.FriendRequest
import com.example.carebear.models.RequestStatus
import com.example.carebear.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

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

    fun sendFriendRequest(context: Context, senderId: String, receiverId: String) {
        val friendRequest = FriendRequest(requesterId = senderId, status = RequestStatus.PENDING)
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
}