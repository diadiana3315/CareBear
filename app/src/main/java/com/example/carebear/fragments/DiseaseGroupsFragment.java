package com.example.carebear.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.carebear.R;
import com.example.carebear.activities.chats.StartNewChatActivity;
import com.example.carebear.activities.groups.CreateGroupChatActivity;
import com.example.carebear.adapters.GroupsAdapter;
import java.util.ArrayList;
import java.util.List;

public class DiseaseGroupsFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private GroupsAdapter groupsAdapter;

    public DiseaseGroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_disease_groups, container, false);

        initCreateNewGroupChatButtonClick();

        recyclerView = rootView.findViewById(R.id.recycler_view_disease_groups);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample groups for the list
        List<String> groups = getSampleGroups();
        groupsAdapter = new GroupsAdapter(getContext(), groups);
        recyclerView.setAdapter(groupsAdapter);

        return rootView;
    }

    private void initCreateNewGroupChatButtonClick() {
        View startNewChatButton = rootView.findViewById(R.id.fab_add_group);
        startNewChatButton.setOnClickListener(viewOnClickListener -> {
            Intent intent = new Intent(getContext(), CreateGroupChatActivity.class);
            if (getContext() != null) {
                getContext().startActivity(intent);
            }
        });
    }

    private List<String> getSampleGroups() {
        List<String> groups = new ArrayList<>();
        groups.add("Diabetes Support");
        groups.add("Hypertension Management");
        groups.add("Arthritis & Joint Pain");
        groups.add("Cardiac Health");
        return groups;
    }
}
