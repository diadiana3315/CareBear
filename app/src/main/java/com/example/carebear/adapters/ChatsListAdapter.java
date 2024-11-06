package com.example.carebear.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.models.Chat;

import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ChatViewHolder> {

    private List<Chat> chats;
    private OnItemClickListener onItemClickListener;

    public ChatsListAdapter(List<Chat> chats, OnItemClickListener onItemClickListener) {
        this.chats = chats;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.nameTextView.setText(chat.getName());
        holder.lastMessageTextView.setText(chat.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, lastMessageTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_chat_name);
            lastMessageTextView = itemView.findViewById(R.id.tv_last_message);
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition()));
        }
    }
}
