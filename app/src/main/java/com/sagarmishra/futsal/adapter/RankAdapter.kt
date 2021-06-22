package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.SingleTeamActivity
import com.sagarmishra.futsal.StatsActivity
import com.sagarmishra.futsal.entityapi.TeamStats

class RankAdapter(val context:Context,var lstRanker:MutableList<TeamStats>):RecyclerView.Adapter<RankAdapter.RankViewHolder>() {
    class RankViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val tvRank:TextView = view.findViewById(R.id.tvRank)
        val tvTeamName:TextView = view.findViewById(R.id.tvTeamName)
        val tvPoints:TextView = view.findViewById(R.id.tvPoints)
        val tvRatio:TextView = view.findViewById(R.id.tvRatio)
        val tabLayout:TableLayout = view.findViewById(R.id.tabLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.tablelayout_layout,parent,false)
        return RankViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        var rank =lstRanker[position]
        holder.tvRank.text = "#${position+1}"
        holder.tvTeamName.text = rank.team_id!!.teamName
        holder.tvPoints.text = rank.pointsCollected.toString()
        holder.tvRatio.text = rank.ratio.toString()

        holder.tabLayout.setOnClickListener {
            val intent = Intent(context,SingleTeamActivity::class.java)
            intent.putExtra("task","Search")
            intent.putExtra("tid",rank.team_id!!._id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return lstRanker.size
    }
}