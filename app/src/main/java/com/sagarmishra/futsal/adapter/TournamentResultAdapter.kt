package com.sagarmishra.futsal.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TournamentRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.TournamentMatch
import com.sagarmishra.futsal.entityapi.TournamentPerformance
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class TournamentResultAdapter(val context:Context,var lstMatches:MutableList<TournamentMatch>):RecyclerView.Adapter<TournamentResultAdapter.TournamentResultViewholder>() {
    class TournamentResultViewholder(val view:View):RecyclerView.ViewHolder(view)
    {
        val matchAnalysis: TextView = view.findViewById(R.id.matchAnalysis)
        val tournamentName: TextView = view.findViewById(R.id.tvTournmentName)
        val cvTeam1: CircleImageView = view.findViewById(R.id.cvTeam1)
        val cvTeam2: CircleImageView = view.findViewById(R.id.cvTeam2)
        val tvTeamName1: TextView = view.findViewById(R.id.tvTeamName1)
        val tvTeamName2: TextView = view.findViewById(R.id.tvTeamName2)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvFutsal: TextView = view.findViewById(R.id.tvFutsal)
        val tvStage: TextView = view.findViewById(R.id.tvStage)
        val tvScores: TextView = view.findViewById(R.id.tvScores)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentResultViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tournament_results,parent,false)
        return TournamentResultViewholder(view)
    }

    override fun onBindViewHolder(holder: TournamentResultViewholder, position: Int) {
        var match = lstMatches[position]
        holder.tournamentName.text = match.tournament_id!!.tournamentName
        holder.tvTeamName1.text = match.team1!!.teamName
        holder.tvTeamName2.text = match.team2!!.teamName


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

        if(match.status == "Starting Soon")
        {
            holder.tvScores.text = "VS"
            holder.matchAnalysis.visibility = View.VISIBLE
            var teamP1:TournamentPerformance = TournamentPerformance()
            var teamP2:TournamentPerformance = TournamentPerformance()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = TournamentRepository()
                    val response = repo.teamPerformance(StaticData.tournamentId,match.team1!!._id)
                    val response2 = repo.teamPerformance(StaticData.tournamentId,match.team2!!._id)
                    if(response.success == true)
                    {
                        teamP1 = response.data!!
                    }
                    else
                    {
                        println(response.message!!)
                    }
                    if(response2.success == true)
                    {
                        teamP2 = response.data!!
                    }
                    else
                    {
                        println(response.message!!)
                    }
                }
                catch(err:Exception)
                {
                    println(err.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        holder.tvScores.snackbar("Server Error")
                    }
                }
            }

            var dialog = Dialog(context)
            dialog.setContentView(R.layout.tournament_prediction_layout)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(false)


            //binding
            var tvMatch:TextView = dialog.findViewById(R.id.tvMatch)
            var ivCross:ImageView = dialog.findViewById(R.id.ivCross)
            var team1:TextView = dialog.findViewById(R.id.team1)
            var team2:TextView = dialog.findViewById(R.id.team2)
            var team1Match:TextView = dialog.findViewById(R.id.team1Match)
            var team2Match:TextView = dialog.findViewById(R.id.team2Match)
            var team1GoalsS:TextView = dialog.findViewById(R.id.team1GoalsS)
            var team2GoalsS:TextView = dialog.findViewById(R.id.team2GoalsS)
            var team1GoalsC:TextView = dialog.findViewById(R.id.team1GoalsC)
            var team2GoalsC:TextView = dialog.findViewById(R.id.team2GoalsC)
            var team1Win:TextView = dialog.findViewById(R.id.team1Win)
            var team2Win:TextView = dialog.findViewById(R.id.team2Win)
            var team1Draw:TextView = dialog.findViewById(R.id.team1Draw)
            var team2Draw:TextView = dialog.findViewById(R.id.team2Draw)
            var team1Loss:TextView = dialog.findViewById(R.id.team1Loss)
            var team2Loss:TextView = dialog.findViewById(R.id.team2Loss)
            var team1Ratio:TextView = dialog.findViewById(R.id.team1Ratio)
            var team2Ratio:TextView = dialog.findViewById(R.id.team2Ratio)
            var win1:TextView = dialog.findViewById(R.id.win1)
            var win2:TextView = dialog.findViewById(R.id.win2)
            var loss1:TextView = dialog.findViewById(R.id.loss1)
            var loss2:TextView = dialog.findViewById(R.id.loss2)
            var draw1:TextView = dialog.findViewById(R.id.draw1)
            var draw2:TextView = dialog.findViewById(R.id.draw2)
            var tvScoreLine:TextView = dialog.findViewById(R.id.tvScoreLine)
            var predictionLayout:LinearLayout = dialog.findViewById(R.id.layoutPrediction)

            ivCross.setOnClickListener {
                dialog.dismiss()
            }

            tvMatch.text = "${match.team1!!.teamName} vs ${match.team2!!.teamName}"
            team1.text = match.team1!!.teamName
            team2.text = match.team2!!.teamName
            team1Match.text = teamP1.matchPlayed.toString()
            team2Match.text = teamP2.matchPlayed.toString()
            team1GoalsS.text = teamP1.goalsScored.toString()
            team2GoalsS.text = teamP2.goalsScored.toString()
            team1GoalsC.text = teamP1.goalsConceaded.toString()
            team2GoalsC.text = teamP2.goalsConceaded.toString()
            team1Win.text = teamP1.winPercent.toString()+"%"
            team2Win.text = teamP2.winPercent.toString()+"%"
            team1Draw.text = teamP1.drawPercent.toString()+"%"
            team2Draw.text = teamP2.drawPercent.toString()+"%"
            team1Loss.text = teamP1.lossPercent.toString()+"%"
            team2Loss.text = teamP2.lossPercent.toString()+"%"
            team1Ratio.text = teamP1.ratio.toString()
            team2Ratio.text = teamP2.ratio.toString()

            if(match.analysisStatus == "Analyzed")
            {
                predictionLayout.visibility = View.VISIBLE
                win1.text = match.win1.toString()+"%"
                win2.text = match.win2.toString()+"%"
                draw1.text = match.draw1.toString()+"%"
                draw2.text = match.draw2.toString()+"%"
                loss1.text = match.loss1.toString()+"%"
                loss2.text = match.loss2.toString()+"%"
                tvScoreLine.text = match.scoreLinePrediction
            }
            else
            {
                predictionLayout.visibility = View.GONE
            }


            holder.matchAnalysis.setOnClickListener {
                dialog.show()
            }


        }
        else
        {
            holder.tvScores.text = "${match.goals!![0]} - ${match.goals!![1]}"
            holder.matchAnalysis.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return lstMatches.size
    }
}