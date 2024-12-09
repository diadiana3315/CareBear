package com.example.carebear.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carebear.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etUsername, etBio, etMedicalConditions, etMedications, etAllergies;
    private Button btnSaveChanges;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase references
        String currentUserId = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);

        // Bind views
        etUsername = findViewById(R.id.et_username);
        etBio = findViewById(R.id.et_bio);
        etMedicalConditions = findViewById(R.id.et_medical_conditions);
        etMedications = findViewById(R.id.et_medications);
        etAllergies = findViewById(R.id.et_allergies);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        // Load existing profile data into fields
        loadUserProfile();

        // Set up button listener for saving changes
        btnSaveChanges.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserProfile() {
        // Fetch user data from Firebase and populate fields
        userRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String username = snapshot.child("username").getValue(String.class);
                String bio = snapshot.child("bio").getValue(String.class);
                String medicalConditions = snapshot.child("medical_conditions").getValue(String.class);
                String medications = snapshot.child("medications").getValue(String.class);
                String allergies = snapshot.child("allergies").getValue(String.class);

                etUsername.setText(username != null ? username : "");
                etBio.setText(bio != null ? bio : "");
                etMedicalConditions.setText(medicalConditions != null ? medicalConditions : "");
                etMedications.setText(medications != null ? medications : "");
                etAllergies.setText(allergies != null ? allergies : "");
            } else {
                Toast.makeText(this, "User data not found!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Failed to load user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveProfileChanges() {
        // Get updated values from input fields
        String updatedUsername = etUsername.getText().toString().trim();
        String updatedBio = etBio.getText().toString().trim();
        String updatedMedicalConditions = etMedicalConditions.getText().toString().trim();
        String updatedMedications = etMedications.getText().toString().trim();
        String updatedAllergies = etAllergies.getText().toString().trim();

        // Save updated data to Firebase
        if (!updatedUsername.isEmpty()) {
            userRef.child("username").setValue(updatedUsername);
        }
        userRef.child("bio").setValue(updatedBio);
        userRef.child("medical_conditions").setValue(updatedMedicalConditions);
        userRef.child("medications").setValue(updatedMedications);
        userRef.child("allergies").setValue(updatedAllergies).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close activity and return to ProfileFragment
            } else {
                Toast.makeText(this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
