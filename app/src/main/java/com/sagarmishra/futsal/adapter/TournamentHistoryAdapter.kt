package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.TournamentMatch
import com.sagarmishra.futsal.model.StaticData
import de.hdodenhof.circleimageview.CircleImageView

class TournamentHistoryAdapter(val context:Context,var lstTournament:MutableList<TournamentMatch>,var type:String):RecyclerView.Adapter<TournamentHistoryAdapter.TournamentHistoryViewHolder>() {
    class TournamentHistoryViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val matchStats:TextView = view.findViewById(R.id.matchStats)
        val tournamentName:TextView = view.findViewById(R.id.tvTournmentName)
        val cvTeam1:CircleImageView = view.findViewById(R.id.cvTeam1)
        val cvTeam2:CircleImageView = view.findViewById(R.id.cvTeam2)
        val tvTeamName1:TextView = view.findViewById(R.id.tvTeamName1)
        val tvTeamName2:TextView = view.findViewById(R.id.tvTeamName2)
        val tvTime:TextView = view.findViewById(R.id.tvTime)
        val tvFutsal:TextView = view.findViewById(R.id.tvFutsal)
        val tvStage:TextView = view.findViewById(R.id.tvStage)
        val tvScores:TextView = view.findViewById(R.id.tvScores)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tournament_history_layout,parent,false)
        return TournamentHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TournamentHistoryViewHolder, position: Int) {
        var match = lstTournament[position]
        holder.tournamentName.text = match.tournament_id!!.tournamentName
        holder.tvTeamName1.text = match.team1!!.teamName
        holder.tvTeamName2.text = match.team2!!.teamName

        if(StaticData.tournamentHistory!!.keys.contains(match._id) && type == "history")
        {
            holder.matchStats.text = StaticData.tournamentHistory!![match._id].toString()
        }

        if(type == "match")
        {
            holder.matchStats.text = "Best Wishes"
        }


        if(match.team1!!.teamLogo != "no-logo.png")
        {
            var imgPath = RetrofitService.loadImagePath()+match.team1!!.teamLogo!!.replace("\\","/")
            Glide.with(context).load(imgPath).into(holder.cvTeam1)
        }
        else
        {
            holder.cvTeam1.setImageResource(R.drawable.logoteam)
        }

        if(match.team2!!.teamLogo != "no-logo.png")
        {
            var imgPath = RetrofitService.loadImagePath()+match.team2!!.teamLogo!!.replace("\\","/")
            Glide.with(context).load(imgPath).into(holder.cvTeam2)
        }
        else
        {
            holder.cvTeam2.setImageResource(R.drawable.logoteam)
        }

        if(match.gameDate != null)
        {
            holder.tvTime.text = match.fancyDate+"|"+match.time
            holder.tvFutsal.text = match.venue!!.futsalName
            holder.tvStage.text = match.round+"|"+match.matchType
        }
        else
        {
            holder.tvTime.text = "Not Announced"
            holder.tvFutsal.text = "Not Announced"
            holder.tvStage.text = "N/A"
        }

        if(type == "match")
        {
            holder.tvScores.text = "VS"
        }
        else
        {
            holder.tvScores.text = "${match.goals!![0]} - ${match.goals!![1]}"
        }


    }

    override fun getItemCount(): Int {
        return lstTournament.size
    }
}