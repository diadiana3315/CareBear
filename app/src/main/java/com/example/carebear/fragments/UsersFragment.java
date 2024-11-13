package com.example.carebear.fragments;

import static com.google.firebase.database.DatabaseKt.getDatabase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carebear.R;
import com.example.carebear.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lombok.var;

public class UsersFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_users, container, false);

        initDatabase();

        return rootView;
    }

    private void initDatabase() {
        DatabaseReference usersRef = database.getReference("users");
        User user = new User("user", "user@gmail.com");
        usersRef.child("roFTP9rryVSk85bshtlJn4AhuS73").setValue(user);


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                System.out.println("Failed to read value: " + databaseError.toException());
            }
        });
    }
}
