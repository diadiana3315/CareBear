package ro.gnd.solutions.carebear.activities.groups;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ro.gnd.solutions.carebear.R;
import ro.gnd.solutions.carebear.adapters.ChatGroupsAdapter;
import ro.gnd.solutions.carebear.models.MessageGroups;

public class GroupChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private List<MessageGroups> messages = new ArrayList<>();
    private ChatGroupsAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Retrieve the group name from the intent
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        setTitle(groupName);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter and set it to RecyclerView
        chatAdapter = new ChatGroupsAdapter(messages);
        recyclerView.setAdapter(chatAdapter);

        // Initialize views
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.btn_send_message);

        // Set up send button functionality
        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString();
            sendMessage(messageText); // Call sendMessage() method with the input message
            editTextMessage.getText().clear(); // Clear the input field after sending the message
        });
    }

    private void sendMessage(String text) {
        if (!text.trim().isEmpty()) {
            // Create a new message and add it to the list
            MessageGroups newMessage = new MessageGroups(text, true); // "true" means it's a sent message
            messages.add(newMessage); // Add the message to the list
            chatAdapter.notifyItemInserted(messages.size() - 1); // Notify adapter of new item
            recyclerView.scrollToPosition(messages.size() - 1); // Scroll to the bottom of the list
        }
    }
}
