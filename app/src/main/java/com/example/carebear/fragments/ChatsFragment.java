package com.example.carebear.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.adapters.ChatAdapter;
import com.example.carebear.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private EditText etMessage;
    private ImageButton btnSend;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_messages);
        etMessage = rootView.findViewById(R.id.et_message);
        btnSend = rootView.findViewById(R.id.btn_send);

        // Initialize chat list
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set the send button click listener
        btnSend.setOnClickListener(v -> {
            String messageText = etMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                // Send message locally (simulating WebSocket)
                sendMessage(messageText);
                etMessage.setText(""); // Clear the input field
            }
        });

        // Connect mock WebSocket to simulate receiving messages
        connectMockWebSocket();

        return rootView;
    }

    private void connectMockWebSocket() {
        // Simulate a delay as if the message was received via WebSocket
        new Handler().postDelayed(() -> {
            ChatMessage mockReceivedMessage = new ChatMessage("Mock message from server", false, System.currentTimeMillis());
            chatAdapter.addMessage(mockReceivedMessage);
        }, 2000); // Simulating a 2-second delay for incoming message
    }

    private void sendMessage(String messageText) {
        // Simulate sending the message via WebSocket (the message is sent as a string)
        // Add the message to the chat (as sent by the user)
        ChatMessage chatMessage = new ChatMessage(messageText, true, System.currentTimeMillis());
        chatAdapter.addMessage(chatMessage);

        // Optionally simulate a delay for sending
        new Handler().postDelayed(() -> {
            ChatMessage mockSentMessage = new ChatMessage(messageText, true, System.currentTimeMillis());
            chatAdapter.addMessage(mockSentMessage);
        }, 1000);
    }
}
