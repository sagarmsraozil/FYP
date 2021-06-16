package com.sagarmishra.futsal.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Battle
import com.sagarmishra.futsal.model.StaticData

class BattleResultAdapter(val context:Context,val lstBattles:MutableList<Battle>):RecyclerView.Adapter<BattleResultAdapter.BattleResultViewHolder>() {
    class BattleResultViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val ivTeam1:ImageView = view.findViewById(R.id.ivTeam1)
        val ivTeam2:ImageView = view.findViewById(R.id.ivTeam2)
        val tvScores:TextView = view.findViewById(R.id.tvScores)
        val tvPoint:TextView = view.findViewById(R.id.tvPoint)
        val tvTeam1:TextView = view.findViewById(R.id.tvTeam1)
        val tvTeam2:TextView = view.findViewById(R.id.tvTeam2)
        val btnView:Button = view.findViewById(R.id.btnView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattleResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.battle_result_layout,parent,false)
        return BattleResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: BattleResultViewHolder, position: Int) {
        var battle = lstBattles[position]
        if (battle.awayTeam!!.teamLogo != "no-logo.png") {
            var imgPath = RetrofitService.loadImagePath()+battle.awayTeam!!.teamLogo!!.replace("\\", "/")
            Glide.with(context).load(imgPath).into(holder.ivTeam2)
        }
        else
        {
            holder.ivTeam2.setImageResource(R.drawable.logoteam)
        }
        if (StaticData.team!!.teamLogo != "no-logo.png")
        {
            var imgPath = RetrofitService.loadImagePath()+ StaticData.team!!.teamLogo!!.replace("\\", "/")
            Glide.with(context).load(imgPath).into(holder.ivTeam1)
        }
        else
        {
            holder.ivTeam1.setImageResource(R.drawable.logoteam)
        }

        holder.tvScores.text = battle.scoreLine
        holder.tvPoint.text = battle.pointObtained
        holder.tvTeam1.text = StaticData.team!!.teamName
        holder.tvTeam2.text = battle.awayTeam!!.teamName

        //buiding a dialog
        var dialog: Dialog = Dialog(context)
        dialog.setContentView(R.layout.match_details)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        //binding
        val ivCross:ImageView = dialog.findViewById(R.id.ivCross)
        val tvMatch : TextView = dialog.findViewById(R.id.tvMatch)
        val tvFutsalName:TextView = dialog.findViewById(R.id.tvFutsalName)
        val tvFutsalAddress:TextView = dialog.findViewById(R.id.tvFutsalAddress)
        val tvDate:TextView = dialog.findViewById(R.id.tvDate)
        val tvTime:TextView = dialog.findViewById(R.id.tvTime)
        val tvCode:TextView = dialog.findViewById(R.id.tvCode)
        val ivCredited:ImageView = dialog.findViewById(R.id.ivCredited)
        val tvDescription:TextView = dialog.findViewById(R.id.tvDescription)

        tvMatch.text = "${StaticData.team!!.teamName} vs ${battle.awayTeam!!.teamName}"
        tvFutsalName.text = battle.futsal_id!!.futsalName
        tvFutsalAddress.text = battle.futsal_id!!.location
        tvDate.text = battle.date
        tvTime.text = battle.time
        tvCode.text = battle.battleCode
        tvDescription.text = battle.description
        if(battle.pointGiven == "Credited")
        {
            ivCredited.setImageResource(R.drawable.bg_unavailable)
        }
        else
        {
            ivCredited.setImageResource(R.drawable.bg_available)
        }

        ivCross.setOnClickListener {
            dialog.dismiss()
        }

        holder.btnView.setOnClickListener {
            dialog.show()
        }

    }

    override fun getItemCount(): Int {
        return lstBattles.size
    }
}