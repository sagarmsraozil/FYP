package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R

class WarningDotAdapter(val context:Context,val count:Int):RecyclerView.Adapter<WarningDotAdapter.WarningDotViewHolder>() {
    class WarningDotViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        var linearLayout : LinearLayout = view.findViewById(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarningDotViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.dot_layout,parent,false)
        return WarningDotViewHolder(view)
    }

    override fun onBindViewHolder(holder: WarningDotViewHolder, position: Int) {
       holder.linearLayout.setOnClickListener {
           Toast.makeText(context, "${position+1}", Toast.LENGTH_SHORT).show()
       }
    }

    override fun getItemCount(): Int {
        return count
    }
}