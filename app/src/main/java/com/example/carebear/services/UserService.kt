package com.example.carebear.services

import com.example.carebear.models.User
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

    fun persistUser(user: User) {
        val usersRef = database.getReference("users")
        usersRef.child(user.id).setValue(user)
    }
}