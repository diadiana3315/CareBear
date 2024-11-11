package com.example.carebear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.adapters.AnnouncementAdapter;
import com.example.carebear.models.Announcement;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcements;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_announcements);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list of announcements (this can be fetched from an API or database)
        announcements = new ArrayList<>();
        announcements.add(new Announcement("Q&A Session", "Join us for a live Q&A session on Android Development.", "2024-11-10 18:00"));
        announcements.add(new Announcement("Article: How to Build an App", "Check out this article on how to build a mobile app with Kotlin.", "2024-11-05"));
        announcements.add(new Announcement("Maintenance Notice", "Scheduled maintenance on November 12th. Please be aware.", "2024-11-02"));

        // Set the adapter for RecyclerView
        announcementAdapter = new AnnouncementAdapter(announcements);
        recyclerView.setAdapter(announcementAdapter);

        return rootView;
    }
}
