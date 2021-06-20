package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sagarmishra.futsal.adapter.TeamUpdateFragmentAdapter
import com.sagarmishra.futsal.fragments.team_update.TeamDetailsUpdateFragment
import com.sagarmishra.futsal.fragments.team_update.TeamLogoUpdaeFragment

class EditTeamDetailsActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var swipe:SwipeRefreshLayout
    private lateinit var tabLayout:TabLayout
    private lateinit var vp:ViewPager2
    private lateinit var adapter: TeamUpdateFragmentAdapter
    var titleAndFragment:MutableMap<String,Fragment> = mutableMapOf(
        "Edit Details" to TeamDetailsUpdateFragment(),
        "Edit Logo" to TeamLogoUpdaeFragment()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_team_details)
        supportActionBar?.hide()
        tabLayout = findViewById(R.id.tabLayout)
        vp = findViewById(R.id.vp)
        swipe = findViewById(R.id.swipe)

        adapter = TeamUpdateFragmentAdapter(supportFragmentManager,lifecycle,titleAndFragment.values.toMutableList())
        vp.adapter = adapter
        swipe.setOnRefreshListener(this)

        vp.isUserInputEnabled = true

        TabLayoutMediator(tabLayout,vp)
        { tab: TabLayout.Tab, i: Int ->
            tab.text = titleAndFragment.keys.toMutableList()[i]
        }.attach()
    }

    override fun onRefresh() {
        val intent = Intent(this,EditTeamDetailsActivity::class.java)
        startActivity(intent)
        finish()
    }
}