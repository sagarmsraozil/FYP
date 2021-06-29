package com.sagarmishra.futsal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.MatchMakingAdapter
import com.sagarmishra.futsal.adapter.NearbyFutsalAdapter
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.entityapi.MatchMaking
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.response.MatchMakingResponse
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SearchMatchActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var btnTeam:Button
    private lateinit var btnRandom:Button
    private lateinit var tvFetchedBy:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var recyclerFutsal:RecyclerView
    private lateinit var adapter:MatchMakingAdapter
    private lateinit var futsalAdapter: NearbyFutsalAdapter
    var lstOpponents:MutableList<MatchMaking> = mutableListOf()
    var lstFutsals:MutableList<Futsal> = mutableListOf()
    var flag:Boolean =true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_match)
        supportActionBar?.hide()
        binding()
        listeners()
        initialize()
    }

    private fun binding()
    {
        btnTeam = findViewById(R.id.btnTeam)
        btnRandom = findViewById(R.id.btnRandom)
        tvFetchedBy = findViewById(R.id.tvFetchedBy)
        recycler = findViewById(R.id.recycler)
        recyclerFutsal = findViewById(R.id.recyclerFutsal)
    }

    private fun listeners()
    {
        btnTeam.setOnClickListener(this)
        btnRandom.setOnClickListener(this)
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TeamRepository()
                var response: MatchMakingResponse
                var fetchBy:String = ""
                if(flag)
                {
                    response = repo.findMatchingTeam(StaticData.team!!._id)
                    withContext(Dispatchers.Main)
                    {
                        fetchBy = "Filtered according to Team preferences"
                    }
                }
                else
                {
                    response = repo.randomMatching(StaticData.team!!._id)
                    withContext(Dispatchers.Main)
                    {
                        fetchBy = "Randomly"
                    }
                }

                if(response.success == true)
                {
                    lstOpponents = response.data!!
                    lstFutsals = response.nearbyFutsals!!
                    withContext(Dispatchers.Main)
                    {
                       fetchBy+="(${lstOpponents.size} Opponents)"
                        tvFetchedBy.text = fetchBy
                        adapter = MatchMakingAdapter(this@SearchMatchActivity,lstOpponents)
                        recycler.adapter = adapter
                        recycler.layoutManager= LinearLayoutManager(this@SearchMatchActivity,LinearLayoutManager.HORIZONTAL,false)
                        var snapHelper:SnapHelper = PagerSnapHelper()
                        recycler.onFlingListener = null
                        snapHelper.attachToRecyclerView(recycler)

                        futsalAdapter = NearbyFutsalAdapter(this@SearchMatchActivity,lstFutsals)
                        recyclerFutsal.adapter = futsalAdapter
                        recyclerFutsal.layoutManager = GridLayoutManager(this@SearchMatchActivity,2,GridLayoutManager.VERTICAL,false)
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
                    btnTeam.snackbar("Server Error!!")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnTeam ->{
                flag = true
                btnRandom.visibility = View.VISIBLE
                btnTeam.visibility = View.GONE
                initialize()
            }
            R.id.btnRandom ->{
                flag = false
                btnTeam.visibility = View.VISIBLE
                btnRandom.visibility = View.GONE
                initialize()
            }
        }
    }
}