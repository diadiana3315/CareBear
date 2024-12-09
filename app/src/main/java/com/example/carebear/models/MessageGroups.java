package com.example.carebear.models;

public class MessageGroups {
    private String text;
    private boolean isSent;

    public MessageGroups(String text, boolean isSent) {
        this.text = text;
        this.isSent = isSent;
    }


    public String getText() {
        return text;
    }

    public boolean isSent() {
        return isSent;
    }
}
