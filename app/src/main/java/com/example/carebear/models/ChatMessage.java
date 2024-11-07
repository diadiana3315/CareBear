package com.example.carebear.models;

public class ChatMessage {
    private String text;       // The text of the message
    private boolean isSent;    // True if the message was sent by the user, false if received
    private long timestamp;    // Optional: Timestamp for when the message was sent

    // Constructor
    public ChatMessage(String text, boolean isSent, long timestamp) {
        this.text = text;
        this.isSent = isSent;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
