package com.example.carebear.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.activities.friends.AddNewFriendActivity
import com.example.carebear.activities.friends.FriendRequestsActivity
import com.example.carebear.adapters.FriendAdapter
import com.example.carebear.adapters.FriendRequestAdapter
import com.example.carebear.adapters.SearchedUserAdapter
import com.example.carebear.models.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendsFragment : Fragment() {
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_users, container, false)

        initFriends()

        // Set an OnClickListener for the Search User Button
        val buttonAddNewFriend: Button = rootView.findViewById(R.id.button_add_friend)
        buttonAddNewFriend.setOnClickListener {
            val intent = Intent(context, AddNewFriendActivity::class.java)
            context?.startActivity(intent)
        }

        // Set an OnClickListener for the Notification Bell Button
        val notificationButton: ImageButton = rootView.findViewById(R.id.notification_button)
        notificationButton.setOnClickListener {
            val intent = Intent(context, FriendRequestsActivity::class.java)
            context?.startActivity(intent)
    }

        return rootView
    }

    private fun initFriends() {
        val friends = mutableListOf<Friend>()
        val friendsRef = database.getReference("users").child(loggedUserId!!).child("friends")
        friendsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friends.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val friend = userSnapshot.getValue(Friend::class.java)
                    friend.let {
                        if (it != null) {
                            friends.add(it)
                        }
                    }
                }

                displayFriends(friends)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }

    private fun displayFriends(friends: List<Friend>) {
        val friendsView: RecyclerView = rootView.findViewById(R.id.friends_recycler_view)
        friendsView.layoutManager = LinearLayoutManager(requireContext())
        val friendsAdapter = FriendAdapter(this.requireContext(), friends)
        friendsView.adapter = friendsAdapter
    }
}