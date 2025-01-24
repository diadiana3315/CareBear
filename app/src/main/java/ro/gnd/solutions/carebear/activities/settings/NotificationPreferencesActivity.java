package ro.gnd.solutions.carebear.activities.settings;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import lombok.var;
import ro.gnd.solutions.carebear.R;
import ro.gnd.solutions.carebear.models.User;
import ro.gnd.solutions.carebear.services.UserService;

public class NotificationPreferencesActivity extends AppCompatActivity {

    private final UserService userService = UserService.Companion.getInstance();
    private Switch switchNotifications;
    private TextView tvAllowNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_preferences);

        // Initialize views
        switchNotifications = findViewById(R.id.switch_notifications);
        tvAllowNotifications = findViewById(R.id.tv_allow_notifications);

        var backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(l -> finish());


        // Set listener for the Switch
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationPreferences(isChecked);
        });

        // Optionally, set the initial state of the Switch based on saved preferences
        // For example, use SharedPreferences to persist the state
        loadNotificationPreferences();
    }

    private void loadNotificationPreferences() {
        userService.getLoggedUserReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Convert the snapshot to a User object
                    User loggedUser = snapshot.getValue(User.class);

                    if (loggedUser != null) {
                        // Update UI with the logged user's data
                        switchNotifications.setChecked(Boolean.TRUE.equals(loggedUser.getAreNotificationsEnabled()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void saveNotificationPreferences(Boolean isChecked) {
        userService.getLoggedUser(loggedUser -> {
            loggedUser.setAreNotificationsEnabled(isChecked);

            userService.persistUser(loggedUser);

            return null;
        });

        Toast.makeText(this, "Notification preferences updated", Toast.LENGTH_SHORT).show();

        boolean isNotificationsEnabled = switchNotifications.isChecked();
        getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("notifications_enabled", isNotificationsEnabled)
                .apply();
    }
}
