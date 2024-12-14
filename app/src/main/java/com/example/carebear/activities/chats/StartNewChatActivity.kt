package com.example.carebear.activities.chats

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.adapters.AvailableFriendToChatAdapter
import com.example.carebear.models.Friend
import com.example.carebear.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StartNewChatActivity : AppCompatActivity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var availableFriendsView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_new_chat)

        availableFriendsView = findViewById(R.id.start_new_chat_recycler_view)

        initAvailableFriendsToChat()

        initOnBackButtonClick()
    }

    private fun initAvailableFriendsToChat() {
        val friends = mutableListOf<Friend>()
        val userRef = database.getReference("users").child(loggedUserId!!)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friends.clear()
                val user = dataSnapshot.getValue(User::class.java)
                user?.friends?.forEach { friend -> friend.let {
                    if (user.chats.none{ chat -> chat.recipientId == friend.friendId })
                    friends.add(it)
                } }

                displayAvailableFriendsToChats(friends)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }

    private fun displayAvailableFriendsToChats(friends: List<Friend>) {
        availableFriendsView.layoutManager = LinearLayoutManager(this)
        val friendRequestsAdapter = AvailableFriendToChatAdapter(this, friends)
        availableFriendsView.adapter = friendRequestsAdapter
    }

    private fun initOnBackButtonClick() {
        val backButton: ImageButton = findViewById(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }
    }
}