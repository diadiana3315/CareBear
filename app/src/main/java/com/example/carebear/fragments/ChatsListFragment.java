package com.example.carebear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.adapters.ChatsListAdapter;
import com.example.carebear.models.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatsListAdapter chatsListAdapter;
    private List<Chat> chats;

    public ChatsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chats_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list of chats (you can replace this with a backend call)
        chats = new ArrayList<>();
        chats.add(new Chat("John Doe", "Hey! How are you?", System.currentTimeMillis()));
        chats.add(new Chat("Jane Smith", "Can we meet later?", System.currentTimeMillis()));
        chats.add(new Chat("Mike Johnson", "Let's catch up soon.", System.currentTimeMillis()));

        // Set the adapter for RecyclerView
        chatsListAdapter = new ChatsListAdapter(chats, position -> {
            // Navigate to the chat screen when a chat item is clicked
            Chat selectedChat = chats.get(position);
            openChatFragment(selectedChat);
        });
        recyclerView.setAdapter(chatsListAdapter);

        return rootView;
    }

    private void openChatFragment(Chat chat) {
        // Create a new ChatsFragment and pass the selected chat information
        ChatsFragment chatsFragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString("chat_name", chat.getName());
        args.putString("last_message", chat.getLastMessage());
        args.putLong("timestamp", chat.getTimestamp());
        chatsFragment.setArguments(args);

        // Perform the fragment transaction to display the chat fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, chatsFragment) // Replacing fragment in content_frame
                .addToBackStack(null) // Allows the user to go back to the previous fragment
                .commit();
    }

}
