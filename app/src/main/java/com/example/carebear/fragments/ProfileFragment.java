package com.example.carebear.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carebear.R;
import com.example.carebear.activities.authentications.LoginActivity;
import com.example.carebear.activities.settings.EditProfileActivity;
import com.example.carebear.activities.settings.ChangePasswordActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    private TextView tvUsername, tvBio, tvMedicalConditions, tvMedications, tvAllergies;
    private Button btnEditProfile, btnChangePassword, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getContext(), "User not authenticated!", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Initialize Firebase references
        String currentUserId = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);

        // Bind views
        tvUsername = view.findViewById(R.id.tv_username);
        tvBio = view.findViewById(R.id.tv_bio);
        tvMedicalConditions = view.findViewById(R.id.tv_medical_conditions);
        tvMedications = view.findViewById(R.id.tv_medications);
        tvAllergies = view.findViewById(R.id.tv_allergies);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Set up button click listeners for navigation
        btnEditProfile.setOnClickListener(v -> navigateToEditProfile());
        btnChangePassword.setOnClickListener(v -> navigateToChangePassword());
        btnLogout.setOnClickListener(v -> logout());

        // Load user data
        loadUserData(currentUser);

        return view;
    }

    private void navigateToEditProfile() {
        // Navigate to EditProfileActivity
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void navigateToChangePassword() {
        // Navigate to ChangePasswordActivity
        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }


    private void logout() {
        // Logout user and navigate to login activity
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to login screen (assuming you have a login activity)
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish(); // Finish ProfileFragment so the user can't go back
    }

    private void loadUserData(FirebaseUser currentUser) {
        // Display username (email) from Firebase Authentication
        String usernameFromEmail = currentUser.getEmail();
        tvUsername.setText(usernameFromEmail);

        // Load additional user data from Firebase Database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Check if the username is in the database, otherwise use the email
                    String username = snapshot.child("username").getValue(String.class);
                    if (username != null && !username.isEmpty()) {
                        tvUsername.setText(username);
                    }

                    // Load other fields (bio, medical conditions, medications, allergies)
                    String bio = snapshot.child("bio").getValue(String.class);
                    String medicalConditions = snapshot.child("medical_conditions").getValue(String.class);
                    String medications = snapshot.child("medications").getValue(String.class);
                    String allergies = snapshot.child("allergies").getValue(String.class);

                    // Populate fields with data from the database
                    tvBio.setText(bio != null ? bio : "No bio available.");
                    tvMedicalConditions.setText(medicalConditions != null ? medicalConditions : "No medical conditions listed.");
                    tvMedications.setText(medications != null ? medications : "No medications listed.");
                    tvAllergies.setText(allergies != null ? allergies : "No allergies listed.");
                } else {
                    Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
