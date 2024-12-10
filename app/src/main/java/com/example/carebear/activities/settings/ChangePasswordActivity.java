package com.example.carebear.activities.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carebear.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmNewPassword;
    private Button btnChangePassword;
    private TextView tvChangePasswordMessage;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmNewPassword = findViewById(R.id.et_confirm_new_password);
        btnChangePassword = findViewById(R.id.btn_change_password);
        tvChangePasswordMessage = findViewById(R.id.tv_change_password_message);

        // Set the button click listener
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(currentPassword)) {
            showMessage("Please enter your current password.");
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            showMessage("Please enter a new password.");
            return;
        }
        if (TextUtils.isEmpty(confirmNewPassword)) {
            showMessage("Please confirm your new password.");
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            showMessage("New passwords do not match.");
            return;
        }

        // Get the currently authenticated user
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Reauthenticate the user with their email and current password
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            // Reauthenticate the user
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Successfully reauthenticated, now update the password
                            updatePassword(newPassword);
                        } else {
                            showMessage("Current password is incorrect.");
                        }
                    })
                    .addOnFailureListener(e -> showMessage("Reauthentication failed: " + e.getMessage()));
        } else {
            showMessage("User not authenticated.");
        }
    }

    private void updatePassword(String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Update the password
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showMessage("Password changed successfully!");
                        } else {
                            showMessage("Failed to change password.");
                        }
                    })
                    .addOnFailureListener(e -> showMessage("Error: " + e.getMessage()));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        tvChangePasswordMessage.setText(message);
        tvChangePasswordMessage.setVisibility(View.VISIBLE);
    }
}
