package com.sagarmishra.futsal.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R

class PlayerRequestAdapter():RecyclerView.Adapter<PlayerRequestAdapter.PlayerRequestViewHolder>() {
    class PlayerRequestViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val tvPlayerName:TextView = view.findViewById(R.id.playerName)
        val tvEmail:TextView = view.findViewById(R.id.tvEmail)
        val tvPhone:TextView = view.findViewById(R.id.tvPhone)
        val btnAccept:Button = view.findViewById(R.id.btnAccept)
        val btnDecline:Button = view.findViewById(R.id.btnDecline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerRequestViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PlayerRequestViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}