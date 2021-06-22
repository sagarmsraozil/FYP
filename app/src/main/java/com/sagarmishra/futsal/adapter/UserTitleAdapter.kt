package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.interfaces.RadioRefresh
import com.sagarmishra.futsal.model.StaticData

class UserTitleAdapter(val context:Context,var title:MutableList<String>,val radioRfr:RadioRefresh):RecyclerView.Adapter<UserTitleAdapter.UserTitleViewHolder>() {
    var latestChecked:RadioButton? = null
    var selected = title.indexOf(StaticData.user!!.activeTitle)
    class UserTitleViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val rbRadio:RadioButton = view.findViewById(R.id.rbRadio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTitleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_radio_groups,parent,false)
        return UserTitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserTitleViewHolder, position: Int) {
        var myTitle = title[position]

        if(myTitle == "")
        {
            holder.rbRadio.text = "None"
        }
        else
        {
            holder.rbRadio.text = myTitle
        }


        holder.rbRadio.setOnClickListener {
            if(selected != position)
            {
                latestChecked!!.isChecked = false
                selected = position
                latestChecked = holder.rbRadio
                latestChecked!!.isChecked = true
                radioRfr.refreshRadio(position)
            }
        }


        if(selected == position)
        {
            holder.rbRadio.isChecked = true
            latestChecked = holder.rbRadio
        }
        else
        {
            holder.rbRadio.isChecked = false
        }

    }

    override fun getItemCount(): Int {
        return title.size
    }
}