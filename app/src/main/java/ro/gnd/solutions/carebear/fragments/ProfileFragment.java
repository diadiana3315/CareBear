package ro.gnd.solutions.carebear.fragments;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ro.gnd.solutions.carebear.R;
import ro.gnd.solutions.carebear.activities.MainActivity;
import ro.gnd.solutions.carebear.activities.settings.ChangePasswordActivity;
import ro.gnd.solutions.carebear.activities.settings.EditProfileActivity;
import ro.gnd.solutions.carebear.activities.settings.NotificationPreferencesActivity;
import ro.gnd.solutions.carebear.models.User;
import ro.gnd.solutions.carebear.services.UserService;

public class ProfileFragment extends Fragment {

    private final UserService userService = UserService.Companion.getInstance();
    private FirebaseAuth mAuth;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ImageView profilePicture;
    private TextView tvUsername, tvBio;
    private EditText etMedicalConditions, etMedications, etAllergies;
    private Button btnEditProfile, btnChangePassword, btnNotificationPreferences, btnLogout, submitHealthInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        // Bind views
        profilePicture = view.findViewById(R.id.profile_picture);
        tvUsername = view.findViewById(R.id.tv_username);
        tvBio = view.findViewById(R.id.tv_bio);
        etMedicalConditions = view.findViewById(R.id.et_medical_conditions);
        etMedications = view.findViewById(R.id.et_medications);
        etAllergies = view.findViewById(R.id.et_allergies);
//        rgVisibility = view.findViewById(R.id.rg_visibility);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnNotificationPreferences = view.findViewById(R.id.btn_notification_preferences);
        submitHealthInfo = view.findViewById(R.id.btn_submit_health_information);
//        btnThemeOptions = view.findViewById(R.id.btn_theme_options);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Load user data
        loadUserData();

        // Set button listeners
        btnEditProfile.setOnClickListener(v -> editProfile());
        btnChangePassword.setOnClickListener(v -> changePassword());
        btnNotificationPreferences.setOnClickListener(v -> openNotificationPreferences());
        submitHealthInfo.setOnClickListener(v -> submitHealthInformation());
//        btnThemeOptions.setOnClickListener(v -> openThemeOptions());
        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserData() {
        tvUsername.setText("");
        tvBio.setText("");
        userService.getLoggedUserReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Convert the snapshot to a User object
                    User loggedUser = snapshot.getValue(User.class);

                    if (loggedUser != null) {
                        // Update UI with the logged user's data
                        tvUsername.setText(loggedUser.getName());
                        tvBio.setText(loggedUser.getBio());
                        etMedicalConditions.setText(loggedUser.getMedicalConditions());
                        etMedications.setText(loggedUser.getMedications());
                        etAllergies.setText(loggedUser.getAllergies());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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

    private void submitHealthInformation() {
        userService.getLoggedUser(loggedUser -> {
            String medicalConditions = etMedicalConditions.getText().toString();
            String medications = etMedications.getText().toString();
            String allergies = etAllergies.getText().toString();

            loggedUser.setMedicalConditions(medicalConditions);
            loggedUser.setMedications(medications);
            loggedUser.setAllergies(allergies);

            userService.persistUser(loggedUser);

            return null;
        });

        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
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
