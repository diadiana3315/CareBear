package ro.gnd.solutions.carebear.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ro.gnd.solutions.carebear.R;
import ro.gnd.solutions.carebear.fragments.ChatsFragment;
import ro.gnd.solutions.carebear.fragments.DiseaseGroupsFragment;
import ro.gnd.solutions.carebear.fragments.FriendsFragment;
import ro.gnd.solutions.carebear.fragments.HomeFragment;
import ro.gnd.solutions.carebear.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvStreak = findViewById(R.id.tvStreak);

        tvStreak.setText("Your Streak: ");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load HomeFragment as the default fragment
        if (savedInstanceState == null) {
            try {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new HomeFragment())
                        .commit();
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
            } catch (Exception e) {
                Log.e(TAG, "Error loading HomeFragment", e);
            }
        }

        // Handle navigation item_friend.xml selections using if-else statements
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_groups) {
                selectedFragment = new DiseaseGroupsFragment();
            } else if (item.getItemId() == R.id.nav_chats) {
                selectedFragment = new ChatsFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.nav_friends) {
                selectedFragment = new FriendsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
