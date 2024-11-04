package com.example.carebear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carebear.R;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageButton btnSend, btnAttachMedia;
    private ChatAdapter chatAdapter; // Adapter for handling messages

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_messages);
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.btn_send);
        btnAttachMedia = view.findViewById(R.id.btn_attach_media);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter();  // Adapter should be implemented separately to bind chat items
        recyclerView.setAdapter(chatAdapter);

        // Set up button actions
        btnSend.setOnClickListener(v -> sendMessage());
        btnAttachMedia.setOnClickListener(v -> attachMedia());

        return view;
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            // Add message to the adapter's dataset and notify RecyclerView
            chatAdapter.addMessage(new ChatMessage(message, true));  // Add "sent" message
            etMessage.setText("");
            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);  // Scroll to the latest message
        }
    }

    private void attachMedia() {
        // Open media picker or handle media upload here
    }
}
