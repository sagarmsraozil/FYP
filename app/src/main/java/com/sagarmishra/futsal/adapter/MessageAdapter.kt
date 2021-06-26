package com.sagarmishra.futsal.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.entityapi.Chat
import com.sagarmishra.futsal.model.StaticData

class MessageAdapter(val context:Context,var lstMessages:MutableList<Chat>):RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    class MessageViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val tvTime:TextView = view.findViewById(R.id.tvTime)
        val tvMessage:TextView = view.findViewById(R.id.tvMessage)
        val layoutMessage:LinearLayout = view.findViewById(R.id.layoutMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_messages_layout,parent,false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        var message = lstMessages[position]

        if(message.sender!!._id == StaticData.user!!._id)
        {
            holder.layoutMessage.setBackgroundColor(Color.parseColor("#CCC969"))
        }


        holder.tvMessage.text = message.message
        holder.tvTime.text = message.dateAndTime


    }

    override fun getItemCount(): Int {
        return lstMessages.size
    }
}