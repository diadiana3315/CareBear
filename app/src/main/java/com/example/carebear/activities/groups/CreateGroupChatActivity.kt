package com.example.carebear.activities.groups

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CreateGroupChatActivity : AppCompatActivity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_group_chat)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initOnBackButtonClick()
    }

    private fun initOnBackButtonClick() {
        val backButton: ImageButton = findViewById(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }
    }
}