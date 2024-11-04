package com.example.carebear.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.carebear.R;
import com.example.carebear.adapters.GroupsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiseaseGroupsFragment extends Fragment {

    private RecyclerView recyclerView;
    private GroupsAdapter groupsAdapter;

    public DiseaseGroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disease_groups, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_disease_groups);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up adapter with sample data
        groupsAdapter = new GroupsAdapter(getSampleGroups());
        recyclerView.setAdapter(groupsAdapter);

        return view;
    }

    // Method to get sample data
    private List<String> getSampleGroups() {
        List<String> groups = new ArrayList<>();
        groups.add("Diabetes Support");
        groups.add("Hypertension Management");
        groups.add("Arthritis & Joint Pain");
        groups.add("Cardiac Health");
        return groups;
    }
}
