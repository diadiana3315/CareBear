//////package com.example.carebear.models;
//////
//////public class MessageGroups {
//////    private String text;
//////    private boolean isSent;
//////
//////    public MessageGroups(String text, boolean isSent) {
//////        this.text = text;
//////        this.isSent = isSent;
//////    }
//////
//////
//////    public String getText() {
//////        return text;
//////    }
//////
//////    public boolean isSent() {
//////        return isSent;
//////    }
//////}
////package com.example.carebear.models;
////
////public class MessageGroups {
////    private String text;
////    private boolean isSent;
////
////    // Required empty constructor for Firebase
////    public MessageGroups() {
////    }
////
////    public MessageGroups(String text, boolean isSent) {
////        this.text = text;
////        this.isSent = isSent;
////    }
////
////    public String getText() {
////        return text;
////    }
////
////    public void setText(String text) {
////        this.text = text;
////    }
////
////    public boolean isSent() {
////        return isSent;
////    }
////
////    public void setSent(boolean sent) {
////        isSent = sent;
////    }
////}
//package com.example.carebear.models;
//
//public class MessageGroups {
//    private String senderId;
//    private String text;
//    private long timestamp;
//
//    public MessageGroups() {}
//
//    public MessageGroups(String senderId, String text, long timestamp) {
//        this.senderId = senderId;
//        this.text = text;
//        this.timestamp = timestamp;
//    }
//
//    public String getSenderId() {
//        return senderId;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//}
package com.example.carebear.models;

import com.google.firebase.auth.FirebaseAuth;

public class MessageGroups {
    private String senderId;
    private String text;
    private long timestamp;

    public MessageGroups() {}

    public MessageGroups(String senderId, String text, long timestamp) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Add the isSent() method
    public boolean isSent() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return senderId != null && senderId.equals(currentUserId);
    }
}
