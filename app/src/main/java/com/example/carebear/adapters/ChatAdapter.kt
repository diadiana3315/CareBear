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
import com.example.carebear.models.ChatMembership
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(
    private val context: Context,
    private val chatMemberships: List<ChatMembership>,
    private val onItemClick: (ChatMembership) -> Unit
) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val lastMessage: TextView = itemView.findViewById(R.id.last_message)
        val date: TextView = itemView.findViewById(R.id.text_date)
        val hour: TextView = itemView.findViewById(R.id.text_hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) { // TODO
        val chat = chatMemberships[position]

        holder.textName.text = chat.recipientName
//        holder.lastMessage.text = chat.lastMessage
//
//        val timestamp = Date(chat.timestamp)
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        holder.date.text = dateFormat.format(timestamp)
//        val minuteHourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
//        holder.hour.text = minuteHourFormat.format(timestamp)

        holder.itemView.setOnClickListener {
            onItemClick(chat) // Trigger the callback with the clicked item
        }
    }

    override fun getItemCount() = chatMemberships.size
}