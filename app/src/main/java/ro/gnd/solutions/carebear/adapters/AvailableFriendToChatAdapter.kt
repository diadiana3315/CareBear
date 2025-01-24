package ro.gnd.solutions.carebear.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.gnd.solutions.carebear.R
import ro.gnd.solutions.carebear.models.BaseUser
import ro.gnd.solutions.carebear.models.Friend
import ro.gnd.solutions.carebear.services.ChatService
import ro.gnd.solutions.carebear.services.FriendService
import ro.gnd.solutions.carebear.services.UserService

class AvailableFriendToChatAdapter (
    private val context: Context,
    private val availableFriendsToChat: List<Friend>
) :
    RecyclerView.Adapter<AvailableFriendToChatAdapter.AvailableFriendToChatViewHolder>() {

    class AvailableFriendToChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEmail: TextView = itemView.findViewById(R.id.text_email)
        val startNewChatButtonAction: Button = itemView.findViewById(R.id.start_chat_button_action)
        val friendService: FriendService = FriendService.getInstance()
        val chatService: ChatService = ChatService.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableFriendToChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_available_friends_to_chat, parent, false)
        return AvailableFriendToChatViewHolder(view)
    }


    override fun onBindViewHolder(holder: AvailableFriendToChatViewHolder, position: Int) {
        val friend = availableFriendsToChat[position]
        val friendId = friend.friendId
        val friendEmail = friend.friendEmail
        val friendBaseUser = BaseUser(friendId, friendEmail, friendEmail)
        val loggedBaseUser = UserService.getInstance().getBaseLoggedUser()

        holder.textEmail.text = friendEmail
        holder.startNewChatButtonAction.setOnClickListener {
            val newChat = holder.chatService.createPrivateChat(loggedBaseUser, friendBaseUser)
            holder.friendService.startChat(context, newChat.chatId, friendId, friendEmail)
        }
    }

    override fun getItemCount() = availableFriendsToChat.size


}