////package com.example.carebear.activities.groups;
////
////import android.os.Bundle;
////import android.widget.Button;
////import android.widget.EditText;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////import com.example.carebear.R;
////import com.example.carebear.adapters.ChatGroupsAdapter;
////import com.example.carebear.models.MessageGroups;
////import java.util.ArrayList;
////import java.util.List;
////
////public class GroupChatActivity extends AppCompatActivity {
////
////    private RecyclerView recyclerView;
////    private EditText editTextMessage;
////    private Button buttonSend;
////    private List<MessageGroups> messages = new ArrayList<>();
////    private ChatGroupsAdapter chatAdapter;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_group_chat);
////
////        // Retrieve the group name from the intent
////        String groupName = getIntent().getStringExtra("GROUP_NAME");
////        setTitle(groupName);
////
////        // Initialize RecyclerView
////        recyclerView = findViewById(R.id.recycler_view_chat);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////
////        // Initialize adapter and set it to RecyclerView
////        chatAdapter = new ChatGroupsAdapter(messages);
////        recyclerView.setAdapter(chatAdapter);
////
////        // Initialize views
////        editTextMessage = findViewById(R.id.edit_text_message);
////        buttonSend = findViewById(R.id.btn_send_message);
////
////        // Set up send button functionality
////        buttonSend.setOnClickListener(v -> {
////            String messageText = editTextMessage.getText().toString();
////            sendMessage(messageText); // Call sendMessage() method with the input message
////            editTextMessage.getText().clear(); // Clear the input field after sending the message
////        });
////    }
////
////    private void sendMessage(String text) {
////        if (!text.trim().isEmpty()) {
////            // Create a new message and add it to the list
////            MessageGroups newMessage = new MessageGroups(text, true); // "true" means it's a sent message
////            messages.add(newMessage); // Add the message to the list
////            chatAdapter.notifyItemInserted(messages.size() - 1); // Notify adapter of new item
////            recyclerView.scrollToPosition(messages.size() - 1); // Scroll to the bottom of the list
////        }
////    }
////}
package com.example.carebear.activities.groups;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.adapters.ChatGroupsAdapter;
import com.example.carebear.models.MessageGroups;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.carebear.services.GroupChatService;

import java.util.ArrayList;
import java.util.List;
//
//public class GroupChatActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private EditText editTextMessage;
//    private Button buttonSend;
//    private List<MessageGroups> messages = new ArrayList<>();
//    private ChatGroupsAdapter chatAdapter;
//    private DatabaseReference groupChatRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group_chat);
//
//        // Initialize views
//        editTextMessage = findViewById(R.id.edit_text_message);
//        buttonSend = findViewById(R.id.btn_send_message);
//
//        // Retrieve group name
//        String groupName = getIntent().getStringExtra("GROUP_NAME");
//        setTitle(groupName);
//
//        // Initialize Firebase reference for the group chat
//        groupChatRef = FirebaseDatabase.getInstance().getReference("GroupChats").child(groupName);
//
//        // Initialize RecyclerView
//        recyclerView = findViewById(R.id.recycler_view_chat);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        chatAdapter = new ChatGroupsAdapter(messages);
//        recyclerView.setAdapter(chatAdapter);
//
//        // Fetch existing messages
//        loadMessages(groupName);  // Call this method to load the chat messages
//
//        // Handle send button click
//        buttonSend.setOnClickListener(v -> {
//            String messageText = editTextMessage.getText().toString();
//            if (!messageText.trim().isEmpty()) {
//                String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Get current user ID
//                // Call GroupChatService to send the message
//                GroupChatService.getInstance().sendMessage(this, groupName, senderId, messageText);
//                // Clear the input field after sending the message
//                editTextMessage.getText().clear();
//            }
//        });
//    }
//
//    // Load messages from Firebase
//    private void loadMessages(String groupName) {
//        GroupChatService.getInstance().fetchMessages(groupName, fetchedMessages -> {
//            if (fetchedMessages != null) {
//                messages.clear();  // Clear old messages
//                messages.addAll(fetchedMessages);  // Add the fetched messages to the list
//                chatAdapter.notifyDataSetChanged();  // Notify the adapter that data has changed
//                recyclerView.scrollToPosition(messages.size() - 1);  // Scroll to the latest message
//            }
//        });
//    }
//
//}
public class GroupChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private List<MessageGroups> messages = new ArrayList<>();
    private ChatGroupsAdapter chatAdapter;
    private DatabaseReference groupChatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Initialize views
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.btn_send_message);

        // Retrieve group name
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        setTitle(groupName);

        // Initialize Firebase reference for the group chat
        groupChatRef = FirebaseDatabase.getInstance().getReference("GroupChats").child(groupName);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatGroupsAdapter(messages);
        recyclerView.setAdapter(chatAdapter);

        // Fetch existing messages
        loadMessages(groupName);  // Call this method to load the chat messages

        // Handle send button click
        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString();
            if (!messageText.trim().isEmpty()) {
                String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Get current user ID
                long timestamp = System.currentTimeMillis();  // Timestamp for the message as long

                // Create a MessageGroups object with the necessary details
                MessageGroups message = new MessageGroups(senderId, messageText, timestamp);

                // Push the message to Firebase under the group
                groupChatRef.push().setValue(message)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("GroupChatActivity", "Message sent successfully");
                                editTextMessage.getText().clear();  // Clear the input field after sending the message
                            } else {
                                Log.e("GroupChatActivity", "Failed to send message", task.getException());
                            }
                        });
            }
        });

    }

    // Load messages from Firebase
    private void loadMessages(String groupName) {
        groupChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageGroups message = dataSnapshot.getValue(MessageGroups.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                chatAdapter.notifyDataSetChanged();  // Notify the adapter that data has changed
                recyclerView.scrollToPosition(messages.size() - 1);  // Scroll to the latest message
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("GroupChatActivity", "Failed to load messages", error.toException());
            }
        });
    }
}
