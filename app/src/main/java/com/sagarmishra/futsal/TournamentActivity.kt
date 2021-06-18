package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sagarmishra.futsal.adapter.TournamentFragmentAdapter
import com.sagarmishra.futsal.fragments.match_fragments.TournamentMatchFragment
import com.sagarmishra.futsal.fragments.match_fragments.TournamentResultFragment

class TournamentActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tabLayout:TabLayout
    private lateinit var vp:ViewPager2
    private lateinit var adapter:TournamentFragmentAdapter
    private lateinit var swipe:SwipeRefreshLayout
    var titleAndFragment:MutableMap<String,Fragment> = mutableMapOf(
        "Match" to TournamentMatchFragment(),
        "Result" to TournamentResultFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament)
        supportActionBar?.hide()
        tabLayout = findViewById(R.id.tabLayout)
        vp = findViewById(R.id.vp)
        swipe = findViewById(R.id.swipe)
        adapter = TournamentFragmentAdapter(supportFragmentManager,lifecycle,titleAndFragment.values.toMutableList())
        vp.adapter = adapter

        vp.isUserInputEnabled = true

        TabLayoutMediator(tabLayout,vp)
        { tab: TabLayout.Tab, i: Int ->
            tab.text = titleAndFragment.keys.toMutableList()[i]
        }.attach()

        swipe.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        val intent = Intent(this,TournamentActivity::class.java)
        startActivity(intent)
        finish()
    }
}