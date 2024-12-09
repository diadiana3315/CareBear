//package com.example.carebear.models;
//
//public class MessageGroups {
//    private String text;
//    private boolean isSent;
//
//    public MessageGroups(String text, boolean isSent) {
//        this.text = text;
//        this.isSent = isSent;
//    }
//
//
//    public String getText() {
//        return text;
//    }
//
//    public boolean isSent() {
//        return isSent;
//    }
//}
package com.example.carebear.models;

public class MessageGroups {
    private String text;
    private boolean isSent;

    // Required empty constructor for Firebase
    public MessageGroups() {
    }

    public MessageGroups(String text, boolean isSent) {
        this.text = text;
        this.isSent = isSent;
    }

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
}
