package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.TeamsAdapter
import com.sagarmishra.futsal.entityapi.Team
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class NewTeamActivity : AppCompatActivity() {
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:TeamsAdapter
    private lateinit var tvSearch:AutoCompleteTextView
    private lateinit var tvTeams:TextView
    var lstTeams:MutableList<Team> = mutableListOf()
    var status:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_team)
        supportActionBar?.hide()
        binding()
        initialize()
        searchWork()
    }

    private fun binding()
    {
        recycler = findViewById(R.id.recycler)
        tvSearch = findViewById(R.id.tvSearch)
        tvTeams = findViewById(R.id.tvTeams)
    }

    private fun initialize()
    {
        status = intent.getStringExtra("task")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TeamRepository()
                val response = repo.fetchEveryTeams()
                if(response.success == true)
                {
                    lstTeams = response.data!!
                    withContext(Dispatchers.Main)
                    {
                        adapter = TeamsAdapter(this@NewTeamActivity,lstTeams,status!!)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(this@NewTeamActivity)

                        var teamNames = lstTeams.map{
                            it.teamName!!
                        }

                        var teamCodes = lstTeams.map{
                            it.teamCode!!
                        }

                        var items:MutableList<String> = teamCodes.toMutableList()
                        items.addAll(teamNames.toMutableList())

                        var searchAdapter = ArrayAdapter(this@NewTeamActivity,android.R.layout.simple_expandable_list_item_1,items)
                        tvSearch.setAdapter(searchAdapter)
                        tvSearch.threshold = 1

                        tvTeams.text = "${lstTeams.size} teams"
                    }
                }

            }
            catch (err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    recycler.snackbar("Server Error!!")
                }
            }
        }
    }

    private fun searchWork()
    {
        tvSearch.doOnTextChanged { text, start, before, count ->

            var searchData = lstTeams.filter{
                it.teamName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim()) || it.teamCode!! == text.toString()
            }

            adapter = TeamsAdapter(this,searchData.toMutableList(),status!!)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

            if(text.toString().length > 0)
            {
                tvTeams.text = "${searchData.size} teams"
            }
            else
            {
                tvTeams.text = "${lstTeams.size} teams"
            }
        }
    }

    override fun onBackPressed() {
        if(status == "Join")
        {
            var intent = Intent(this,MainActivity::class.java)
            intent.putExtra("FRAGMENT_NUMBER",0)
            startActivity(intent)
        }
    }


}