package com.example.carebear.models;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import lombok.Getter;

@Getter
public class ChatMessage {

    private String text;
    private String senderId;
    private long timestamp;

    public ChatMessage(String text, String senderId, long timestamp) {
        this.text = text;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public ChatMessage() {}

    // This method checks if the message was sent by the current user
    public boolean isSent() {
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        return senderId.equalsIgnoreCase(currentUserId);
    }
}
