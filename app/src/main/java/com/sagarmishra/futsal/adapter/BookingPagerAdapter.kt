package com.sagarmishra.futsal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class BookingPagerAdapter(val fragmentManager: FragmentManager,val lifecycle: Lifecycle,var lstFragments:MutableList<Fragment>):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return lstFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return lstFragments[position]
    }
}