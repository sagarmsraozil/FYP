package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.FutsalInstancesActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.model.StaticData

class NearbyFutsalAdapter(val context:Context,var futsals:MutableList<Futsal>):RecyclerView.Adapter<NearbyFutsalAdapter.NearbyFutsalViewHolder>() {
    class NearbyFutsalViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val layoutFutsal:LinearLayout = view.findViewById(R.id.layoutFutsal)
        val tvFutsalName:TextView = view.findViewById(R.id.tvFutsalName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyFutsalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nearby_futsals_layout,parent,false)
        return NearbyFutsalViewHolder(view)
    }

    override fun onBindViewHolder(holder: NearbyFutsalViewHolder, position: Int) {
        var futsal = futsals[position]
        holder.tvFutsalName.text = futsal.futsalName
        holder.layoutFutsal.setOnClickListener {
            val intent = Intent(context, FutsalInstancesActivity::class.java)
            StaticData.futsalId = futsal._id
            intent.putExtra("futsal",futsal)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return futsals.size
    }
}