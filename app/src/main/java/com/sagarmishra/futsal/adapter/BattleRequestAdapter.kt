package com.sagarmishra.futsal.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.RequestRepository
import com.sagarmishra.futsal.entityapi.MatchRequest
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class BattleRequestAdapter(val activity:Activity,val context:Context,val lstRequests:MutableList<MatchRequest>,var task:String):RecyclerView.Adapter<BattleRequestAdapter.BattleRequestViewHolder>() {
    class BattleRequestViewHolder(val view:View,activity: Activity):RecyclerView.ViewHolder(view)
    {
        val tvTeamName:TextView = view.findViewById(R.id.tvTeamName)
        val tvAge:TextView = view.findViewById(R.id.tvAge)
        val tvAddress:TextView = view.findViewById(R.id.tvAddress)
        val ivStatus:ImageView = view.findViewById(R.id.ivStatus)
        val btnAccept:Button = view.findViewById(R.id.btnAccept)
        val btnDecline:Button = view.findViewById(R.id.btnDecline)
        val layoutDecision:LinearLayout = view.findViewById(R.id.layoutDecision)
        val tvRequests:TextView = activity.findViewById(R.id.tvRequests)
        val tvDate:TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattleRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_battle_requests,parent,false)
        return BattleRequestViewHolder(view,activity)
    }

    private fun decisionMap(holder:BattleRequestViewHolder,position: Int,decision:String,reqId:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = RequestRepository()
                val response = repo.decisionBattle(reqId,StaticData.team!!._id,decision)
                if(response.success == true)
                {
                    withContext(Dispatchers.Main)
                    {
                        holder.tvTeamName.snackbar(response.message!!)
                        lstRequests.removeAt(position)
                        holder.tvRequests.text = "${lstRequests.size} requests"
                        notifyDataSetChanged()
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        holder.tvTeamName.snackbar(response.message!!)
                    }
                }
            }
            catch (err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    holder.tvTeamName.snackbar("Server Error")
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BattleRequestViewHolder, position: Int) {
        var request = lstRequests[position]
        if(task == "myRequests")
        {
            holder.tvTeamName.text = request.awayTeam!!.teamName+"(${request.awayTeam!!.teamTag}) ${request.awayTeam!!.teamCode}"
            holder.tvAge.text = "Age Group: ${request.awayTeam!!.ageGroup}"
            holder.tvAddress.text = "Location: ${request.awayTeam!!.locatedCity}"

            if(request.status == "Accepted")
            {
                holder.ivStatus.setImageResource(R.drawable.ic_tick)
            }
            else if(request.status == "Declined")
            {
                holder.ivStatus.setImageResource(R.drawable.ic_baseline_disabled_by_default_24)
            }
            else
            {
                holder.ivStatus.setImageResource(R.drawable.ic_hourglass)
            }

            holder.layoutDecision.visibility = View.GONE
            holder.ivStatus.visibility = View.VISIBLE
        }
        else
        {
            holder.tvTeamName.text = request.homeTeam!!.teamName+"(${request.homeTeam!!.teamTag}) ${request.homeTeam!!.teamCode}"
            holder.tvAge.text = "Age Group: ${request.homeTeam!!.ageGroup}"
            holder.tvAddress.text = "Location: ${request.homeTeam!!.locatedCity}"

            holder.layoutDecision.visibility = View.VISIBLE
            holder.ivStatus.visibility = View.GONE

            holder.btnAccept.setOnClickListener {
                decisionMap(holder,position,"Accepted",request._id)
            }

            holder.btnDecline.setOnClickListener {
                decisionMap(holder,position,"Declined",request._id)
            }
        }

        holder.tvDate.text = request.fancyDate
    }

    override fun getItemCount(): Int {
        return lstRequests.size
    }


}