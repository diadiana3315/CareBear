package com.example.carebear.models;

public class Chat {
    private String name;
    private String lastMessage;
    private long timestamp;

    // Constructor
    public Chat(String name, String lastMessage, long timestamp) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
