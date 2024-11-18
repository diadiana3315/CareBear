package com.example.carebear.services

import com.example.carebear.models.User
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

    fun persistUser(currentUser: FirebaseUser) {
        if (currentUser.email != null) {
            val displayName = extractAndCapitalizeEmailPrefix(currentUser.email.toString())
            val user = User(
                currentUser.uid,
                displayName,
                currentUser.email.toString()
            )
            val usersRef = database.getReference("users")
            usersRef.child(user.id).setValue(user)
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
}