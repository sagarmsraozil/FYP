package com.sagarmishra.futsal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SingleTournamentFragmentAdapter(val fragmentManager: FragmentManager,val lifecycle: Lifecycle,var lstMatches:MutableList<Fragment>):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return lstMatches.size
    }

    override fun createFragment(position: Int): Fragment {
        return lstMatches[position]
    }
}