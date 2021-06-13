package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.FutsalInstancesActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.databinding.FutsalLayoutBinding
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.model.StaticData

class FutsalAdapter(val context: Context,var lstFutsal:MutableList<Futsal>):RecyclerView.Adapter<FutsalAdapter.FutsalViewholder>() {

    class FutsalViewholder(val futsalLayoutBinding: FutsalLayoutBinding):RecyclerView.ViewHolder(futsalLayoutBinding.root)
    {
        var futsalBinding = futsalLayoutBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutsalViewholder {
       val inflater = LayoutInflater.from(parent.context)
       val fb = FutsalLayoutBinding.inflate(inflater,parent,false)
       return FutsalViewholder(fb)
    }

    override fun onBindViewHolder(holder: FutsalViewholder, position: Int) {
        var futsal = lstFutsal[position]

        holder.futsalBinding.futsal = futsal

        if(position % 2 == 0)
        {
            holder.futsalBinding.constraintFutsal.setBackgroundResource(R.drawable.bg_futsal)
        }
        else
        {
            holder.futsalBinding.constraintFutsal.setBackgroundResource(R.drawable.bg_futsal)
        }

        holder.futsalBinding.btnBook.setOnClickListener {
            if(RetrofitService.online == true)
            {
                val intent = Intent(context,FutsalInstancesActivity::class.java)
                StaticData.futsalId = futsal._id
                intent.putExtra("futsal",futsal)
                context.startActivity(intent)
            }
            else
            {
                val snk = Snackbar.make(holder.futsalBinding.linearLayout,"Please connect to internet for further proceeding.",
                    Snackbar.LENGTH_INDEFINITE)
                snk.setAction("Ok", View.OnClickListener {
                    snk.dismiss()
                })
                snk.show()
            }

        }
    }

    override fun getItemCount(): Int {
       return lstFutsal.size
    }


}