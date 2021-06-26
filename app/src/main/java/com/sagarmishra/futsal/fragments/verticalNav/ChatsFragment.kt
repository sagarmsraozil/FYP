package com.sagarmishra.futsal.fragments.verticalNav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.adapter.ChatFragmentAdapter
import com.sagarmishra.futsal.fragments.chatFragments.AvailableFragment
import com.sagarmishra.futsal.fragments.chatFragments.ChattingFragment


class ChatsFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tabLayout:TabLayout
    private lateinit var vp:ViewPager2
    private lateinit var adapter:ChatFragmentAdapter
    private lateinit var swipe:SwipeRefreshLayout
    var titleAndFragment:MutableMap<String,Fragment> = mutableMapOf(
        "Chats" to ChattingFragment(),
        "Available Players" to AvailableFragment()
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_chat2, container, false)
        tabLayout = view.findViewById(R.id.tabLayout)
        vp = view.findViewById(R.id.vp)
        swipe = view.findViewById(R.id.swipe)

        swipe.setOnRefreshListener(this)

        adapter = ChatFragmentAdapter(requireActivity().supportFragmentManager,lifecycle,titleAndFragment.values.toMutableList())
        vp.adapter = adapter
        vp.isUserInputEnabled = true

        TabLayoutMediator(tabLayout,vp)
        { tab: TabLayout.Tab, i: Int ->
            tab.text = titleAndFragment.keys.toMutableList()[i]
        }.attach()

        return view
    }

    override fun onRefresh() {
        val intent = Intent(requireContext(),MainActivity::class.java)
        intent.putExtra("FRAGMENT_NUMBER",10)
        startActivity(intent)
    }


}