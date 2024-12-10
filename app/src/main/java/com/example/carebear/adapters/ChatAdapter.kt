package com.example.carebear.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.models.Chat
import java.time.format.DateTimeFormatter

class ChatAdapter (private val context: Context, private val chats: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val lastMessage: TextView = itemView.findViewById(R.id.last_message)
        val timestamp: TextView = itemView.findViewById(R.id.text_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ChatViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]

        holder.textName.text = chat.name
        holder.lastMessage.text = chat.lastMessage
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        val formattedDate = chat.timestamp.format(formatter)
        holder.timestamp.text = formattedDate
    }

    override fun getItemCount() = chats.size
}