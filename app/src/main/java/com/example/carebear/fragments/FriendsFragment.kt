package com.example.carebear.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.activities.friends.AddNewFriendActivity
import com.example.carebear.adapters.SearchedUserAdapter
import com.example.carebear.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendsFragment : Fragment() {
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_users, container, false)

        initFoundUsers()

        // Set an OnClickListener for the Search User Button
        val buttonAddNewFriend: Button = rootView.findViewById(R.id.button_add_friend)
        buttonAddNewFriend.setOnClickListener {
            val intent = Intent(context, AddNewFriendActivity::class.java)
            context?.startActivity(intent)
        }

        return rootView
    }

    private fun initFoundUsers() {
        val usersRef = database.getReference("users")
        usersRef.orderByChild("email").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
                val databaseUsers = mutableListOf<User>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null && user.id != loggedUserId) {
                        user.let { databaseUsers.add(it) }
                    }
                }
                displayFoundUsers(databaseUsers)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }

    private fun displayFoundUsers(users: List<User>) {
        val foundUsersView: RecyclerView = rootView.findViewById(R.id.found_users_recycler_view)
        foundUsersView.layoutManager = LinearLayoutManager(requireContext())
        val searchedUsersAdapter = context?.let {
            SearchedUserAdapter(it, users) { user ->
                println("Fetched user: ${user.email}")
            }
        }
        foundUsersView.adapter = searchedUsersAdapter
    }
}