package com.example.carebear.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

class FriendsFragment : Fragment() {
    private val databaseUsers = mutableListOf<User>()
    private var searchedUserEmail = ""
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
        val buttonSearchUser: Button = rootView.findViewById(R.id.button_search_user)
        buttonSearchUser.setOnClickListener {
            filterUsersBySearchedEmail()
        }

        return rootView
    }

    private fun initFoundUsers() {
        val usersRef = database.getReference("users")
        usersRef.orderByChild("email").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                databaseUsers.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { databaseUsers.add(it) }
                }
                filterUsersBySearchedEmail()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }

    private fun filterUsersBySearchedEmail() {
        val inputSearchUser: EditText = rootView.findViewById(R.id.input_search_user)
        searchedUserEmail = inputSearchUser.text.toString()
        val displayedUsers = mutableListOf<User>()
        databaseUsers.forEach { user ->
            if (user.email.contains(searchedUserEmail)) displayedUsers.add(
                user
            )
        }
        displayFoundUsers(displayedUsers)
    }

    private fun displayFoundUsers(users: List<User>) {
        val foundUsersView: RecyclerView = rootView.findViewById(R.id.found_users_recycler_view)
        foundUsersView.layoutManager = LinearLayoutManager(requireContext())
        val searchedUsersAdapter = SearchedUserAdapter(users) { user ->
            println("Fetched user: ${user.email}")
        }
        foundUsersView.adapter = searchedUsersAdapter
    }
}