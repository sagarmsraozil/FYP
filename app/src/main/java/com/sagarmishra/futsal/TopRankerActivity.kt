package com.sagarmishra.futsal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.RankAdapter
import com.sagarmishra.futsal.entityapi.TeamStats
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

@Suppress("Deprecation")
class TopRankerActivity : AppCompatActivity() {
    private lateinit var spinnerSeason:Spinner
    private lateinit var spinnerAge:Spinner
    private lateinit var tableRankers:TableLayout
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:RankAdapter
    var lstRankers:MutableList<TeamStats> = mutableListOf()
    var currentSeason = 0
    var ageGroup:MutableList<Int> = mutableListOf()
    var totalSeasons:MutableList<Int> = mutableListOf()
    var ageGroups:MutableList<String> = mutableListOf("8-10","11-12","13-15","16-18","19-22","22-35","36-55")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_ranker)
        supportActionBar?.hide()
        spinnerSeason = findViewById(R.id.spinnerSeason)
        spinnerAge = findViewById(R.id.spinnerAge)
        tableRankers = findViewById(R.id.tableRankers)
        recycler = findViewById(R.id.recycler)


        initialize()
        listeners()
    }

    private fun loadData(season:Int,ageData:MutableList<Int>)
    {
        if(lstRankers.size > 0)
        {

            var searchedData = lstRankers.filter {
                it.season == season && (it.team_id!!.ageGroup >= ageData[0] && it.team_id!!.ageGroup <= ageData[1])
            }.toMutableList()

            adapter = RankAdapter(this,searchedData)
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(this)

            var stringfyAge = ageData.joinToString("-")

            currentSeason = season
            ageGroup = ageData.map{
                it
            }.toMutableList()

            spinnerSeason.setSelection(totalSeasons.indexOf(currentSeason))
            spinnerAge.setSelection(ageGroups.indexOf(stringfyAge))
        }

    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TeamRepository()
                val response = repo.topRankers()
                if(response.success == true)
                {
                    lstRankers = response.data!!
                    currentSeason = response.currentSeason
                    ageGroup = response.ageGroup!!
                    totalSeasons = response.totalSeasons!!

                    var seasonMapping =  totalSeasons.map {
                        "Season $it"
                    }.toMutableList()

                    withContext(Dispatchers.Main)
                    {
                        var seasonAdapter = ArrayAdapter(this@TopRankerActivity,android.R.layout.simple_expandable_list_item_1,seasonMapping)
                        var ageAdapter = ArrayAdapter(this@TopRankerActivity,android.R.layout.simple_expandable_list_item_1,ageGroups)
                        spinnerSeason.adapter = seasonAdapter
                        spinnerAge.adapter = ageAdapter
                        loadData(currentSeason,ageGroup)

                    }
                }
                else
                {
                    println(response.message!!)
                }
            }
            catch (err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    spinnerAge.snackbar("Server Error")
                }
            }
        }
    }

    private fun listeners()
    {
        spinnerAge.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                   var selected = parent?.getItemAtPosition(position).toString()
                    var dataBox = selected.split("-")
                    ageGroup = dataBox.map {
                        it.toInt()
                    }.toMutableList()

                    loadData(currentSeason,ageGroup)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

        spinnerSeason.onItemSelectedListener =
            object :AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    loadData(totalSeasons[position],ageGroup)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
    }

}