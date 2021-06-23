package com.sagarmishra.futsal.fragments.verticalNav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TournamentRepository
import com.sagarmishra.futsal.adapter.TournamentAdapter
import com.sagarmishra.futsal.entityapi.Tournament
import com.sagarmishra.futsal.response.TournamentResponse
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class TournamentsFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var etSearch:AutoCompleteTextView
    private lateinit var tvTournamentCount:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var swipeRefresh:SwipeRefreshLayout
    var lstTournamentAndCode:MutableList<String> = mutableListOf()
    var lstTournaments:MutableList<Tournament> = mutableListOf()
    private lateinit var adapter:TournamentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tournaments, container, false)
        etSearch = view.findViewById(R.id.etSearch)
        tvTournamentCount = view.findViewById(R.id.tvTournamentCount)
        recycler = view.findViewById(R.id.recycler)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener(this)
        initialize()
        searchWork()
        return view
    }


    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TournamentRepository()
                val response = repo.fetchTournaments()
                if(response.success == true)
                {
                    lstTournaments = response.data!!

                    lstTournamentAndCode = lstTournaments.map{
                        it.tournamentName!!
                    }.toMutableList()
                    lstTournamentAndCode.addAll(lstTournaments.map {
                        it.tournamentCode!!
                    }.toMutableList())

                    lstTournamentAndCode = lstTournamentAndCode.toMutableSet().toMutableList()

                    withContext(Dispatchers.Main)
                    {
                        adapter = TournamentAdapter(requireContext(),lstTournaments)
                        var searchAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,lstTournamentAndCode)

                        recycler.adapter = adapter
                        etSearch.setAdapter(searchAdapter)
                        etSearch.threshold = 1

                        recycler.layoutManager = LinearLayoutManager(requireContext())

                        tvTournamentCount.text = "${lstTournaments.size} tournaments"

                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        tvTournamentCount.text = "${lstTournaments.size} tournaments"
                    }

                    println(response.message!!)
                }
            }
            catch(err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    etSearch.snackbar("Server Error!!")
                }
            }
        }
    }

    private fun searchWork()
    {
        etSearch.doOnTextChanged { text, start, before, count ->

            var searchedTournament = lstTournaments.filter {
                it.tournamentName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim()) || it.tournamentCode == text.toString()
            }.toMutableList()

            adapter = TournamentAdapter(requireContext(),searchedTournament)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

            if(text.toString().length > 0)
            {
                tvTournamentCount.text = "${searchedTournament.size} tournaments searched"
            }
            else
            {
                tvTournamentCount.text = "${lstTournaments.size} tournaments"
            }
        }
    }

    override fun onRefresh() {
        val intent = Intent(requireContext(),MainActivity::class.java)
        intent.putExtra("FRAGMENT_NUMBER",9)
        startActivity(intent)
    }

}