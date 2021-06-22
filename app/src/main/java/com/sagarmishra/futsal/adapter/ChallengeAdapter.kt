package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.entityapi.TeamChallenges
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChallengeAdapter(val context:Context,var lstChallenges:MutableList<TeamChallenges>):RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {
    class ChallengeViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val tvChallenge:TextView = view.findViewById(R.id.tvChallenge)
        val tvTitleReward:TextView = view.findViewById(R.id.titleReward)
        val progress:ProgressBar = view.findViewById(R.id.progress)
        val tvRange:TextView = view.findViewById(R.id.tvRange)
        val btnCollect:Button = view.findViewById(R.id.btnCollect)
        val btnCollect1:Button = view.findViewById(R.id.btnCollect1)
        val btnCollect2:Button = view.findViewById(R.id.btnCollect2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_challenges,parent,false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        var challenge = lstChallenges[position]

        holder.tvChallenge.text = challenge.challenge_id!!.challengeTitle
        holder.tvTitleReward.text = "Title Reward: ${challenge.challenge_id!!.challengeReward}"
        holder.tvRange.text = "${challenge.progessPoint}/${challenge.challenge_id!!.challengeProgressionPoint}"
        holder.progress.max = challenge.challenge_id!!.challengeProgressionPoint
        holder.progress.progress = challenge.progessPoint

        if(challenge.status == "Completed" && challenge.rewardCollected == false)
        {
            holder.btnCollect.visibility = View.GONE
            holder.btnCollect1.visibility = View.VISIBLE
            holder.btnCollect2.visibility = View.GONE
        }
        else if(challenge.status == "Completed" && challenge.rewardCollected == true)
        {
            holder.btnCollect.visibility = View.GONE
            holder.btnCollect1.visibility = View.GONE
            holder.btnCollect2.visibility = View.VISIBLE
        }
        else
        {
            holder.btnCollect.visibility = View.VISIBLE
            holder.btnCollect1.visibility = View.GONE
            holder.btnCollect2.visibility = View.GONE
        }

        holder.btnCollect1.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = TeamRepository()
                    val response = repo.addTitleFromChallenge(challenge._id)
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            holder.btnCollect1.snackbar(response.message!!);
                            var intent = Intent(context,MainActivity::class.java)
                            intent.putExtra("FRAGMENT_NUMBER",8)
                            context.startActivity(intent)
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            holder.btnCollect1.snackbar(response.message!!);
                        }
                    }
                }
                catch(err:Exception)
                {
                    println(err.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        holder.btnCollect1.snackbar("Server Error");
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return lstChallenges.size
    }
}