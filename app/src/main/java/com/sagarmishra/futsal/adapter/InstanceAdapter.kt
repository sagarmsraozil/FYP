package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.BookingActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.BookingRepository
import com.sagarmishra.futsal.Repository.FutsalRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.databinding.FutsalInstancesLayoutBinding
import com.sagarmishra.futsal.entityapi.FutsalInstances
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class InstanceAdapter(val context:Context,var lstFutsalInstance:MutableList<FutsalInstances>):RecyclerView.Adapter<InstanceAdapter.InstanceViewHolder>() {
    class InstanceViewHolder(futsalInstances:FutsalInstancesLayoutBinding):RecyclerView.ViewHolder(futsalInstances.root)
    {
        var instance = futsalInstances
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstanceViewHolder {
       var inflater = LayoutInflater.from(parent.context)
        var ib = FutsalInstancesLayoutBinding.inflate(inflater,parent,false)
        return InstanceViewHolder(ib)
    }

    override fun onBindViewHolder(holder: InstanceViewHolder, position: Int) {
        var fInstance = lstFutsalInstance[position]
        holder.instance.instance = fInstance
        holder.instance.cbMark.isEnabled = StaticData.remnant!!.contains(fInstance._id)

        //checking whether the instance is marked or not.
        var marked = false;
        if(StaticData.markDetails!![fInstance._id]!!.contains(StaticData.user!!._id))
        {
            marked = true;
            holder.instance.cbMark.isChecked = true
        }

        holder.instance.cbMark.setOnClickListener {
            if(StaticData.remnant!!.contains(fInstance._id))
            {
                if(marked)
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = BookingRepository()
                            val response = repo.removeMark(fInstance._id)
                            if(response.success == true)
                            {
                                withContext(Dispatchers.Main)
                                {
                                    holder.instance.cbMark.snackbar("${response.message}")
                                    holder.instance.cbMark.isChecked = false
                                    StaticData.markDetails!![fInstance._id]!!.remove(StaticData.user!!._id)
                                    marked = false
                                    holder.instance.tvMarkeds.text = StaticData.markDetails!![fInstance._id]!!.size.toString()
                                }
                            }
                            else
                            {
                                withContext(Dispatchers.Main)
                                {
                                    holder.instance.cbMark.snackbar("${response.message}")
                                    holder.instance.cbMark.isChecked = true

                                }
                            }
                        }
                        catch(err:Exception)
                        {
                            println(err.printStackTrace())
                            withContext(Dispatchers.Main)
                            {
                                holder.instance.cbMark.snackbar("Server Error")
                            }
                        }

                    }
                }
                else
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = BookingRepository()
                            val response = repo.markInstance(fInstance._id)
                            if(response.success == true)
                            {
                                withContext(Dispatchers.Main)
                                {
                                    holder.instance.cbMark.snackbar("${response.message}")
                                    holder.instance.cbMark.isChecked = true
                                    StaticData.markDetails!![fInstance._id]!!.add(StaticData.user!!._id)
                                    marked = true
                                    holder.instance.tvMarkeds.text = StaticData.markDetails!![fInstance._id]!!.size.toString()
                                }

                            }
                            else
                            {
                                withContext(Dispatchers.Main)
                                {
                                    holder.instance.cbMark.snackbar("${response.message}")
                                    holder.instance.cbMark.isChecked = false

                                }
                            }
                        }
                        catch(err:Exception)
                        {
                            println(err.printStackTrace())
                            withContext(Dispatchers.Main)
                            {
                                holder.instance.cbMark.snackbar("Server Error")
                            }
                        }
                    }
                }
            }
        }

       holder.instance.tvMarkeds.text = StaticData.markDetails!![fInstance._id]!!.size.toString()

       if(StaticData.bookTimes != null)
       {
           if(StaticData.bookTimes!![fInstance._id] != null)
           {
               holder.instance.ivBook.visibility = View.VISIBLE
               holder.instance.tvBookedAt.visibility = View.VISIBLE
               holder.instance.tvBookedAt.text = StaticData.bookTimes!![fInstance._id]
           }
           else
           {
               holder.instance.ivBook.visibility = View.GONE
               holder.instance.tvBookedAt.visibility = View.GONE
           }
       }


        if(fInstance.available == false)
        {
              holder.instance.available.setBackgroundResource(R.drawable.bg_available)
        }
        else
        {
            holder.instance.available.setBackgroundResource(R.drawable.bg_unavailable)
        }
        if(fInstance.superPrice > 0)
        {
            holder.instance.tvPrice.text = "Rs ${fInstance.superPrice}"
            holder.instance.superDay.setBackgroundResource(R.drawable.bg_unavailable)
        }

        holder.instance.btnBook.setOnClickListener {
            if(RetrofitService.online == false)
            {
                val snk = Snackbar.make(holder.instance.timeLayout,"Please connect to internet for further proceeding.",Snackbar.LENGTH_INDEFINITE)
                snk.setAction("Ok",View.OnClickListener {
                    snk.dismiss()
                })
                snk.show()
            }
            else
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = FutsalRepository()
                        val response = repo.checkTimeInstanceAvailability(fInstance._id)
                        if(response.success == true)
                        {
                            val intent = Intent(context,BookingActivity::class.java)
                            intent.putExtra("timeInstance",fInstance)
                            context.startActivity(intent)
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                val snk = Snackbar.make(holder.instance.timeLayout,"${response.message}",Snackbar.LENGTH_INDEFINITE)
                                snk.setAction("OK",View.OnClickListener {
                                    snk.dismiss()
                                })
                                snk.show()
                            }
                        }
                    }
                    catch (ex:Exception)
                    {
                        withContext(Dispatchers.Main)
                        {
                            val snk = Snackbar.make(holder.instance.timeLayout,"Sorry! We are having some problem.",Snackbar.LENGTH_INDEFINITE)
                            snk.setAction("OK",View.OnClickListener {
                                snk.dismiss()
                            })
                            println(ex.printStackTrace())
                            snk.show()
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lstFutsalInstance.size
    }

}