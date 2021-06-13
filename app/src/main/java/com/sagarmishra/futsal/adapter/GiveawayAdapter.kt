package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Giveaway

class GiveawayAdapter(val context:Context,val lstGiveaway:MutableList<Giveaway>):RecyclerView.Adapter<GiveawayAdapter.GiveawayViewholder>() {
    class GiveawayViewholder(val view:View):RecyclerView.ViewHolder(view)
    {
        var ivImage : ImageView
        var tvItem : TextView
        var tvQuantity : TextView

        init {
            ivImage = view.findViewById(R.id.ivImage)
            tvItem = view.findViewById(R.id.tvItem)
            tvQuantity = view.findViewById(R.id.tvQuantity)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiveawayViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item,parent,false)
        return GiveawayViewholder(view)
    }

    override fun onBindViewHolder(holder: GiveawayViewholder, position: Int) {
        var giveaway = lstGiveaway[position]
        var imgPath = RetrofitService.loadImagePath()+giveaway.image!!.replace("\\","/")
        Glide.with(context).load(imgPath).into(holder.ivImage)
        holder.tvItem.text = giveaway.item
        holder.tvQuantity.text = giveaway.quantity.toString()
    }

    override fun getItemCount(): Int {
        return lstGiveaway.size
    }
}