package com.sagarmishra.futsal.adapter

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.entityapi.TournamentGroup

class GroupPointAdapter(val context:Context,var lstPoints:MutableList<MutableList<TournamentGroup>>):RecyclerView.Adapter<GroupPointAdapter.GroupPointViewHolder>() {
    private lateinit var adapter2:PointAdjustAdapter
    class GroupPointViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val GroupName:TextView = view.findViewById(R.id.GroupName)
        val recycler:RecyclerView = view.findViewById(R.id.recyclerPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupPointViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.point_table1,parent,false)
        return GroupPointViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupPointViewHolder, position: Int) {
        var group = lstPoints[position]

        var groupName = group[0].group

        holder.GroupName.text = groupName
        adapter2 = PointAdjustAdapter(context,group)
        holder.recycler.adapter = adapter2
        holder.recycler.layoutManager = LinearLayoutManager(context)
    }

    override fun getItemCount(): Int {
        return lstPoints.size
    }

}