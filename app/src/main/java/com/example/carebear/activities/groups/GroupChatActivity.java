//package com.example.carebear.activities.groups;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.carebear.R;
//import com.example.carebear.adapters.ChatGroupsAdapter;
//import com.example.carebear.models.MessageGroups;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GroupChatActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private EditText editTextMessage;
//    private Button buttonSend;
//    private List<MessageGroups> messages = new ArrayList<>();
//    private ChatGroupsAdapter chatAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group_chat);
//
//        // Retrieve the group name from the intent
//        String groupName = getIntent().getStringExtra("GROUP_NAME");
//        setTitle(groupName);
//
//        // Initialize RecyclerView
//        recyclerView = findViewById(R.id.recycler_view_chat);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize adapter and set it to RecyclerView
//        chatAdapter = new ChatGroupsAdapter(messages);
//        recyclerView.setAdapter(chatAdapter);
//
//        // Initialize views
//        editTextMessage = findViewById(R.id.edit_text_message);
//        buttonSend = findViewById(R.id.btn_send_message);
//
//        // Set up send button functionality
//        buttonSend.setOnClickListener(v -> {
//            String messageText = editTextMessage.getText().toString();
//            sendMessage(messageText); // Call sendMessage() method with the input message
//            editTextMessage.getText().clear(); // Clear the input field after sending the message
//        });
//    }
//
//    private void sendMessage(String text) {
//        if (!text.trim().isEmpty()) {
//            // Create a new message and add it to the list
//            MessageGroups newMessage = new MessageGroups(text, true); // "true" means it's a sent message
//            messages.add(newMessage); // Add the message to the list
//            chatAdapter.notifyItemInserted(messages.size() - 1); // Notify adapter of new item
//            recyclerView.scrollToPosition(messages.size() - 1); // Scroll to the bottom of the list
//        }
//    }
//}
package com.example.carebear.activities.groups;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carebear.R;
import com.example.carebear.adapters.ChatGroupsAdapter;
import com.example.carebear.models.MessageGroups;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

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

        // Retrieve the group name from the intent
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        setTitle(groupName);

        // Initialize Firebase reference for the group chat
        groupChatRef = FirebaseDatabase.getInstance().getReference("GroupChats").child(groupName);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter and set it to RecyclerView
        chatAdapter = new ChatGroupsAdapter(messages);
        recyclerView.setAdapter(chatAdapter);

        // Initialize views
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.btn_send_message);

        // Load existing messages
        loadMessages();

        // Set up send button functionality
        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString();
            sendMessage(messageText);
            editTextMessage.getText().clear();
        });
    }

    private void loadMessages() {
        groupChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MessageGroups message = data.getValue(MessageGroups.class);
                    messages.add(message);
                }
                chatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void sendMessage(String text) {
        if (!text.trim().isEmpty()) {
            MessageGroups newMessage = new MessageGroups(text, true); // Sent message
            groupChatRef.push().setValue(newMessage); // Push to Firebase
        }
    }
}
