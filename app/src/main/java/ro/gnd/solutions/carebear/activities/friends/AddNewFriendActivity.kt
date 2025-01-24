package ro.gnd.solutions.carebear.activities.friends

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ro.gnd.solutions.carebear.R
import ro.gnd.solutions.carebear.adapters.SearchedUserAdapter
import ro.gnd.solutions.carebear.models.User

class AddNewFriendActivity : AppCompatActivity() {
    private val databaseUsers = mutableListOf<User>()
    private var searchedUserEmail = ""
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var foundUsersView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_friend)

        foundUsersView = findViewById(R.id.found_users_recycler_view)

        initFoundUsers()

        initOnBackButtonClick()
        initOnSearchUserButtonClick()
    }

    private fun initOnSearchUserButtonClick() {
        val buttonSearchUser: Button = findViewById(R.id.button_search_user)
        buttonSearchUser.setOnClickListener {
            filterUsersBySearchedEmail()
        }
        val inputSearchUser: EditText = findViewById(R.id.input_search_user)
        inputSearchUser.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                actionId == android.view.inputmethod.EditorInfo.IME_ACTION_GO) {
                filterUsersBySearchedEmail()
                true
            } else {
                false
            }
        }
    }

    private fun initOnBackButtonClick() {
        val backButton: ImageButton = findViewById(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initFoundUsers() {
        val usersRef = database.getReference("users")
        usersRef.orderByChild("email").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
                databaseUsers.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null && user.id != loggedUserId) {
                        user.let { databaseUsers.add(it) }
                    }
                }
                filterUsersBySearchedEmail()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }

    private fun filterUsersBySearchedEmail() {
        val inputSearchUser: EditText = findViewById(R.id.input_search_user)
        searchedUserEmail = inputSearchUser.text.toString()
        val displayedUsers = mutableListOf<User>()
        databaseUsers.forEach { user ->
            if (user.email.contains(searchedUserEmail)) displayedUsers.add(
                user
            )
        }

        filterUsersWithAlreadyExistingFriendRequest(displayedUsers)
    }

    private fun filterUsersWithAlreadyExistingFriendRequest(users: List<User>) {
        val displayedUsers = mutableListOf<User>()
        users.forEach { user ->
            if (
                !user.friendRequests.any { it.requesterId == loggedUserId } &&
                !user.friends.any { it.friendId == loggedUserId }
                ) {
                displayedUsers.add(user)
            }
        }

        displayFoundUsers(displayedUsers)
    }

    private fun displayFoundUsers(users: List<User>) {
        foundUsersView.layoutManager = LinearLayoutManager(this)
        val searchedUsersAdapter = SearchedUserAdapter(this, users)
        foundUsersView.adapter = searchedUsersAdapter
    }
}