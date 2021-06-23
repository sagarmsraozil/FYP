package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.SingleTournamentActivity
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Tournament
import com.sagarmishra.futsal.model.StaticData

class TournamentAdapter(val context:Context,var lstTournament:MutableList<Tournament>):RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder>() {
    class TournamentViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val ivBanner:ImageView = view.findViewById(R.id.ivBanner)
        val tvStatus:TextView = view.findViewById(R.id.tvStatus)
        val tvTournamentName:TextView = view.findViewById(R.id.tvTournamentName)
        val tvParticipation:TextView = view.findViewById(R.id.tvParticipation)
        val tvAgeGroup:TextView = view.findViewById(R.id.tvAgeGroup)
        val tvParticipationCount:TextView = view.findViewById(R.id.tvParticipationCount)
        val tvEventDate:TextView = view.findViewById(R.id.tvEventDate)
        val tvMatchIteration:TextView = view.findViewById(R.id.tvMatchIteration)
        val tvTournamentCode:TextView = view.findViewById(R.id.tvTournamentCode)
        val tvTournamentFormat:TextView = view.findViewById(R.id.tvTournamentFormat)
        val tvPrize:TextView = view.findViewById(R.id.tvPrize)
        val tvOrganizedBy:TextView = view.findViewById(R.id.tvOrganizedBy)
        val tvSponsors:TextView = view.findViewById(R.id.tvSponsors)
        val btnView:TextView = view.findViewById(R.id.btnView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_tournaments,parent,false)
        return TournamentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        var tournament = lstTournament[position]

        if(tournament.tournamentBanner == "no-banner.png")
        {
            holder.ivBanner.setImageResource(R.drawable.logoteam)
        }
        else
        {
            var imgPath = RetrofitService.loadImagePath()+tournament.tournamentBanner!!.replace("\\","/")
            Glide.with(context).load(imgPath).into(holder.ivBanner)
        }

        holder.tvTournamentName.text = tournament.tournamentName
        holder.tvParticipation.text = "Participation: ${tournament.participationStartsFrom}-${tournament.participationEndsAt}"
        holder.tvAgeGroup.text = "Age group: ${tournament.minAgeGroup}-${tournament.maxAgeGroup}"
        holder.tvParticipationCount.text = "Participation Count: ${tournament.participatedTeams!!.size}/${tournament.participationCount}"
        holder.tvTournamentCode.text = "Tournament Code: "+tournament.tournamentCode
        holder.tvTournamentFormat.text = "Tournament Format: "+tournament.tournamentFormat
        holder.tvOrganizedBy.text = "Organized By: "+tournament.organizedBy
        if(tournament.tournamentFormat == "League")
        {
            holder.tvMatchIteration.text = "Match Iteration: "+tournament.grpMatchPhase.toString()
            holder.tvMatchIteration.visibility = View.VISIBLE
        }
        else
        {
            holder.tvMatchIteration.visibility = View.GONE
        }

        var prizeBox = ""
        for(i in tournament.prizePool!!)
        {
            prizeBox+="${i["position"]}: ${i["prize"]}\n"
        }
        holder.tvPrize.text = prizeBox


        if(tournament.startsFrom != null)
        {
            holder.tvEventDate.text = "Event: ${tournament.startsFrom}-${tournament.endsAt}"
            holder.tvEventDate.visibility = View.VISIBLE
        }
        else
        {
            holder.tvEventDate.visibility = View.GONE
        }

        if(tournament.sponsors != null)
        {
            holder.tvSponsors.text = "Sponsors: "+tournament.sponsors
            holder.tvSponsors.visibility = View.VISIBLE
        }
        else
        {
            holder.tvSponsors.visibility = View.GONE
        }


        if(tournament.status == "Opening Soon")
        {
            holder.tvStatus.text = "Opening Soon"
        }
        else if(tournament.status == "On Count")
        {
            holder.tvStatus.text = "Participation Ongoing"
        }
        else if(tournament.status == "Participation Ended")
        {
            holder.tvStatus.text = "Participation Ended"
        }
        else if(tournament.status == "Date Out")
        {
            holder.tvStatus.text = "Date Out"
        }
        else if(tournament.status == "Ongoing")
        {
            holder.tvStatus.text = "Ongoing"
        }
        else if(tournament.status == "Finished")
        {
            holder.tvStatus.text = "Finished"
        }

        holder.btnView.setOnClickListener {
            StaticData.tournamentId = tournament._id
            val intent = Intent(context,SingleTournamentActivity::class.java)
            intent.putExtra("tournamentId",tournament._id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return lstTournament.size
    }


}