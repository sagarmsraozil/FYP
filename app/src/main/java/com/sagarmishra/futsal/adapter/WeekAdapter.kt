package com.sagarmishra.futsal.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.FutsalRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.db.FutsalRoomDB
import com.sagarmishra.futsal.entityapi.FutsalInstances
import com.sagarmishra.futsal.interfaces.RefreshInstance
import com.sagarmishra.futsal.model.DateAndTime
import com.sagarmishra.futsal.model.StaticData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class WeekAdapter(val context: Context, val lstDateAndDay:MutableList<DateAndTime>,val ref:RefreshInstance):RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {
    var dayChecked = false
    var instanceLoaded = false
    var row_index = -1
    class WeekViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        var tvDate : TextView
        var tvDay:TextView
        var linearLayout:LinearLayout
        init {
            tvDate = view.findViewById(R.id.tvDate)
            tvDay = view.findViewById(R.id.tvDay)
            linearLayout = view.findViewById(R.id.linearLayout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_days,parent,false)
        return WeekViewHolder(view)
    }

    private fun createInstances(holder:WeekViewHolder,position: Int,calendar:DateAndTime)
    {
        if(RetrofitService.online == false)
        {
            CoroutineScope(Dispatchers.IO).launch {
                var lstInstances = FutsalRoomDB.getFutsalDBInstance(context).getInstancesDAO().retrieveData(calendar.day!!,calendar.date2!!,StaticData.futsalId)
                if(lstInstances.size > 0)
                {
                    withContext(Dispatchers.Main)
                    {
                        ref.refreshInstances(position,lstInstances)
                    }
                }
                else
                {
                    var lstInstances:MutableList<FutsalInstances> = mutableListOf()
                    withContext(Dispatchers.Main)
                    {
                        ref.refreshInstances(position,lstInstances)
                        var snk = Snackbar.make(holder.linearLayout,"No Data to Fetch!!",Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("OK",View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }
                }
            }
        }
        else
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = FutsalRepository()
                    val response = repo.retrieveBookingInstance(StaticData.futsalId,calendar.day!!,calendar.date2!!)
                    if(response.success == true)
                    {
                        var lstInstances:MutableList<FutsalInstances> = response.data!!
                        val instance = FutsalRoomDB.getFutsalDBInstance(context)
                        instance.getInstancesDAO().deleteAll(StaticData.getToday())
                        instance.getInstancesDAO().addFutsalTimeInstance(lstInstances)
                        StaticData.remnant = response.remnant
                        StaticData.markDetails = response.markDetails
                        StaticData.bookTimes = response.bookTimes
                        withContext(Dispatchers.Main)
                        {
                            ref.refreshInstances(position,lstInstances)
                        }

                    }
                    else
                    {
                        var snk = Snackbar.make(holder.linearLayout,"${response.message}",Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("OK",View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }
                }
                catch (ex:Exception)
                {
                    withContext(Dispatchers.Main)
                    {

                        var snk = Snackbar.make(holder.linearLayout,"Make sure you have stable connection!!",Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("OK",View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                        println(ex.printStackTrace())

                    }


//                val pref = context.getSharedPreferences("credentials",Context.MODE_PRIVATE)
//                val username = pref.getString("username","")

                }
            }
        }

    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        var day = lstDateAndDay[position]
        if(!instanceLoaded)
        {
            createInstances(holder,position,day)
            instanceLoaded = true
        }
        holder.tvDate.text = day.date1
        holder.tvDay.text = day.day

        holder.linearLayout.setOnClickListener {
            row_index = position
            notifyDataSetChanged()
            createInstances(holder,position,day)
        }

        if(!dayChecked)
        {
            if(position == 0)
            {
                holder.linearLayout.setBackgroundResource(R.drawable.bg_week2)
                holder.tvDate.setTextColor(Color.parseColor("#FAF7FE"))
                holder.tvDay.setTextColor(Color.parseColor("#FAF7FE"))
            }
            dayChecked = true
        }
        else
        {
            if(row_index == position)
            {
                holder.linearLayout.setBackgroundResource(R.drawable.bg_week2)
                holder.tvDate.setTextColor(Color.parseColor("#FAF7FE"))
                holder.tvDay.setTextColor(Color.parseColor("#FAF7FE"))
            }
            else
            {
                holder.linearLayout.setBackgroundResource(R.drawable.bg_week)
                holder.tvDate.setTextColor(Color.parseColor("#807676"))
                holder.tvDay.setTextColor(Color.parseColor("#0288D1"))
            }
        }
    }

    override fun getItemCount(): Int {
        return lstDateAndDay.size
    }
}