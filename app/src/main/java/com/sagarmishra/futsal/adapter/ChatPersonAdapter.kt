package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.ChatActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.model.StaticData

class ChatPersonAdapter(val context:Context,var lstPlayers:MutableList<AuthUser>):RecyclerView.Adapter<ChatPersonAdapter.ChatPersonViewHolder>() {
    class ChatPersonViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val layoutLinear:LinearLayout = view.findViewById(R.id.layoutLinear)
        val tvPlayerName:TextView = view.findViewById(R.id.tvPlayerName)
        val tvCount:TextView = view.findViewById(R.id.tvCount)
        val chatCount:LinearLayout = view.findViewById(R.id.chatCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatPersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_users_layout,parent,false)
        return ChatPersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatPersonViewHolder, position: Int) {
        var player = lstPlayers[position]
        holder.tvPlayerName.text = player.userName+"(${StaticData.playerTeam[player._id]})"
        if(StaticData.unseenMessage.keys.contains(player._id) && StaticData.unseenMessage[player._id]!!>0)
        {
            holder.tvCount.text = StaticData.unseenMessage[player._id].toString()
            holder.chatCount.visibility = View.VISIBLE
        }
        else
        {
            holder.chatCount.visibility = View.GONE
        }

        holder.layoutLinear.setOnClickListener {
            StaticData.receiverId = player._id
            var intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("player",player)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return lstPlayers.size
    }
}