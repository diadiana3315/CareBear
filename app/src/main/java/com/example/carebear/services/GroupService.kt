package com.example.carebear.services

import android.content.Context
import android.widget.Toast
import com.example.carebear.models.GroupChat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupService {
    private var database = FirebaseDatabase.getInstance()
    private var chatService = ChatService.getInstance()

    companion object {
        @Volatile
        private var instance: GroupService? = null

        fun getInstance(): GroupService {
            return instance ?: synchronized(this) {
                instance ?: GroupService().also {
                    instance = it
                    it.database = FirebaseDatabase.getInstance()
                }
            }
        }
    }

    fun createGroup(context: Context, groupName: String) {
        database.getReference("groups").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var isDuplicate = false
                    for (groupSnapshot in snapshot.children) {
                        val existingGroupName = groupSnapshot.child("name").getValue(String::class.java)
                        if (existingGroupName.equals(groupName, ignoreCase = true)) {
                            isDuplicate = true
                            break
                        }
                    }

                    if (isDuplicate) {
                        Toast.makeText(context, "Group name already exists!", Toast.LENGTH_SHORT).show()
                    } else {
                        persistGroup(groupName)
                        Toast.makeText(context, "Group was created", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    persistGroup(groupName)
                    Toast.makeText(context, "Group was created", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun persistGroup(name: String) {
        val group = GroupChat("", name)
        val groupChat = chatService.createGroupChat(name)
        val newGroupChatRef = database.getReference("groups").push()
        group.id = newGroupChatRef.key.toString()
        group.name = name
        group.chatId = groupChat.chatId
        newGroupChatRef.setValue(group)
    }
}