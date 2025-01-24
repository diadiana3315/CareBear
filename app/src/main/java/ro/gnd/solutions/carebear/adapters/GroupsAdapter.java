package ro.gnd.solutions.carebear.adapters;

import static ro.gnd.solutions.carebear.fragments.ChatsFragment.CHAT_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.RequiredArgsConstructor;
import ro.gnd.solutions.carebear.R;
import ro.gnd.solutions.carebear.activities.chats.ChatActivity;
import ro.gnd.solutions.carebear.models.BaseUser;
import ro.gnd.solutions.carebear.models.GroupChat;
import ro.gnd.solutions.carebear.services.ChatService;
import ro.gnd.solutions.carebear.services.UserService;

@RequiredArgsConstructor
public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private static final ChatService chatService = ChatService.Companion.getInstance();
    private static final UserService userService = UserService.Companion.getInstance();
    private static final BaseUser loggedUser = UserService.Companion.getInstance().getBaseLoggedUser();

    private final Context context;
    private final List<GroupChat> groupChats;

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupChat groupChat = groupChats.get(position);
        String chatId = groupChat.getChatId();
        String groupName = groupChat.getName();
        holder.groupName.setText(groupName);

        holder.itemView.setOnClickListener(v -> {
            chatService.joinGroupChat(loggedUser, chatId);

            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(CHAT_ID_KEY, chatId);

            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return groupChats.size();
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
