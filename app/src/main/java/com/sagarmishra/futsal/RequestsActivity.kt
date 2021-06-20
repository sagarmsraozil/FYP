package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sagarmishra.futsal.adapter.RequestFragmentAdapter
import com.sagarmishra.futsal.fragments.request_fragments.MyRequestFragment
import com.sagarmishra.futsal.fragments.request_fragments.OpponentRequestFragment
import com.sagarmishra.futsal.fragments.request_fragments.PlayerFragment

class RequestsActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tabLayout:TabLayout
    private lateinit var vp:ViewPager2
    private lateinit var swipe:SwipeRefreshLayout
    var titleAndFragment:MutableMap<String,Fragment> = mutableMapOf(
        "Player Requests" to PlayerFragment(),
        "My Requests" to MyRequestFragment(),
        "Opponent Requests" to OpponentRequestFragment()
    )

    private lateinit var adapter:RequestFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)
        supportActionBar?.hide()

        tabLayout = findViewById(R.id.tabLayout)
        vp = findViewById(R.id.vp)
        swipe = findViewById(R.id.swipe)
        adapter = RequestFragmentAdapter(supportFragmentManager,lifecycle,titleAndFragment.values.toMutableList())
        vp.adapter = adapter
        vp.isUserInputEnabled = true
        swipe.setOnRefreshListener(this)

        TabLayoutMediator(tabLayout,vp){ tab: TabLayout.Tab, i: Int ->
            tab.text = titleAndFragment.keys.toMutableList()[i]
        }.attach()

    }

    override fun onRefresh() {
        val intent = Intent(this,RequestsActivity::class.java)
        startActivity(intent)
        finish()
    }
}