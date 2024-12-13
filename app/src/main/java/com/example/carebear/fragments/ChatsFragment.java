package com.example.carebear.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.activities.chats.StartNewChatActivity;
import com.example.carebear.adapters.ChatAdapter;
import com.example.carebear.models.ChatMembership;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.var;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageButton btnSend;

    private View rootView;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final String loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    private String chatId; // Unique ID for the chat

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chats, container, false);

        initChatIds();

        initStartNewChatButtonClick();

        return rootView;
    }

    private void initStartNewChatButtonClick() {
        var startNewChatButton = rootView.findViewById(R.id.btn_start_new_chat);
        startNewChatButton.setOnClickListener(viewOnClickListener -> {
            Intent intent = new Intent(getContext(), StartNewChatActivity.class);
            if (getContext() != null) {
                getContext().startActivity(intent);
            }
        });
    }

    private void initChatIds() {
        final List<ChatMembership> chatMemberships = new ArrayList<>();

        final DatabaseReference userChatsRef = database.getReference("users").child(loggedUserId).child("chats");
        userChatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMemberships.clear();
                snapshot.getChildren().forEach(chatSnapshot  -> {
                    ChatMembership chatMembership = chatSnapshot.getValue(ChatMembership.class);
                    if (chatMembership != null) {
                        chatMemberships.add(chatMembership);
                    }
                });

                displayChats(chatMemberships);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value: " + error.toException());
            }
        });
    }

    private void displayChats(List<ChatMembership> chatMemberships) {
        final RecyclerView chatsView = rootView.findViewById(R.id.chats_recycler_view);
        chatsView.setLayoutManager(new LinearLayoutManager(requireContext()));
        final ChatAdapter chatAdapter = new ChatAdapter(this.requireContext(), chatMemberships);
        chatsView.setAdapter(chatAdapter);
    }

}
