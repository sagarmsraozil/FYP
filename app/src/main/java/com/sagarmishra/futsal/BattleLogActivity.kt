package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.BattleFragmentAdapter
import com.sagarmishra.futsal.fragments.match_fragments.BattleMatchFragment
import com.sagarmishra.futsal.fragments.match_fragments.BattleResultFragment
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BattleLogActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tabLayout:TabLayout
    private lateinit var vp:ViewPager2
    private lateinit var adapter:BattleFragmentAdapter
    private lateinit var swipe:SwipeRefreshLayout
    var titleAndFrag:MutableMap<String,Fragment> = mutableMapOf(
        "Upcoming Battles" to BattleMatchFragment(),
        "Results" to BattleResultFragment()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_log)

        tabLayout = findViewById(R.id.tabLayout)
        vp = findViewById(R.id.vp)
        swipe = findViewById(R.id.swipeRefresh)



        adapter = BattleFragmentAdapter(supportFragmentManager,lifecycle,titleAndFrag.values.toMutableList())
        vp.adapter = adapter
        vp.isUserInputEnabled = true

        TabLayoutMediator(tabLayout,vp){ tab: TabLayout.Tab, i: Int ->
            tab.text = titleAndFrag.keys.toMutableList()[i]
        }.attach()

        swipe.setOnRefreshListener(this)

    }

    override fun onRefresh() {
        val intent = Intent(this@BattleLogActivity,BattleLogActivity::class.java)
        startActivity(intent)
        finish()
    }
}