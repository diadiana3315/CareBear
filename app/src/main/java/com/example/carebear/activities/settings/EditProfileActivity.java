package com.example.carebear.activities.settings;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carebear.R;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView ivProfilePicture;
    private EditText etUsername, etBio;
    private Button btnChangePicture, btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Bind views
        etUsername = findViewById(R.id.et_username);
        etBio = findViewById(R.id.et_bio);
        btnChangePicture = findViewById(R.id.btn_change_picture);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        // Load existing profile data if available
        loadUserProfile();

        // Set up button listeners
        btnChangePicture.setOnClickListener(v -> changeProfilePicture());
        btnSaveChanges.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserProfile() {
        // Placeholder: Load profile data (you would fetch from a database or server)
        etUsername.setText("SampleUsername"); // Example of setting current username
        etBio.setText("This is a sample bio."); // Example of setting current bio
    }

    private void changeProfilePicture() {
        // Placeholder: Implement functionality to change profile picture
        Toast.makeText(this, "Change profile picture clicked", Toast.LENGTH_SHORT).show();
    }

    private void saveProfileChanges() {
        // Placeholder: Save profile changes
        String newUsername = etUsername.getText().toString();
        String newBio = etBio.getText().toString();

        // Log data or use it to update profile in your database
        Log.d("EditProfileActivity", "Username: " + newUsername + ", Bio: " + newBio);

        // Notify the user and finish the activity
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish(); // Go back to ProfileFragment
    }
}
