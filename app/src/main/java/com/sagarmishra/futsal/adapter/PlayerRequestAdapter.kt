package com.sagarmishra.futsal.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.RequestRepository
import com.sagarmishra.futsal.entityapi.RequestModel
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class PlayerRequestAdapter(val activity:Activity,val context:Context,var lstRequests:MutableList<RequestModel>):RecyclerView.Adapter<PlayerRequestAdapter.PlayerRequestViewHolder>() {
    class PlayerRequestViewHolder(val view:View,activity: Activity):RecyclerView.ViewHolder(view)
    {
        val tvPlayerName:TextView = view.findViewById(R.id.tvPlayerName)
        val tvEmail:TextView = view.findViewById(R.id.tvEmail)
        val tvPhone:TextView = view.findViewById(R.id.tvPhone)
        val btnAccept:Button = view.findViewById(R.id.btnAccept)
        val btnDecline:Button = view.findViewById(R.id.btnDecline)
        val tvRequests:TextView = activity.findViewById(R.id.tvRequests)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_requests_layout,parent,false)
        return PlayerRequestViewHolder(view,activity)
    }

    private fun requestResponse(holder:PlayerRequestViewHolder,decision:String,id:String,position: Int)
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = RequestRepository()
                val response = repo.respondToPlayerRequest(decision,id)
                if(response.success == true)
                {
                    withContext(Dispatchers.Main)
                    {
                        holder.tvPhone.snackbar("${response.message}")
                        lstRequests.removeAt(position)
                        notifyDataSetChanged()
                        holder.tvRequests.text = "${lstRequests.size} requests"
                    }
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        holder.tvPhone.snackbar("${response.message}")
                    }
                }
            }
            catch (err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    holder.tvPhone.snackbar("Server Error")
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PlayerRequestViewHolder, position: Int) {
        var request = lstRequests[position]
        holder.tvPlayerName.text = request.user_id!!.firstName+" "+request.user_id!!.lastName+" (${request.user_id!!.userName})"
        holder.tvEmail.text = request.user_id!!.email
        holder.tvPhone.text = request.user_id!!.mobileNumber

        holder.btnAccept.setOnClickListener {
            requestResponse(holder,"Accept",request._id,position)
        }

        holder.btnDecline.setOnClickListener {
            requestResponse(holder,"Decline",request._id,position)
        }

    }

    override fun getItemCount(): Int {
        return lstRequests.size
    }
}