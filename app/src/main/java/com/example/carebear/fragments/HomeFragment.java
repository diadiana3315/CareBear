package com.example.carebear.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.adapters.AnnouncementAdapter;
import com.example.carebear.models.Announcement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcements;
    private DatabaseReference databaseReference;

    private LinearLayout formLayout;
    private EditText titleInput, descriptionInput, dateInput;
    private Button addAnnouncementButton;
    private FloatingActionButton addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_announcements);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        formLayout = rootView.findViewById(R.id.form_layout);
        titleInput = rootView.findViewById(R.id.input_title);
        descriptionInput = rootView.findViewById(R.id.input_description);
        dateInput = rootView.findViewById(R.id.input_date);
        addAnnouncementButton = rootView.findViewById(R.id.btn_add_announcement);
        addButton = rootView.findViewById(R.id.btn_add_chat);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("announcements");

        // Initialize announcements list and adapter
        announcements = new ArrayList<>();
        announcementAdapter = new AnnouncementAdapter(announcements);
        recyclerView.setAdapter(announcementAdapter);

        // Fetch announcements from Firebase
        fetchAnnouncements();

        // Toggle form visibility
        addButton.setOnClickListener(v -> toggleFormVisibility());

        // Add a new announcement
        addAnnouncementButton.setOnClickListener(v -> addAnnouncement());

        return rootView;
    }

    private void fetchAnnouncements() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcements.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Announcement announcement = dataSnapshot.getValue(Announcement.class);
                    announcements.add(announcement);
                }
                announcementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch announcements", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAnnouncement() {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(date)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseReference.push().getKey();
        Announcement announcement = new Announcement(title, description, date);

        databaseReference.child(id).setValue(announcement).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Announcement added", Toast.LENGTH_SHORT).show();
            titleInput.setText("");
            descriptionInput.setText("");
            dateInput.setText("");
            toggleFormVisibility(); // Hide the form after submission
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add announcement", Toast.LENGTH_SHORT).show());
    }

    private void toggleFormVisibility() {
        if (formLayout.getVisibility() == View.GONE) {
            formLayout.setVisibility(View.VISIBLE);
        } else {
            formLayout.setVisibility(View.GONE);
        }
    }
}
