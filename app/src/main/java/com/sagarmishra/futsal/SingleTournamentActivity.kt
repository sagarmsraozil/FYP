package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sagarmishra.futsal.Repository.TournamentRepository
import com.sagarmishra.futsal.adapter.ParticipatedTeamAdapter
import com.sagarmishra.futsal.adapter.SingleTournamentFragmentAdapter
import com.sagarmishra.futsal.fragments.tournament_screens.ResultFragment
import com.sagarmishra.futsal.fragments.tournament_screens.RoundsFragment
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

@Suppress("Deprecation")
class SingleTournamentActivity : AppCompatActivity(),View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tvTournamentName:TextView
    private lateinit var btnPlayed:Button
    private lateinit var btnPlaying:Button
    private lateinit var btnRemove:Button
    private lateinit var btnParticipate:Button
    private lateinit var btnClosed:Button
    private lateinit var btnOpeningSoon:Button
    private lateinit var btnFinished:Button
    private lateinit var btnIneligible:Button
    private lateinit var tvParticipation:TextView
    private lateinit var tvAge:TextView
    private lateinit var tvEvent:TextView
    private lateinit var tvSponsors:TextView
    private lateinit var tvOrganizedBy:TextView
    private lateinit var tvAbout:TextView
    private lateinit var tvPrize:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var tabLayout:TabLayout
    private lateinit var vp:ViewPager2
    private lateinit var adapter:ParticipatedTeamAdapter
    private lateinit var vpAdapter:SingleTournamentFragmentAdapter
    private lateinit var swipeRefresh:SwipeRefreshLayout

    var buttonAuthorize:MutableList<Boolean> = mutableListOf()
    var tid:String? = null
    var buttons:MutableList<Button> = mutableListOf()
    var titleAndFragment : MutableMap<String,Fragment> = mutableMapOf(
        "Tournament Rounds" to RoundsFragment(),
        "Matches/Results" to ResultFragment()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_tournament)
        supportActionBar?.hide()
        binding()
        initialize()
        btnRemove.setOnClickListener(this)
        btnParticipate.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener(this)

        vpAdapter = SingleTournamentFragmentAdapter(supportFragmentManager,lifecycle,titleAndFragment.values.toMutableList())
        vp.adapter = vpAdapter

        vp.isUserInputEnabled = false
        TabLayoutMediator(tabLayout,vp)
        { tab: TabLayout.Tab, i: Int ->
            tab.text = titleAndFragment.keys.toMutableList()[i]
        }.attach()

    }

    private fun binding()
    {
        tvTournamentName = findViewById(R.id.tvTournamentName)
        btnPlayed = findViewById(R.id.btnPlayed)
        btnPlaying = findViewById(R.id.btnPlaying)
        btnRemove = findViewById(R.id.btnRemove)
        btnParticipate = findViewById(R.id.btnParticipate)
        btnOpeningSoon = findViewById(R.id.btnOpeningSoon)
        btnFinished = findViewById(R.id.btnFinished)
        btnIneligible = findViewById(R.id.btnIneligible)
        btnClosed = findViewById(R.id.btnClosed)
        tvParticipation = findViewById(R.id.tvParticipation)
        tvAge = findViewById(R.id.tvAge)
        tvEvent = findViewById(R.id.tvEvent)
        tvSponsors = findViewById(R.id.tvSponsors)
        tvOrganizedBy = findViewById(R.id.tvOrganizedBy)
        tvAbout = findViewById(R.id.tvAbout)
        tvPrize = findViewById(R.id.tvPrize)
        recycler = findViewById(R.id.recycler)
        tabLayout = findViewById(R.id.tabLayout)
        vp = findViewById(R.id.vp)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        buttons.addAll(mutableListOf(btnPlayed,btnPlaying,btnParticipate,btnRemove,btnPlaying,btnClosed,btnOpeningSoon,btnFinished,btnIneligible))
    }

    private fun loadButton()
    {

        var index = buttonAuthorize.indexOf(true)
        for(i in buttons.indices)
        {
            if(index == i)
            {
                buttons[i].visibility = View.VISIBLE
            }
            else
            {
                buttons[i].visibility = View.GONE
            }
        }
    }

    private fun initialize()
    {
        tid = intent.getStringExtra("tournamentId")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TournamentRepository()
                val response = repo.singleTournament(tid!!)
                if(response.success == true)
                {
                    buttonAuthorize = response.authorize!!
                    var tournament = response.data!![0]
                    withContext(Dispatchers.Main)
                    {
                        adapter = ParticipatedTeamAdapter(this@SingleTournamentActivity,tournament.participatedTeams!!)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(this@SingleTournamentActivity,LinearLayoutManager.VERTICAL,false)
                        tvTournamentName.text = tournament.tournamentName
                        tvParticipation.text = "${tournament.participationStarts2} to ${tournament.participationEnds2}"
                        tvAge.text = "${tournament.minAgeGroup}-${tournament.maxAgeGroup}"
                        if(tournament.startsFrom2 != null)
                        {
                            tvEvent.text = "${tournament.startsFrom2} to ${tournament.ends2}"
                        }
                        else
                        {
                            tvEvent.text = "Not Announced"
                        }
                        if(tournament.sponsors != null)
                        {
                            tvSponsors.text = "${tournament.sponsors}"
                        }
                        else
                        {
                            tvSponsors.text = "Not Seen"
                        }
                        tvOrganizedBy.text = tournament.organizedBy
                        tvAbout.text = tournament.organizerDetail

                        var prizeBox = ""
                        for(i in tournament.prizePool!!)
                        {
                            prizeBox+="${i["position"]}: ${i["prize"]}\n"
                        }

                        tvPrize.text = prizeBox

                        loadButton()
                    }
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        tvPrize.snackbar(response.message!!)
                    }
                }
            }
            catch (err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    tvPrize.snackbar("Server Error!!")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnRemove ->{
                CoroutineScope(Dispatchers.IO).launch {
                    try
                    {
                        val repo = TournamentRepository()
                        val response = repo.removeParticipation(tid!!)
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                tvPrize.snackbar("${response.message}")
                                val intent = Intent(this@SingleTournamentActivity,SingleTournamentActivity::class.java)
                                intent.putExtra("tournamentId",tid)
                                startActivity(intent)
                                finish()

                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                tvPrize.snackbar("${response.message}")
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            tvPrize.snackbar("Server Error!!")
                        }
                    }
                }
            }
            R.id.btnParticipate ->{
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TournamentRepository()
                        val response = repo.participateInTournament(tid!!)
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                tvPrize.snackbar("${response.message}")
                                val intent = Intent(this@SingleTournamentActivity,SingleTournamentActivity::class.java)
                                intent.putExtra("tournamentId",tid)
                                startActivity(intent)
                                finish()

                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                tvPrize.snackbar("${response.message}")
                            }
                        }
                    }
                    catch (err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            tvPrize.snackbar("Server Error!!")
                        }
                    }

                }
            }
        }
    }

    override fun onRefresh() {
        val intent = Intent(this@SingleTournamentActivity,SingleTournamentActivity::class.java)
        intent.putExtra("tournamentId",tid)
        startActivity(intent)
        finish()
    }
}