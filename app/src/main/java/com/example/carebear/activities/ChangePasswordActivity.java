package com.example.carebear.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carebear.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmNewPassword;
    private Button btnChangePassword;
    private TextView tvChangePasswordMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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

        // TODO: Add code to verify current password and change to new password
        // This is where you would typically call a backend service to change the password.

        // For now, we can just show a success message
        showMessage("Password changed successfully!");

        // Clear input fields
        etCurrentPassword.setText("");
        etNewPassword.setText("");
        etConfirmNewPassword.setText("");
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        tvChangePasswordMessage.setText(message);
        tvChangePasswordMessage.setVisibility(View.VISIBLE);
    }
}
