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
import com.example.carebear.models.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter (
    private val context: Context,
    private val messages: List<ChatMessage>
) :
    RecyclerView.Adapter<MessageAdapter.ChatMessageViewHolder>() {

    class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val lastMessage: TextView = itemView.findViewById(R.id.message)
        val date: TextView = itemView.findViewById(R.id.text_date)
        val hour: TextView = itemView.findViewById(R.id.text_hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatMessageViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val chat = messages[position]

        holder.textName.text = chat.sender.name
        holder.lastMessage.text = chat.message

        val timestamp = Date(chat.timestamp)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.date.text = dateFormat.format(timestamp)
        val minuteHourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.hour.text = minuteHourFormat.format(timestamp)
    }

    override fun getItemCount() = messages.size
}
