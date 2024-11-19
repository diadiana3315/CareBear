//package com.example.carebear.activities;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.carebear.R;
//import java.util.ArrayList;
//
//public class GroupChatActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private EditText editTextMessage;
//    private Button buttonSend;
//    private ArrayList<String> chatMessages;
//    private ArrayAdapter<String> adapter;
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
//        listView = findViewById(R.id.list_view_chat);
//        editTextMessage = findViewById(R.id.edit_text_message);
//        buttonSend = findViewById(R.id.button_send);
//
//        chatMessages = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatMessages);
//        listView.setAdapter(adapter);
//
//        buttonSend.setOnClickListener(v -> {
//            String message = editTextMessage.getText().toString();
//            if (!message.isEmpty()) {
//                chatMessages.add(message);
//                adapter.notifyDataSetChanged();
//                editTextMessage.setText("");
//            }
//        });
//    }
//}
//package com.example.carebear.activities;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.carebear.R;
//import java.util.ArrayList;
//
//public class GroupChatActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private EditText editTextMessage;
//    private Button buttonSend;
//    private ArrayList<String> chatMessages;
//    private ArrayAdapter<String> adapter;
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
//        listView = findViewById(R.id.list_view_chat);
//        editTextMessage = findViewById(R.id.edit_text_message);
//        buttonSend = findViewById(R.id.button_send);
//
//        chatMessages = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatMessages);
//        listView.setAdapter(adapter);
//
//        buttonSend.setOnClickListener(v -> {
//            String message = editTextMessage.getText().toString();
//            if (!message.isEmpty()) {
//                chatMessages.add(message);
//                adapter.notifyDataSetChanged();
//                editTextMessage.getText().clear();
//            }
//        });
//    }
//}
package com.example.carebear.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carebear.R;
import com.example.carebear.adapters.MessageAdapter;
import com.example.carebear.models.Message;
import java.util.ArrayList;

public class GroupChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private ArrayList<Message> chatMessages;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Retrieve the group name from the intent
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        setTitle(groupName);

        recyclerView = findViewById(R.id.recycler_view_chat);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);

        chatMessages = new ArrayList<>();
        adapter = new MessageAdapter(chatMessages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString();
            if (!messageText.isEmpty()) {
                // Add the message to the chatMessages list
                Message message = new Message("You", messageText);
                chatMessages.add(message);
                adapter.notifyItemInserted(chatMessages.size() - 1);
                recyclerView.scrollToPosition(chatMessages.size() - 1);
                editTextMessage.getText().clear();
            }
        });
    }
}

//
//package com.example.carebear.activities.groups;
//
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.carebear.R;
//import java.util.ArrayList;
//
//public class GroupChatActivity extends AppCompatActivity {
//
//    private EditText editTextMessage;
//    private ArrayList<String> chatMessages;
//    private ArrayAdapter<String> adapter;
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
//        ListView listView = findViewById(R.id.list_view_chat);
//        editTextMessage = findViewById(R.id.edit_text_message);
//        Button buttonSend = findViewById(R.id.button_send);
//
//        chatMessages = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatMessages);
//        listView.setAdapter(adapter);
//
//        buttonSend.setOnClickListener(v -> {
//            String message = editTextMessage.getText().toString();
//            if (!message.isEmpty()) {
//                chatMessages.add(message);
//                adapter.notifyDataSetChanged();
//                editTextMessage.setText("");
//            }
//        });
//    }
//}

