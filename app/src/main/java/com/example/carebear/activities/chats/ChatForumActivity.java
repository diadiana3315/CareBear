package com.example.carebear.activities.chats;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carebear.R;

public class ChatForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_forum);

        // Retrieve group name passed from the adapter
        String groupName = getIntent().getStringExtra("groupName");

        // Display group name in the toolbar or text view
        TextView textViewGroupName = findViewById(R.id.text_view_group_name);
        textViewGroupName.setText(groupName);
    }
}
