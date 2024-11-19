package com.example.carebear.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carebear.R
import com.example.carebear.models.User

class SearchedUserAdapter(private val userList: List<User>, private val onActionClick: (User) -> Unit) :
    RecyclerView.Adapter<SearchedUserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEmail: TextView = itemView.findViewById(R.id.text_email)
        val buttonAction: Button = itemView.findViewById(R.id.button_action)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_searched_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.textEmail.text = user.email
        holder.buttonAction.setOnClickListener {
            onActionClick(user)
        }
    }

    override fun getItemCount() = userList.size
}
