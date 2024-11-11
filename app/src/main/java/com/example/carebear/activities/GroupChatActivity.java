package com.example.carebear.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carebear.R;
import java.util.ArrayList;

public class GroupChatActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editTextMessage;
    private Button buttonSend;
    private ArrayList<String> chatMessages;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Retrieve the group name from the intent
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        setTitle(groupName);

        listView = findViewById(R.id.list_view_chat);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);

        chatMessages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatMessages);
        listView.setAdapter(adapter);

        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString();
            if (!message.isEmpty()) {
                chatMessages.add(message);
                adapter.notifyDataSetChanged();
                editTextMessage.setText("");
            }
        });
    }
}
