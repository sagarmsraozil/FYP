package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.PlayerAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Team
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SingleTeamActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var tvTeamName: TextView
    private lateinit var tvTeamCode: TextView
    private lateinit var tvSeasonCount: TextView
    private lateinit var tvPointsObtained: TextView
    private lateinit var tvTierReached: TextView
    private lateinit var tvBattleCount: TextView
    private lateinit var tvTitleName: TextView
    private lateinit var ivLogo: CircleImageView
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: PlayerAdapter
    private lateinit var btnReqToJoin:Button
    private lateinit var btnStats:Button
    private var status:String?=null
    private var tid:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_team)
        supportActionBar?.hide()
        binding()
        initialize()
        btnStats.setOnClickListener(this)
        btnReqToJoin.setOnClickListener(this)
    }

    private fun binding()
    {
        tvTeamName = findViewById(R.id.tvTeamName)
        tvTeamCode = findViewById(R.id.tvTeamCode)
        tvSeasonCount = findViewById(R.id.tvSeasonCount)
        tvPointsObtained =findViewById(R.id.tvPointsObtained)
        tvTierReached = findViewById(R.id.tvTierReached)
        tvBattleCount = findViewById(R.id.tvBattleCount)
        tvTitleName = findViewById(R.id.tvTitleName)
        ivLogo = findViewById(R.id.ivLogo)
        recycler = findViewById(R.id.recycler)
        btnReqToJoin = findViewById(R.id.btnReqToJoin)
        btnStats = findViewById(R.id.btnStats)
    }

    private fun initialize()
    {

        status = intent.getStringExtra("task")!!
        tid = intent.getStringExtra("tid")!!
        if(status != "Join")
        {
            btnReqToJoin.visibility = View.GONE
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TeamRepository()
                val response2 = repo.fetchSingleTeam(tid!!)
                val response = repo.getTeamStats(tid!!)
                if(response2.success == true)
                {
                    withContext(Dispatchers.Main)
                    {
                        tvTeamName.text = response2.data!!.teamName
                        tvTeamCode.text = response2.data!!.teamCode
                        if(response2.data!!.activeTitle != null && response2.data!!.activeTitle != "")
                        {
                            tvTitleName.text = response2.data!!.activeTitle
                        }
                        else
                        {
                            tvTitleName.text = "N/A"
                        }

                        if(response2.data!!.teamLogo == "no-logo.png")
                        {
                            ivLogo.setImageResource(R.drawable.logoteam)
                        }
                        else
                        {
                            var imgPath = RetrofitService.loadImagePath()+response2.data!!.teamLogo!!.replace("\\","/")
                            Glide.with(this@SingleTeamActivity).load(imgPath).into(ivLogo)
                        }

                        adapter = PlayerAdapter(this@SingleTeamActivity,response2.data!!.teamPlayers!!,response2.data!!,"other")
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(this@SingleTeamActivity)
                    }
                }
                else
                {
                    println(response2.message)
                }
                if(response.success == true)
                {
                    withContext(Dispatchers.Main)
                    {


                        tvSeasonCount.text = response.data!!.season.toString()
                        tvPointsObtained.text = response.data!!.pointsCollected.toString()
                        tvTierReached.text = response.data!!.tier!!.tierNomenclature
                        tvBattleCount.text = response.data!!.matchPlayed.toString()
                    }
                }
                else
                {
                    println(response.message)
                }
            }
            catch (err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    tvTeamCode.snackbar("Server Error!!")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnStats ->{
                val intent = Intent(this@SingleTeamActivity,StatsActivity::class.java)
                intent.putExtra("teamId",tid)
                startActivity(intent)
            }
            R.id.btnReqToJoin->{
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.requestToJoin(tid!!)
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                btnReqToJoin.snackbar(response.message!!)
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                btnReqToJoin.snackbar(response.message!!)
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            btnStats.snackbar("Server Error!!")
                        }
                    }

                }
            }
        }

    }


}