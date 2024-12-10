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
import com.example.carebear.models.Chat;
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
        final List<Chat> chats = new ArrayList<>();

        final DatabaseReference userChatsRef = database.getReference("users").child(loggedUserId).child("chats");
        userChatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                snapshot.getChildren().forEach(chatSnapshot  -> {
                    Chat chat = chatSnapshot.getValue(Chat.class);
                    if (chat != null) {
                        chats.add(chat);
                    }
                });

                initChats(chats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value: " + error.toException());
            }
        });
    }

    private void initChats(List<Chat> chats) {
    }

    private void displayChats(List<Chat> chats) {
        final RecyclerView chatsView = rootView.findViewById(R.id.chats_recycler_view);
        chatsView.setLayoutManager(new LinearLayoutManager(requireContext()));
        final ChatAdapter chatAdapter = new ChatAdapter(this.requireContext(), chats);
        chatsView.setAdapter(chatAdapter);
    }

//    private void loadMessages() {
//        if (chatId == null) return;
//
//        chatsRef.child(chatId).child("messages").orderByChild("timestamp")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        List<ChatMessage> messages = new ArrayList<>();
//                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
//                            ChatMessage message = messageSnapshot.getValue(ChatMessage.class);
//                            if (message != null) {
//                                messages.add(message);
//                            }
//                        }
//                        chatAdapter.setMessages(messages); // Populate all messages
//                        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // Scroll to last message
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getContext(), "Failed to load messages.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void listenForMessages() {
//        if (chatId == null) return;
//
//        chatsRef.child(chatId).child("messages").orderByChild("timestamp")
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
//                        ChatMessage message = snapshot.getValue(ChatMessage.class);
//                        if (message != null) {
//                            chatAdapter.addMessage(message); // Append message
//                            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {}
//                });
//    }
//
//    private void sendMessage(String messageText) {
//        ChatMessage newMessage = new ChatMessage(messageText, auth.getCurrentUser().getUid(), System.currentTimeMillis());
//
//        DatabaseReference newMessageRef = chatsRef.child(chatId).child("messages").push();
//        newMessageRef.setValue(newMessage).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // Optionally handle success (e.g., update the UI, show a toast, etc.)
//            } else {
//                Toast.makeText(getContext(), "Message failed to send", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
