package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sagarmishra.futsal.adapter.ChallengeFragmentAdapter
import com.sagarmishra.futsal.fragments.challenges_and_titles.ChallengeFragment
import com.sagarmishra.futsal.fragments.challenges_and_titles.TitlesFragment

@Suppress("Deprecation")
class ChallengeActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tabLayout:TabLayout
    private lateinit var vp:ViewPager2
    private lateinit var adapter:ChallengeFragmentAdapter
    private lateinit var swipe:SwipeRefreshLayout
    var titleAndFragment:MutableMap<String,Fragment> = mutableMapOf(
        "Challenges" to ChallengeFragment(),
        "Manage Titles" to TitlesFragment()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        supportActionBar?.hide()
        tabLayout = findViewById(R.id.tabLayout)
        vp = findViewById(R.id.vp)
        swipe = findViewById(R.id.swipe)

        adapter = ChallengeFragmentAdapter(supportFragmentManager,lifecycle,titleAndFragment.values.toMutableList())
        vp.adapter = adapter

        vp.isUserInputEnabled = true
        swipe.setOnRefreshListener(this)

        TabLayoutMediator(tabLayout,vp){ tab: TabLayout.Tab, i: Int ->
            tab.text = titleAndFragment.keys.toMutableList()[i]
        }.attach()
    }

    override fun onRefresh() {
        val intent = Intent(this@ChallengeActivity,ChallengeActivity::class.java)
        startActivity(intent)
        finish()
    }
}