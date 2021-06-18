package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.MatchMaking
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MatchMakingAdapter(val context:Context,var lstMatchMaking:MutableList<MatchMaking>):RecyclerView.Adapter<MatchMakingAdapter.MatchMakingViewHolder>() {
    class MatchMakingViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val ivLogo:CircleImageView = view.findViewById(R.id.ivLogo)
        val tvTeamName:TextView = view.findViewById(R.id.tvTeamName)
        val tvAge:TextView = view.findViewById(R.id.tvAge)
        val tvLocation:TextView = view.findViewById(R.id.tvLocation)
        val tvDistance:TextView = view.findViewById(R.id.tvDistance)
        val tvTier:TextView = view.findViewById(R.id.tvTier)
        val tvRatio:TextView = view.findViewById(R.id.tvRatio)
        val tvTeam1:TextView = view.findViewById(R.id.tvTeam1)
        val tvTeam2:TextView = view.findViewById(R.id.tvTeam2)
        val tvWin1:TextView = view.findViewById(R.id.tvWin1)
        val tvWin2:TextView = view.findViewById(R.id.tvWin2)
        val tvDraw1:TextView = view.findViewById(R.id.tvDraw1)
        val tvDraw2:TextView = view.findViewById(R.id.tvDraw2)
        val tvLoss1:TextView = view.findViewById(R.id.tvLoss1)
        val tvLoss2:TextView = view.findViewById(R.id.tvLoss2)
        val tvScoreLine:TextView = view.findViewById(R.id.tvScoreLine)
        val btnAsk:Button = view.findViewById(R.id.btnAsk)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchMakingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_matchmaking,parent,false)
        return MatchMakingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchMakingViewHolder, position: Int) {
        var match = lstMatchMaking[position]
        holder.tvTeamName.text = match.opponent!!.teamName+"(${match.opponent!!.teamTag})"
        holder.tvAge.text = "Age Group: "+match.opponent!!.ageGroup.toString()
        holder.tvLocation.text = "Address: "+match.opponent!!.locatedCity
        holder.tvDistance.text = "Distance: "+match.distance+"KM"
        holder.tvTier.text = "Tier: "+match.opponentStats!!.tierNomenclature
        holder.tvRatio.text = "S/C: "+match.ratio.toString()
        holder.tvTeam1.text = match.me!!.teamName
        holder.tvTeam2.text = match.opponent!!.teamName
        holder.tvWin1.text = match.myWin.toString()+"%"
        holder.tvWin2.text = match.opponentWin.toString()+"%"
        holder.tvDraw1.text = match.myDraw.toString()+"%"
        holder.tvDraw2.text = match.opponentDraw.toString()+"%"
        holder.tvLoss1.text = match.myLoss.toString()+"%"
        holder.tvLoss2.text = match.opponentLoss.toString()+"%"
        holder.tvScoreLine.text = match.scoreLine

        if(match.opponent!!.teamLogo == "no-logo.png")
        {
            holder.ivLogo.setImageResource(R.drawable.logoteam)
        }
        else
        {
            var imgPath = RetrofitService.loadImagePath()+match.opponent!!.teamLogo!!.replace("\\","/")
            Glide.with(context).load(imgPath).into(holder.ivLogo)
        }

        holder.btnAsk.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = TeamRepository()
                    val response = repo.requestAMatch(match.me!!._id,match.opponent!!._id)
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            holder.tvScoreLine.snackbar(response.message!!)
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            holder.tvScoreLine.snackbar(response.message!!)
                        }
                    }
                }
                catch(err:Exception)
                {
                    println(err.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        holder.tvScoreLine.snackbar("Server Error!!")
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return lstMatchMaking.size
    }
}