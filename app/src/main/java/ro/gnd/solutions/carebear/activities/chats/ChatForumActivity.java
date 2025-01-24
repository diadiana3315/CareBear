package ro.gnd.solutions.carebear.activities.chats;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ro.gnd.solutions.carebear.R;

public class ChatForumActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private Button btnAttachMedia, btnSendMessage;
    private EditText editTextMessage;

    private static final int PICK_MEDIA_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_forum);

        recyclerViewChat = findViewById(R.id.recycler_view_chat);
        btnAttachMedia = findViewById(R.id.btn_attach_media);
        btnSendMessage = findViewById(R.id.btn_send_message);
        editTextMessage = findViewById(R.id.edit_text_message);

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Set up your adapter for chat messages here

        // Attach Media Button Click Listener
        btnAttachMedia.setOnClickListener(v -> openMediaPicker());

        // Send Message Button Click Listener
        btnSendMessage.setOnClickListener(v -> sendMessage());
    }

    private void openMediaPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startActivityForResult(intent, PICK_MEDIA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MEDIA_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedMediaUri = data.getData();
            if (selectedMediaUri != null) {
                // TODO: Handle the media URI (e.g., send it to the chat or display it in the chat)
            }
        }
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            // TODO: Add the message to your chat (e.g., send it to the backend or update the UI)
            editTextMessage.setText(""); // Clear the input field
        }
    }
}
