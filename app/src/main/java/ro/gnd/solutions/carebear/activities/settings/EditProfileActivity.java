package ro.gnd.solutions.carebear.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ro.gnd.solutions.carebear.R;
import ro.gnd.solutions.carebear.services.UserService;

public class EditProfileActivity extends AppCompatActivity {

    private final UserService userService = UserService.Companion.getInstance();
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
        etUsername.setText("SampleUsername");
        etBio.setText("This is a sample bio.");

        userService.getLoggedUser(loggedUser -> {
            etUsername.setText(loggedUser.getName());
            etBio.setText(loggedUser.getBio());

            return null;
        });
    }

    private void changeProfilePicture() {
        Toast.makeText(this, "Change profile picture clicked", Toast.LENGTH_SHORT).show();
    }

    private void saveProfileChanges() {
        String newUsername = etUsername.getText().toString();
        String newBio = etBio.getText().toString();

        userService.getLoggedUser(loggedUser -> {
            etUsername.setText(newUsername);
            etBio.setText(newBio);
            loggedUser.setUsername(newUsername);
            loggedUser.setBio(newBio);

            userService.persistUser(loggedUser);

            return null;
        });

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
