package com.example.carebear.services;

import android.content.Context;
import android.util.Log;
import com.example.carebear.models.MessageGroups;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class GroupChatService {

    private static GroupChatService instance;
    private final DatabaseReference databaseRef;

    private GroupChatService() {
        databaseRef = FirebaseDatabase.getInstance().getReference("GroupChats");
    }

    public static synchronized GroupChatService getInstance() {
        if (instance == null) {
            instance = new GroupChatService();
        }
        return instance;
    }

    public void fetchMessages(String groupName, OnMessagesFetchedListener listener) {
        databaseRef.child(groupName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<MessageGroups> messages = new ArrayList<>();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    MessageGroups message = snapshot.getValue(MessageGroups.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                listener.onMessagesFetched(messages);  // Pass the messages to the listener
                Log.d("GroupChatService", "Messages fetched successfully");
            } else {
                Log.e("GroupChatService", "Failed to fetch messages", task.getException());
            }
        });
    }

    public void sendMessage(Context context, String groupName, String senderId, String text) {
        MessageGroups message = new MessageGroups(senderId, text, System.currentTimeMillis());
        databaseRef.child(groupName).push().setValue(message)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("GroupChatService", "Failed to send message", task.getException());
                    } else {
                        Log.d("GroupChatService", "Message sent successfully");
                    }
                });
    }



    public interface OnMessagesFetchedListener {
        void onMessagesFetched(List<MessageGroups> messages);
    }
}
