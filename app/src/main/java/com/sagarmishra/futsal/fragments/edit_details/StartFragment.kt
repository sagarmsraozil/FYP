package com.sagarmishra.futsal.fragments.edit_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.adapter.DetailsAdapter


class StartFragment : Fragment() {
    private lateinit var vp:ViewPager2
    private lateinit var tab:TabLayout
    var fragList:MutableMap<String,Fragment> = mutableMapOf(
        "Details" to DetailsFragment(),
        "Profile Picture" to ChangeImageFragment(),
        "Password" to ChangePasswordFragment()
    )
    private lateinit var adapter:DetailsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_start, container, false)
        vp = view.findViewById(R.id.vp)
        tab = view.findViewById(R.id.tab)
        adapter = DetailsAdapter(requireActivity().supportFragmentManager,lifecycle,fragList.values.toMutableList())
        vp.adapter = adapter
        TabLayoutMediator(tab,vp){ tab: TabLayout.Tab, i: Int ->
            tab.text = fragList.keys.toMutableList()[i]
        }.attach()
        return view
    }



}