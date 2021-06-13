package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.MyBookingDetailsActivity
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.databinding.MyExpiredBookingLayoutBinding
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExpiredBookingAdapter(val context:Context,var lstBooking:MutableList<Booking>):RecyclerView.Adapter<ExpiredBookingAdapter.ExpiredBookingViewHolder>() {
    class ExpiredBookingViewHolder(val myExpiredBookingLayoutBinding: MyExpiredBookingLayoutBinding):RecyclerView.ViewHolder(myExpiredBookingLayoutBinding.root)
    {
        var expiry = myExpiredBookingLayoutBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpiredBookingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val ev = MyExpiredBookingLayoutBinding.inflate(inflater,parent,false)
        return ExpiredBookingViewHolder(ev)
    }

    override fun onBindViewHolder(holder: ExpiredBookingViewHolder, position: Int) {
        var booking = lstBooking[position]
        holder.expiry.expired = booking

        if(RetrofitService.online == false)
        {
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                holder.expiry.tvDate.text = booking.offlineDate
                holder.expiry.tvTime.text = booking.offlineTime
                holder.expiry.tvFutsal.text = booking.offlineFutsalName
            }

        }

        holder.expiry.tvDetails.setOnClickListener {
            if(RetrofitService.online == true)
            {
                val intent = Intent(context,MyBookingDetailsActivity::class.java)
                intent.putExtra("record",booking._id)
                context.startActivity(intent)
            }
            else
            {
                holder.expiry.cvImage.snackbar("No Internet connection!!")
            }

        }
    }

    override fun getItemCount(): Int {
        return lstBooking.size
    }
}