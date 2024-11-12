package com.example.carebear.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carebear.R;
import com.example.carebear.activities.settings.ChangePasswordActivity;
import com.example.carebear.activities.settings.EditProfileActivity;
import com.example.carebear.activities.MainActivity;
import com.example.carebear.activities.settings.NotificationPreferencesActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private ImageView profilePicture;
    private TextView tvUsername, tvBio;
    private EditText etMedications, etAllergies;
//    private RadioGroup rgVisibility;
    private Button btnEditProfile, btnChangePassword, btnNotificationPreferences, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        // Bind views
        profilePicture = view.findViewById(R.id.profile_picture);
        tvUsername = view.findViewById(R.id.tv_username);
        tvBio = view.findViewById(R.id.tv_bio);
        etMedications = view.findViewById(R.id.et_medications);
        etAllergies = view.findViewById(R.id.et_allergies);
//        rgVisibility = view.findViewById(R.id.rg_visibility);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnNotificationPreferences = view.findViewById(R.id.btn_notification_preferences);
//        btnThemeOptions = view.findViewById(R.id.btn_theme_options);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Load user data
        loadUserData();

        // Set button listeners
        btnEditProfile.setOnClickListener(v -> editProfile());
        btnChangePassword.setOnClickListener(v -> changePassword());
        btnNotificationPreferences.setOnClickListener(v -> openNotificationPreferences());
//        btnThemeOptions.setOnClickListener(v -> openThemeOptions());
        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserData() {
        // Placeholder: Load user data from your data source
        tvUsername.setText("SampleUsername");
        tvBio.setText("This is a sample bio.");
//        etMedications.setText("Sample medication");
//        etAllergies.setText("Sample allergy");
        // Set initial visibility based on saved preferences
//        RadioButton rbPublic = getView().findViewById(R.id.rb_public);
//        rbPublic.setChecked(true); // Example of setting default to public
    }

    private void editProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void changePassword() {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void openNotificationPreferences() {
        Intent intent = new Intent(getActivity(), NotificationPreferencesActivity.class);
        startActivity(intent);
    }

    private void logout() {
        // Log out from firebase
        mAuth.signOut();

        // Clear any user data if necessary, such as shared preferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Navigate to LoginActivity (assuming this activity exists)
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears the back stack
        startActivity(intent);

        // Show a message to the user
        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
