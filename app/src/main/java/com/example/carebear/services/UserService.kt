package com.example.carebear.services

import com.example.carebear.models.BaseUser
import com.example.carebear.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lombok.extern.java.Log

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

    fun getBaseLoggedUser(): BaseUser {
        val loggedUserId: String? = FirebaseAuth.getInstance().currentUser?.uid
        val loggedUserEmail: String? = FirebaseAuth.getInstance().currentUser?.email
        val id = loggedUserId ?: ""
        val name = loggedUserEmail ?: ""
        val email = loggedUserEmail ?: ""

        return BaseUser(id, name, email)
    }

    fun getLoggedUser(callback: (User?) -> Unit) {
        val loggedUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

        if (loggedUserId == null) {
            callback(null)
            return
        }

        val database = FirebaseDatabase.getInstance().getReference("users").child(loggedUserId)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    fun getLoggedUserReference(): DatabaseReference {
        val loggedUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

        return if (loggedUserId != null) {
            database.getReference("users").child(loggedUserId);
        } else {
            database.getReference("users").child("")
        }
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