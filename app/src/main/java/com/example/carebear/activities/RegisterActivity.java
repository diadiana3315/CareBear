package com.example.carebear.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carebear.R;
import com.example.carebear.network.ApiClient;
import com.example.carebear.network.models.ApiResponse;
import com.example.carebear.network.models.RegisterRequest;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_register_username);
        etEmail = findViewById(R.id.et_register_email);
        etPassword = findViewById(R.id.et_register_password);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(RegisterActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterRequest request = new RegisterRequest(username, email, password);
            ApiClient.getApiService().register(request).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        etUsername.setText("");
                        etEmail.setText("");
                        etPassword.setText("");
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = response.body() != null ? response.body().getMessage() : "Registration failed";
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }
}
