package com.example.carebear.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.activities.groups.CreateGroupChatActivity;
import com.example.carebear.adapters.GroupsAdapter;
import com.example.carebear.models.GroupChat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiseaseGroupsFragment extends Fragment {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

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

        initGroups();

        groupsAdapter = new GroupsAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(groupsAdapter);

        return rootView;
    }

    private void initGroups() {
        database.getReference("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<GroupChat> groupChats = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot groupSnapshot : snapshot.getChildren()) {
                        GroupChat groupChat = groupSnapshot.getValue(GroupChat.class);
                        if (groupChat != null) {
                            groupChats.add(groupChat);
                        }
                    }
                }

                groupsAdapter = new GroupsAdapter(getContext(), groupChats);
                recyclerView.setAdapter(groupsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FetchGroupChats", "Error fetching group chats: " + error.getMessage());
            }
        });
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
}
