package com.example.carebear.services

import com.google.firebase.database.FirebaseDatabase

class FriendService private constructor() {
    private var database = FirebaseDatabase.getInstance()

    companion object {
    @Volatile
    private var instance: FriendService? = null

        fun getInstance(): FriendService {
            return instance ?: synchronized(this) {
                instance ?: FriendService().also {
                    instance = it
                    it.database = FirebaseDatabase.getInstance()
                }
            }
        }
    }

//    fun persistUser(currentUser: FirebaseUser) {
//        if (currentUser.displayName != null && currentUser.email != null) {
//            val user = User(
//                currentUser.uid,
//                currentUser.displayName.toString(),
//                currentUser.email.toString()
//            )
//            val usersRef = database.getReference("users")
//            usersRef.child(user.id).setValue(user)
//        }
//    }
//
//    fun persistUser(user: User) {
//        val usersRef = database.getReference("users")
//        usersRef.child(user.id).setValue(user)
//    }
}