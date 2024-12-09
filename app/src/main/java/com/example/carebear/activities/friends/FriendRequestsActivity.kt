package com.example.carebear.activities.friends

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.adapters.FriendRequestAdapter
import com.example.carebear.models.FriendRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendRequestsActivity : AppCompatActivity() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
    private val friendRequests = mutableListOf<FriendRequest>()

    private lateinit var friendRequestsView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_friend_requests)

        friendRequestsView = findViewById(R.id.found_users_recycler_view)

        initFriendRequests()

        initOnBackButtonClick()
    }

    private fun initOnBackButtonClick() {
        val backButton: ImageButton = findViewById(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initFriendRequests() {
        val friendRequestsRef =
            database.getReference("users").child(loggedUserId!!).child("friendRequests")
        friendRequestsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendRequests.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val friendRequest = userSnapshot.getValue(FriendRequest::class.java)
                    friendRequest.let {
                        if (it != null) {
                            friendRequests.add(it)
                        }
                    }
                }

                displayFriendRequests(friendRequests)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }

    private fun displayFriendRequests(friendRequests: List<FriendRequest>) {
        friendRequestsView.layoutManager = LinearLayoutManager(this)
        val friendRequestsAdapter = FriendRequestAdapter(this, friendRequests)
        friendRequestsView.adapter = friendRequestsAdapter
    }
}