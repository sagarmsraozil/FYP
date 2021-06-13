package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.databinding.OfflineExpiredBinding
import com.sagarmishra.futsal.entityapi.BookingRoomDB
import com.sagarmishra.futsal.utils.snackbar

class OfflineExpiredAdapter(val context:Context, val lstBookings:MutableList<BookingRoomDB>):RecyclerView.Adapter<OfflineExpiredAdapter.OfflineExpiredViewHolder>() {
    class OfflineExpiredViewHolder(val offlineExpiredBinding: OfflineExpiredBinding):RecyclerView.ViewHolder(offlineExpiredBinding.root)
    {
        var offlineBinding = offlineExpiredBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfflineExpiredViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val ov = OfflineExpiredBinding.inflate(inflater,parent,false)
        return OfflineExpiredViewHolder(ov)
    }

    override fun onBindViewHolder(holder: OfflineExpiredViewHolder, position: Int) {
        var book = lstBookings[position]
        holder.offlineBinding.expired = book
        holder.offlineBinding.tvDetails.setOnClickListener {
            holder.offlineBinding.tvDetails.snackbar("No Internet Connection")
        }
    }

    override fun getItemCount(): Int {
        return lstBookings.size
    }


}