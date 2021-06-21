package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R

class TitleAdapter(val context:Context,var lstTitles:MutableList<String>):RecyclerView.Adapter<TitleAdapter.TitleViewHolder>() {
    class TitleViewHolder(val view: View):RecyclerView.ViewHolder(view)
    {
        val tvTitle:TextView = view.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_titles_browser,parent,false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        var title = lstTitles[position]
        holder.tvTitle.text = title
    }

    override fun getItemCount(): Int {
        return lstTitles.size
    }
}