package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R

class RuleAdapter(val context:Context,var lstRules:MutableList<String>):RecyclerView.Adapter<RuleAdapter.RuleViewHolder>() {
    class RuleViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val rule:TextView = view.findViewById(R.id.rule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rules_layout,parent,false)
        return RuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: RuleViewHolder, position: Int) {
        var rule = lstRules[position]
        holder.rule.text = rule
    }

    override fun getItemCount(): Int {
        return lstRules.size
    }
}