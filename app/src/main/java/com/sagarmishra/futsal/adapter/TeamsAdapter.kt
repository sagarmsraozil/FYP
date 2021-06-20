package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.SingleTeamActivity
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Team
import de.hdodenhof.circleimageview.CircleImageView

class TeamsAdapter(val context:Context,var lstTeams:MutableList<Team>,var task:String):RecyclerView.Adapter<TeamsAdapter.TeamsViewHolder>() {
    class TeamsViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val ivTeamLogo:CircleImageView = view.findViewById(R.id.ivTeamLogo)
        val tvTeamName:TextView = view.findViewById(R.id.tvTeam)
        val tvAgeGroup:TextView = view.findViewById(R.id.tvAgeGroup)
        val tvPlayers:TextView = view.findViewById(R.id.tvPlayers)
        val layout:LinearLayout = view.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_teams,parent,false)
        return TeamsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        var team = lstTeams[position]
        holder.tvTeamName.text = team.teamName
        holder.tvAgeGroup.text = team.ageGroup.toString()
        holder.tvPlayers.text = "${team.teamPlayers!!.size}/8"
        if(team.teamLogo!! == "no-logo.png")
        {
            holder.ivTeamLogo.setImageResource(R.drawable.logoteam)
        }
        else
        {
            var imgPath = RetrofitService.loadImagePath()+team.teamLogo!!.replace("\\","/")
            Glide.with(context).load(imgPath).into(holder.ivTeamLogo)
        }

        holder.layout.setOnClickListener {
            val intent = Intent(context,SingleTeamActivity::class.java)
            intent.putExtra("task",task)
            intent.putExtra("tid",team._id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return lstTeams.size
    }
}