package com.example.carebear.adapters;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.activities.chats.ChatForumActivity;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private Context context;
    private List<String> groupNames;

    public GroupsAdapter(Context context, List<String> groupNames) {
        this.context = context;
        this.groupNames = groupNames;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
//        String groupName = groupNames.get(position);
//        holder.groupName.setText(groupName);
//
//        holder.attachMediaButton.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/* video/*");
//            context.startActivity(intent);
//        });
//    }
@Override
public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
    String groupName = groupNames.get(position);
    holder.groupName.setText(groupName);

    holder.itemView.setOnClickListener(v -> {
        Intent intent = new Intent(context, ChatForumActivity.class);
        intent.putExtra("GROUP_NAME", groupName); // Pass the group name or ID
        context.startActivity(intent);
    });
}


    @Override
    public int getItemCount() {
        return groupNames.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        Button attachMediaButton;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            attachMediaButton = itemView.findViewById(R.id.btn_attach_media);
        }
    }
}
