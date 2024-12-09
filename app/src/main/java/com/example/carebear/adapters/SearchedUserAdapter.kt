package com.example.carebear.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.models.User
import com.example.carebear.services.FriendService
import com.google.firebase.auth.FirebaseAuth

class SearchedUserAdapter(private val context: Context, private val userList: List<User>) :
    RecyclerView.Adapter<SearchedUserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEmail: TextView = itemView.findViewById(R.id.text_email)
        val buttonAction: Button = itemView.findViewById(R.id.button_action)
        val friendService: FriendService = FriendService.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_searched_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.textEmail.text = user.email
        holder.buttonAction.setOnClickListener {
            val senderId = FirebaseAuth.getInstance().currentUser?.uid
            val senderEmail = FirebaseAuth.getInstance().currentUser?.email
            if (senderId != null) {
                if (senderEmail != null) {
                    holder.friendService.sendFriendRequest(context, senderId, user.id, senderEmail)
                }
            } else {
                Toast.makeText(context, "Friend request failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = userList.size
}
