package com.example.carebear.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carebear.R;
import com.example.carebear.activities.GroupChatActivity;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private Context context;
    private List<String> groups;

    public GroupsAdapter(Context context, List<String> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        String groupName = groups.get(position);
        holder.groupName.setText(groupName);

        // Set click listener to open GroupChatActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupChatActivity.class);
            intent.putExtra("GROUP_NAME", groupName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.text_view_group_name);
        }
    }
}
