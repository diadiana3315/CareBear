package com.example.carebear.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.adapters.SearchedUserAdapter
import com.example.carebear.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendsFragment  : Fragment(){
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_users, container, false)

        initDatabase()

        return rootView
    }

    private fun initDatabase() {
        val usersRef = database.getReference("users")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { users.add(it) }
                }

                val foundUsersView: RecyclerView = rootView.findViewById(R.id.found_users_recycler_view)
                foundUsersView.layoutManager = LinearLayoutManager(requireContext()) // Use requireContext() or context

                val searchedUsersAdapter = SearchedUserAdapter(users) { user ->
                    // Handle button click action for a specific user
                    println("Button clicked for user: ${user.name}")
                }
                foundUsersView.adapter = searchedUsersAdapter


//                val myFriendsView: RecyclerView = rootView.findViewById(R.id.my_friends_recycler_view)
//                myFriendsView.layoutManager = LinearLayoutManager(requireContext()) // Use requireContext() or context
//
//                val myFriendsAdapter = SearchedUserAdapter(users) { user ->
//                    // Handle button click action for a specific user
//                    println("Button clicked for user: ${user.name}")
//                }
//                myFriendsView.adapter = myFriendsAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }
}