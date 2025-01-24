package ro.gnd.solutions.carebear.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.gnd.solutions.carebear.R
import ro.gnd.solutions.carebear.models.Friend
import ro.gnd.solutions.carebear.services.FriendService

class FriendAdapter (private val context: Context, private val friends: List<Friend>) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEmail: TextView = itemView.findViewById(R.id.text_email)
        val deleteFriendButton: Button = itemView.findViewById(R.id.delete_friend)
        val friendService: FriendService = FriendService.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }


    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]

        holder.textEmail.text = friend.friendEmail
        holder.deleteFriendButton.setOnClickListener {
            val friendId = friend.friendId
            holder.friendService.deleteFriend(context, friendId)
        }
    }

    override fun getItemCount() = friends.size
}