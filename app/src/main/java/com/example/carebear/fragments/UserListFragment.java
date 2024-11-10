package com.example.carebear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.adapters.UserListAdapter;
import com.example.carebear.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private List<User> users;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list of users (replace this with data from your backend)
        users = new ArrayList<>();
        users.add(new User("John Doe", "john_doe@example.com"));
        users.add(new User("Jane Smith", "jane_smith@example.com"));
        users.add(new User("Mike Johnson", "mike_johnson@example.com"));

        // Set the adapter for RecyclerView
        userListAdapter = new UserListAdapter(users, position -> {
            // Handle user selection and start a new chat
            User selectedUser = users.get(position);
            startNewChat(selectedUser);
        });
        recyclerView.setAdapter(userListAdapter);

        return rootView;
    }

    private void startNewChat(User user) {
        // Logic to start a new chat with the selected user
        ChatsFragment chatsFragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString("chat_name", user.getName());
        args.putString("user_email", user.getEmail());
        chatsFragment.setArguments(args);

        // Perform the fragment transaction to open the chat
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, chatsFragment)
                .addToBackStack(null) // Allows the user to go back to the UserListFragment
                .commit();
    }
}
