package com.example.carebear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.adapters.ChatAdapter;
import com.example.carebear.models.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageButton btnSend;
    private ChatAdapter chatAdapter;

    private FirebaseAuth auth;
    private DatabaseReference chatsRef;

    private String chatId; // Unique ID for the chat

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);

        auth = FirebaseAuth.getInstance();
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");

        // Retrieve the chatId from arguments
        if (getArguments() != null) {
            chatId = getArguments().getString("chat_id");
        }

        // Initialize UI components
        recyclerView = rootView.findViewById(R.id.recycler_view_messages);
        etMessage = rootView.findViewById(R.id.et_message);
        btnSend = rootView.findViewById(R.id.btn_send);

        // Set up the RecyclerView
        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load existing messages
        loadMessages();

        // Start listening for new messages
        listenForMessages();

        // Send message when button is clicked
        btnSend.setOnClickListener(v -> {
            String messageText = etMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                etMessage.setText(""); // Clear the input field
            }
        });

        return rootView;
    }

    private void loadMessages() {
        if (chatId == null) return;

        chatsRef.child(chatId).child("messages").orderByChild("timestamp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ChatMessage> messages = new ArrayList<>();
                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                            ChatMessage message = messageSnapshot.getValue(ChatMessage.class);
                            if (message != null) {
                                messages.add(message);
                            }
                        }
                        chatAdapter.setMessages(messages); // Populate all messages
                        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // Scroll to last message
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to load messages.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void listenForMessages() {
        if (chatId == null) return;

        chatsRef.child(chatId).child("messages").orderByChild("timestamp")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                        ChatMessage message = snapshot.getValue(ChatMessage.class);
                        if (message != null) {
                            chatAdapter.addMessage(message); // Append message
                            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void sendMessage(String messageText) {
        ChatMessage newMessage = new ChatMessage(messageText, auth.getCurrentUser().getUid(), System.currentTimeMillis());

        DatabaseReference newMessageRef = chatsRef.child(chatId).child("messages").push();
        newMessageRef.setValue(newMessage).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Optionally handle success (e.g., update the UI, show a toast, etc.)
            } else {
                Toast.makeText(getContext(), "Message failed to send", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
