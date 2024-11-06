package com.example.carebear.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carebear.R;
import com.example.carebear.activities.ChatForumActivity;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private List<String> groups;
    private Context context;

    public GroupsAdapter(Context context, List<String> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        String groupName = groups.get(position);
        holder.groupName.setText(groupName);

        // Set click listener for the whole item to navigate to ChatForumActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatForumActivity.class);
            intent.putExtra("groupName", groupName); // Pass group name to the chat forum
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public ImageView groupIcon;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.text_view_group_name);
            groupIcon = itemView.findViewById(R.id.image_view_group_icon);
        }
    }
}
