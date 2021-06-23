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

class ParticipatedTeamAdapter(val context:Context,var lstTeams:MutableList<Team>):RecyclerView.Adapter<ParticipatedTeamAdapter.ParticipatedTeamViewHolder>() {
    class ParticipatedTeamViewHolder(val view:View): RecyclerView.ViewHolder(view)
    {
        val cvLogo:CircleImageView = view.findViewById(R.id.cvLogo)
        val tvTeamName:TextView = view.findViewById(R.id.tvTeamName)
        val linearLayout:LinearLayout = view.findViewById(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipatedTeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.participated_teams_layout,parent,false)
        return ParticipatedTeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipatedTeamViewHolder, position: Int) {
        var team = lstTeams[position]
        holder.tvTeamName.text = team.teamName
        if(team.teamLogo == "no-logo.png")
        {
            holder.cvLogo.setImageResource(R.drawable.logoteam)
        }
        else
        {
            var imgPath = RetrofitService.loadImagePath()+team.teamLogo!!.replace("\\","/")
            Glide.with(context).load(imgPath).into(holder.cvLogo)
        }

        holder.linearLayout.setOnClickListener {
            var intent = Intent(context,SingleTeamActivity::class.java)
            intent.putExtra("task","TournamentSearch")
            intent.putExtra("tid",team._id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return lstTeams.size
    }
}