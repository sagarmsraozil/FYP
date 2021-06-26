package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.ChatActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.dateToAgeConverter
import de.hdodenhof.circleimageview.CircleImageView

class AvailablePlayersAdapter(val context:Context,var lstPlayers:MutableList<AuthUser>):RecyclerView.Adapter<AvailablePlayersAdapter.AvailablePlayersViewHolder>() {
    class AvailablePlayersViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val cvProfile :CircleImageView = view.findViewById(R.id.cvProfile)
        val tvPlayerName :TextView = view.findViewById(R.id.tvPlayerName)
        val tvUserName:TextView = view.findViewById(R.id.tvUsername)
        val tvTitle:TextView = view.findViewById(R.id.tvTitle)
        val btnMessage:Button = view.findViewById(R.id.btnMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailablePlayersViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.available_player_card_layout,parent,false)
       return AvailablePlayersViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvailablePlayersViewHolder, position: Int) {
        var player = lstPlayers[position]
        if(player.profilePicture == "no-photo.jpg")
        {
            holder.cvProfile.setImageResource(R.drawable.logoteam)
        }
        else
        {
            var imgPath = RetrofitService.loadImagePath()+player.profilePicture!!.replace("\\","/")
            Glide.with(context).load(imgPath).into(holder.cvProfile)
        }

        holder.tvPlayerName.text = player.firstName+" "+player.lastName
        holder.tvUserName.text = player.userName+"(${dateToAgeConverter(player.dob!!)} yrs)"
        if(player.activeTitle != null && player.activeTitle != "")
        {
            holder.tvTitle.visibility = View.VISIBLE
            holder.tvTitle.text = player.activeTitle
        }
        else
        {
            holder.tvTitle.visibility = View.GONE
        }

        holder.btnMessage.setOnClickListener {
            StaticData.receiverId = player._id
            var intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("player",player)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return lstPlayers.size
    }
}