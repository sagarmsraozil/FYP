package com.sagarmishra.futsal.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.GiveawayActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.GiveawayRepository
import com.sagarmishra.futsal.entityapi.Giveaway
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class GiveawayWrapperAdapter(val activity:Activity,val context:Context,val lstCode:MutableList<String>):RecyclerView.Adapter<GiveawayWrapperAdapter.GiveawayWrapperViewHolder>() {
    var lstGiveawayData : MutableList<Giveaway> = mutableListOf()
    var lstParticipated : MutableList<String> = mutableListOf()
    var giveawayDetail : MutableMap<String,MutableList<Any>> = mutableMapOf()
    private lateinit var adapter : GiveawayAdapter
    class GiveawayWrapperViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val giveaway:TextView
        val recycler:RecyclerView
        val tvStatus : TextView
        val tvParticipate : TextView
        val tvEnds : TextView
        val tvResult : TextView
        val btnParticipate : Button
        val btnParticipated : Button
        init {
            giveaway = view.findViewById(R.id.giveaway)
            recycler = view.findViewById(R.id.recycler)
            tvStatus = view.findViewById(R.id.tvStatus)
            tvParticipate = view.findViewById(R.id.tvParticipate)
            tvEnds = view.findViewById(R.id.tvEnds)
            tvResult = view.findViewById(R.id.tvResult)
            btnParticipate = view.findViewById(R.id.btnParticipate)
            btnParticipated = view.findViewById(R.id.btnParticipated)
        }
    }

    private fun mapData()
    {
        for(i in lstGiveawayData)
        {
            giveawayDetail[i.giveAwayCode!!] = mutableListOf(i.futsal_id!!.futsalName!!,i.startingFrom!!,i.endAt!!,i.resultAt!!,i.status!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiveawayWrapperViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.giveaway_wrapper,parent,false)
        return GiveawayWrapperViewHolder(view)
    }

    override fun onBindViewHolder(holder: GiveawayWrapperViewHolder, position: Int) {
        var code = lstCode[position]

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = GiveawayRepository()
                val response = repo.getMyParticipant()
                if(response.success == true)
                {
                    lstParticipated = response.data!!

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        holder.tvResult.snackbar(response.message!!)
                    }

                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    holder.tvResult.snackbar(ex.toString())
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = GiveawayRepository()
                val response = repo.getGiveaway()
                if(response.success == true)
                {
                    lstGiveawayData = response.data!!
                    mapData()
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        holder.tvResult.snackbar(response.message!!)
                    }

                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    holder.tvResult.snackbar(ex.toString())
                }
            }
        }
        Thread.sleep(2000)
        var list : MutableList<Giveaway> = mutableListOf()
        for(i in lstGiveawayData)
        {
            if(i.giveAwayCode == code)
            {
                list.add(i)
            }
        }
        if(list.size > 0)
        {
            adapter = GiveawayAdapter(context,list)
            holder.recycler.adapter = adapter
            holder.recycler.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
        holder.tvStatus.text = giveawayDetail[code]!![4].toString()
        holder.tvResult.text = giveawayDetail[code]!![3].toString()
        holder.tvEnds.text = giveawayDetail[code]!![2].toString()
        holder.tvParticipate.text = giveawayDetail[code]!![1].toString()
        holder.giveaway.text = "Giveaway from ${giveawayDetail[code]!![0]}"

        if(lstParticipated.contains(code))
        {
            holder.btnParticipated.visibility = View.VISIBLE
            holder.btnParticipate.visibility = View.GONE
        }
        else
        {
            holder.btnParticipated.visibility = View.GONE
            holder.btnParticipate.visibility = View.VISIBLE
        }

        holder.btnParticipate.isEnabled = holder.tvStatus.text == "Ongoing"

        holder.btnParticipate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = GiveawayRepository()
                    val response = repo.participateGiveaway(code)
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            holder.recycler.snackbar(response.message!!)
                            holder.btnParticipate.visibility = View.GONE
                            holder.btnParticipated.visibility = View.VISIBLE
//                            var intent = Intent(context,GiveawayActivity::class.java)
//                            context.startActivity(intent)
//                            activity.finish()
                        }

                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            holder.recycler.snackbar(response.message!!)
                        }
                    }
                }
                catch (ex:Exception)
                {
                    println(ex.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        holder.tvResult.snackbar(ex.toString())
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return lstCode.size
    }
}