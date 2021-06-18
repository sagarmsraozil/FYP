package com.sagarmishra.futsal.fragments.match_fragments

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
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TournamentRepository
import com.sagarmishra.futsal.adapter.TournamentHistoryAdapter
import com.sagarmishra.futsal.entityapi.TournamentMatch
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class TournamentMatchFragment : Fragment() {
    var lstTournamentMatch:MutableList<TournamentMatch> = mutableListOf()
    private lateinit var recycler:RecyclerView
    private lateinit var tvSearch:AutoCompleteTextView
    private lateinit var tvRecord:TextView
    private lateinit var adapter:TournamentHistoryAdapter
    var lstFutsalNameAndTCode:MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tournament2, container, false)
        recycler = view.findViewById(R.id.recycler)
        tvSearch = view.findViewById(R.id.tvSearch)
        tvRecord = view.findViewById(R.id.tvRecord)
        initialize()
        searchWork()
        return view
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TournamentRepository()
                val response = repo.getUpcomingMatches(StaticData.team!!._id)
                if(response.success == true)
                {
                    lstTournamentMatch = response.data!!
                    var lstFName = lstTournamentMatch.map{
                        it.tournament_id!!.tournamentName!!
                    }
                    var lstTCode = lstTournamentMatch.map{
                        it.tournament_id!!.tournamentCode!!
                    }
                    var team1 = lstTournamentMatch.map{
                        it.team1!!.teamName!!
                    }
                    var team2 = lstTournamentMatch.map{
                        it.team2!!.teamName!!
                    }
                    var container:MutableList<String> = mutableListOf()
                    container.addAll(team1)
                    container.addAll(team2)
                    lstFutsalNameAndTCode.addAll(lstFName.toMutableList())
                    lstFutsalNameAndTCode.addAll(lstTCode.toMutableList())
                    lstFutsalNameAndTCode.addAll(container.toMutableSet().toMutableList())


                    withContext(Dispatchers.Main)
                    {
                        var searchAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,lstFutsalNameAndTCode)
                        tvSearch.setAdapter(searchAdapter)
                        tvSearch.threshold = 1
                        tvRecord.text = "${lstTournamentMatch.size} records"
                        var adapter = TournamentHistoryAdapter(requireContext(),lstTournamentMatch,"match")
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

                    }
                }
                else
                {
                   println(response.message)
                }
            }
            catch(err:Exception)
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

            var searchRecords = lstTournamentMatch.filter{
                it.tournament_id!!.tournamentName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim()) || it.tournament_id!!.tournamentCode!! == text.toString() || it.team1!!.teamName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim()) || it.team2!!.teamName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim())
            }

            adapter = TournamentHistoryAdapter(requireContext(),searchRecords.toMutableList(),"match")
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

            if(text.toString().length > 0)
            {
                tvRecord.text = "${searchRecords.size} records"
            }
            else
            {
                tvRecord.text = "${lstTournamentMatch.size} records"
            }
        }
    }


}