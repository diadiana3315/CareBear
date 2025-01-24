package ro.gnd.solutions.carebear.activities.groups

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ro.gnd.solutions.carebear.R
import ro.gnd.solutions.carebear.services.GroupService

class CreateGroupChatActivity : AppCompatActivity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val groupService: GroupService = GroupService.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var submitButton: Button
    private lateinit var groupNameEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_group_chat)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        submitButton = findViewById(R.id.btn_submit)
        groupNameEditText = findViewById(R.id.et_group_name)

        initOnCreateGroup()
        initOnBackButtonClick()
    }

    private fun initOnCreateGroup() {
        submitButton.setOnClickListener {
            val groupName = groupNameEditText.text.toString().trim()

            if (groupName.isEmpty()) {
                Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show()
            } else {
                groupService.createGroup(this, groupName)
            }
            finish()
        }
    }

    private fun initOnBackButtonClick() {
        val backButton: ImageButton = findViewById(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }
    }
}