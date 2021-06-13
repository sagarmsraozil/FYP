package com.sagarmishra.futsalarenawearos.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsalarenawearos.databinding.LayoutBookingsBinding
import com.sagarmishra.futsalarenawearos.entity.Booking

class BookingAdapter(val context:Context,var lstBooking:MutableList<Booking>):RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(val layoutBookingsBinding: LayoutBookingsBinding):RecyclerView.ViewHolder(layoutBookingsBinding.root){
        var bookingBind = layoutBookingsBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var bb = LayoutBookingsBinding.inflate(inflater,parent,false)
        return BookingViewHolder(bb)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        var booking = lstBooking[position]
        holder.bookingBind.futsal = booking
        if(booking.expired == true)
        {
            holder.bookingBind.bgExpiry.setBackgroundColor(Color.parseColor("#F64949"))
        }
    }

    override fun getItemCount(): Int {
        return lstBooking.size
    }
}