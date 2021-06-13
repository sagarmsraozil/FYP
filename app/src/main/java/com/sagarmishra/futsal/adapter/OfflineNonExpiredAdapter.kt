package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.databinding.OfflineExpiredBinding
import com.sagarmishra.futsal.databinding.OfflineNonExpiredBinding
import com.sagarmishra.futsal.entityapi.BookingRoomDB
import com.sagarmishra.futsal.utils.snackbar

class OfflineNonExpiredAdapter(val context:Context,val lstBooking:MutableList<BookingRoomDB>):RecyclerView.Adapter<OfflineNonExpiredAdapter.OfflineNonExpiredViewHolder>() {
    class OfflineNonExpiredViewHolder(val offlineNonExpiredBinding: OfflineNonExpiredBinding): RecyclerView.ViewHolder(offlineNonExpiredBinding.root)
    {
        var offlineBinding = offlineNonExpiredBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfflineNonExpiredViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val ov = OfflineNonExpiredBinding.inflate(inflater,parent,false)
        return OfflineNonExpiredViewHolder(ov)
    }

    override fun onBindViewHolder(holder: OfflineNonExpiredViewHolder, position: Int) {
        val book = lstBooking[position]
        holder.offlineBinding.booking = book
        holder.offlineBinding.tvDetails.setOnClickListener {
            holder.offlineBinding.tvDetails.snackbar("No Internet Connection")
        }
        holder.offlineBinding.btnDelete.setOnClickListener {
            holder.offlineBinding.tvDetails.snackbar("No Internet Connection")
        }
    }

    override fun getItemCount(): Int {
        return lstBooking.size
    }

}