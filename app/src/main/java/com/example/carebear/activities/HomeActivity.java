package com.example.carebear.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.carebear.R;
import com.example.carebear.fragments.ChatsListFragment;
import com.example.carebear.fragments.DiseaseGroupsFragment;
import com.example.carebear.fragments.HomeFragment;
import com.example.carebear.fragments.ProfileFragment;
import com.example.carebear.fragments.FriendsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        // Handle navigation item selections using if-else statements
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_groups) {
                selectedFragment = new DiseaseGroupsFragment();
            } else if (item.getItemId() == R.id.nav_chats) {
                selectedFragment = new ChatsListFragment();
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
