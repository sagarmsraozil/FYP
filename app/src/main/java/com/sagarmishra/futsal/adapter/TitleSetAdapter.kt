package com.sagarmishra.futsal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.interfaces.RadioRefresh
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar

class TitleSetAdapter(val context:Context,var lstTitle:MutableList<String>,val radioRfr:RadioRefresh):RecyclerView.Adapter<TitleSetAdapter.TitleSetViewHolder>() {
    var activeTitle = StaticData.team!!.activeTitle
    var selected = lstTitle.indexOf(activeTitle)
    var lastChecked:RadioButton? =null
    class TitleSetViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val layoutLinear: LinearLayout = view.findViewById(R.id.layoutLinear)
        val rbRadio : RadioButton = view.findViewById(R.id.rbRadio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleSetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_radio_groups,parent,false)
        return TitleSetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleSetViewHolder, position: Int) {

        var title = lstTitle[position]

        if(title == "")
        {
            holder.rbRadio.text = "None"
        }
        else
        {
            holder.rbRadio.text = title
        }


        holder.rbRadio.setOnClickListener {

            if(selected != position)
            {
                lastChecked!!.isChecked = false

                holder.rbRadio.isChecked = true
                selected = position
                lastChecked = holder.rbRadio
                radioRfr.refreshRadio(position)
            }

        }



        if(selected == position)
        {
            holder.rbRadio.isChecked = true
            lastChecked = holder.rbRadio
        }
        else
        {
            holder.rbRadio.isChecked = false
        }

    }

    override fun getItemCount(): Int {
        return lstTitle.size
    }


}