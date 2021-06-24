package com.sagarmishra.futsal.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.entityapi.TournamentGroup

class PointAdjustAdapter(val context:Context,var lstGroup:MutableList<TournamentGroup>):RecyclerView.Adapter<PointAdjustAdapter.PointAdjustViewHolder>() {
    class PointAdjustViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val tvRank:TextView = view.findViewById(R.id.tvRank)
        val tvTag:TextView = view.findViewById(R.id.tvTag)
        val tvMatches:TextView = view.findViewById(R.id.tvMatches)
        val tvWins:TextView = view.findViewById(R.id.tvWins)
        val tvLoss:TextView = view.findViewById(R.id.tvLoss)
        val tvDraw:TextView = view.findViewById(R.id.tvDraw)
        val tvGD:TextView = view.findViewById(R.id.tvGD)
        val tvPoints:TextView = view.findViewById(R.id.tvPoints)
        val tableLayout:TableLayout = view.findViewById(R.id.tableLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointAdjustViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.points_adjust_layout,parent,false)
        return PointAdjustViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointAdjustViewHolder, position: Int) {
        var group = lstGroup[position]

        holder.tvRank.text = "#${position+1}"
        holder.tvTag.text = group.team!!.teamTag
        holder.tvMatches.text = group.matchPlayed.toString()
        holder.tvWins.text = group.win.toString()
        holder.tvLoss.text = group.loss.toString()
        holder.tvDraw.text = group.draw.toString()
        holder.tvGD.text = group.goalDifference.toString()
        holder.tvPoints.text = group.Points.toString()

        var dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_titles_browser)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        //binding
        var ivMedal:ImageView = dialog.findViewById(R.id.ivMedal)
        var tvTitle:TextView = dialog.findViewById(R.id.tvTitle)
        var ivCross: ImageView = dialog.findViewById(R.id.ivCross)

        ivMedal.visibility = View.GONE
        ivCross.visibility = View.VISIBLE
        tvTitle.text = group.team!!.teamName

        ivCross.setOnClickListener {
            dialog.dismiss()
        }

        holder.tableLayout.setOnClickListener {
         dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return lstGroup.size
    }
}