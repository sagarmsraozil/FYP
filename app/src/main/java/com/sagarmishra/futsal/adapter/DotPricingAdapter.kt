package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.entityapi.DotsPricing

class DotPricingAdapter(val context:Context,var lstDots:MutableList<DotsPricing>):RecyclerView.Adapter<DotPricingAdapter.DotPricingViewHolder>() {
    class DotPricingViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val tvSN:TextView = view.findViewById(R.id.tvSN)
        val tvFutsal:TextView = view.findViewById(R.id.tvFutsal)
        val tvDate:TextView = view.findViewById(R.id.tvDate)
        val tvTime:TextView = view.findViewById(R.id.tvTime)
        val tvPrice:TextView = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DotPricingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_dots,parent,false)
        return DotPricingViewHolder(view)
    }

    override fun onBindViewHolder(holder: DotPricingViewHolder, position: Int) {
        var dot = lstDots[position]

        holder.tvSN.text = "${position+1}"
        holder.tvFutsal.text = "${dot.futsalInstance_id!!.futsal_id!!.futsalName}"
        holder.tvDate.text = "${dot.futsalInstance_id!!.date}"
        holder.tvTime.text = "${dot.futsalInstance_id!!.time}"
        holder.tvPrice.text = "Rs ${dot.price}"
    }

    override fun getItemCount(): Int {
        return lstDots.size
    }
}