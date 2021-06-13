package com.sagarmishra.futsal.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.MyBookingDetailsActivity
import com.sagarmishra.futsal.Repository.BookingRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.databinding.MyBookingsLayoutBinding
import com.sagarmishra.futsal.db.FutsalRoomDB
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.*

class MyBookingsAdapter(val context: Context, var lstBookings:MutableList<Booking>):RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder>() {
    class MyBookingsViewHolder(val myBookingsLayoutBinding: MyBookingsLayoutBinding):RecyclerView.ViewHolder(myBookingsLayoutBinding.root)
    {
        var myBookings = myBookingsLayoutBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val bb = MyBookingsLayoutBinding.inflate(inflater,parent,false)
        return MyBookingsViewHolder(bb)
    }

    override fun onBindViewHolder(holder: MyBookingsViewHolder, position: Int) {
        var booking = lstBookings[position]
        holder.myBookings.booking = booking
        if(RetrofitService.online == false) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)


                holder.myBookings.tvFutsal.text = booking.offlineFutsalName
                holder.myBookings.tvTime.text = booking.offlineTime
                holder.myBookings.tvDay.text = booking.offlineDay
                holder.myBookings.tvDate.text = booking.offlineDate
                holder.myBookings.tvPrice.text = "Rs "+booking.offlinePrice.toString()


            }
        }
        holder.myBookings.tvDetails.setOnClickListener {
            if(RetrofitService.online == true)
            {
                val intent = Intent(context,MyBookingDetailsActivity::class.java)
                intent.putExtra("record",booking._id)
                context.startActivity(intent)
            }
            else
            {
                holder.myBookings.ivImage.snackbar("No Internet Connection")
            }
        }

        if( StaticData.markAndInstance!![booking.futsalInstance_id!!._id] != null)
        {
            holder.myBookings.tvMarks.text = StaticData.markAndInstance!![booking.futsalInstance_id!!._id].toString()
        }

        holder.myBookings.btnDelete.setOnClickListener {
            if(RetrofitService.online == true)
            {
                var alert = AlertDialog.Builder(context)
                alert.setTitle("Delete Booked Instance.")
                alert.setMessage("Do you really want to delete your bookings?")
                alert.setPositiveButton("Yes")
                { dialogInterface: DialogInterface, i: Int ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = BookingRepository()
                            val response = repo.deleteBooking(booking._id)
                            if(response.success == true)
                            {
                                val instance = FutsalRoomDB.getFutsalDBInstance(context)
                                instance.getBookingDAO().deleteBooking(booking._id)
                                withContext(Dispatchers.Main)
                                {
                                    lstBookings.removeAt(position)
                                    notifyDataSetChanged()
                                    holder.myBookings.ivImage.snackbar("${response.message}")
                                }

                            }
                            else
                            {
                                withContext(Dispatchers.Main)
                                {
                                    holder.myBookings.ivImage.snackbar("${response.message}")
                                }
                            }
                        }
                        catch (ex:Exception)
                        {
                            withContext(Dispatchers.Main)
                            {
                                holder.myBookings.ivImage.snackbar("Sorry! We are having some problem!!")
                                println(ex.printStackTrace())
                            }

                        }
                    }
                }
                alert.setNeutralButton("No")
                { dialogInterface: DialogInterface, i: Int ->

                }

                var dialog = alert.create()
                dialog.setCancelable(false)
                dialog.show()




            }
            else
            {
                holder.myBookings.ivImage.snackbar("No Internet Connection")
            }
        }
    }

    override fun getItemCount(): Int {
        return lstBookings.size
    }


}