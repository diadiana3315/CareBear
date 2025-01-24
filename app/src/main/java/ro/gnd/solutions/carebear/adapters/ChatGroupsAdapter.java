package ro.gnd.solutions.carebear.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ro.gnd.solutions.carebear.R;
import ro.gnd.solutions.carebear.models.MessageGroups;

public class ChatGroupsAdapter extends RecyclerView.Adapter<ChatGroupsAdapter.ChatViewHolder> {
    private List<MessageGroups> messages;
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public ChatGroupsAdapter(List<MessageGroups> messages) {
        this.messages = messages;
    }


    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        MessageGroups message = messages.get(position);
        holder.messageTextView.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_view_sent_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageGroups message = messages.get(position);
        return message.isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sent_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_received_message, parent, false);
        }
        return new ChatViewHolder(view);
    }

}
