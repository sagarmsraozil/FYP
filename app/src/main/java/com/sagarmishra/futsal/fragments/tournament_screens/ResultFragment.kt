package com.sagarmishra.futsal.fragments.tournament_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TournamentRepository
import com.sagarmishra.futsal.adapter.TournamentResultAdapter
import com.sagarmishra.futsal.entityapi.TournamentMatch
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class ResultFragment : Fragment() {
    private lateinit var etSearch:AutoCompleteTextView
    private lateinit var tvMatches:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var tvInfo:TextView
    private lateinit var layoutOverall:LinearLayout
    var lstTournamentMatches:MutableList<TournamentMatch> = mutableListOf()
    var lstTeamName:MutableList<String> = mutableListOf()
    private lateinit var adapter:TournamentResultAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        etSearch = view.findViewById(R.id.etSearch)
        tvMatches = view.findViewById(R.id.tvMatches)
        recycler = view.findViewById(R.id.recycler)
        tvInfo = view.findViewById(R.id.tvInfo)
        layoutOverall = view.findViewById(R.id.layoutOverall)
        initialize()
        searchWork()
        return view
    }


    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TournamentRepository()
                val response = repo.tournamentMatches(StaticData.tournamentId)
                if(response.success == true)
                {
                    StaticData.historyTournament = response.analysis!!
                    lstTournamentMatches = response.data!!
                    lstTeamName = lstTournamentMatches.map{
                        it.team1!!.teamName!!
                    }.toMutableList()

                    lstTeamName.addAll(lstTournamentMatches.map{
                        it.team2!!.teamName!!
                    }.toMutableList())

                    lstTeamName = lstTeamName.toMutableSet().toMutableList()

                    withContext(Dispatchers.Main)
                    {
                        if(lstTournamentMatches.size > 0)
                        {
                            tvMatches.text = "${lstTournamentMatches.size} matches"
                            adapter = TournamentResultAdapter(requireContext(),lstTournamentMatches)
                            recycler.adapter = adapter
                            recycler.layoutManager = LinearLayoutManager(requireContext())
                            var searchAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,lstTeamName)
                            etSearch.setAdapter(searchAdapter)
                            etSearch.threshold = 1

                            layoutOverall.visibility = View.VISIBLE
                            tvInfo.visibility = View.GONE

                        }
                        else
                        {
                            layoutOverall.visibility = View.GONE
                            tvInfo.visibility = View.VISIBLE
                        }

                    }
                }
                else
                {
                    println(response.message!!)
                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
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

            var searched = lstTournamentMatches.filter {
                it.team1!!.teamName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim()) || it.team2!!.teamName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim())
            }.toMutableList()
            adapter = TournamentResultAdapter(requireContext(),searched)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

            if(text.toString().length > 0)
            {
                tvMatches.text = "${searched.size} matches found"
            }
            else
            {
                tvMatches.text = "${lstTournamentMatches.size} matches"
            }
        }
    }

}