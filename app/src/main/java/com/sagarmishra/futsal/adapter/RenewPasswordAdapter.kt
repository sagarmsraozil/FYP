package com.sagarmishra.futsal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class RenewPasswordAdapter(val frag:FragmentManager,val lifecycle:Lifecycle,val lstFrag:MutableList<Fragment>):FragmentStateAdapter(frag,lifecycle) {
    override fun getItemCount(): Int {
        return lstFrag.size
    }

    override fun createFragment(position: Int): Fragment {
       return lstFrag[position]
    }
}