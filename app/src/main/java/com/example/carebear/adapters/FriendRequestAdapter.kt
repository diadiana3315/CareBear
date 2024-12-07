package com.example.carebear.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.models.FriendRequest

class FriendRequestAdapter (private val context: Context, private val friendRequests: List<FriendRequest>, private val onActionClick: (FriendRequest) -> Unit) :
    RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>() {

    class FriendRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEmail: TextView = itemView.findViewById(R.id.text_email)
        val acceptButtonAction: Button = itemView.findViewById(R.id.accept_button_action)
        val denyButtonAction: Button = itemView.findViewById(R.id.deny_button_action)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend_request, parent, false)
        return FriendRequestViewHolder(view)
    }


    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        val request = friendRequests[position]

        holder.textEmail.text = request.requesterEmail
    }

    override fun getItemCount() = friendRequests.size
}