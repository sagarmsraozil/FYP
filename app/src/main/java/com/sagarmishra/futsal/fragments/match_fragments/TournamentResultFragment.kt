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


class TournamentResultFragment : Fragment() {
    private lateinit var recycler:RecyclerView
    private lateinit var tvSearch:AutoCompleteTextView
    private lateinit var tvRecord:TextView
    var lstTournamentHistory:MutableList<TournamentMatch> = mutableListOf()
    private lateinit var adapter:TournamentHistoryAdapter
    private lateinit var tvWins:TextView
    private lateinit var tvLoss:TextView
    private lateinit var tvDraw:TextView
    var lstFutsalNameAndTCode:MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tournament_result, container, false)
        recycler = view.findViewById(R.id.recycler)
        tvSearch=view.findViewById(R.id.tvSearch)
        tvRecord = view.findViewById(R.id.tvRecord)
        tvWins = view.findViewById(R.id.tvWins)
        tvLoss = view.findViewById(R.id.tvLoss)
        tvDraw = view.findViewById(R.id.tvDraw)
        initialize()
        searchWork()
        return view
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TournamentRepository()
                val response = repo.getTournamentHistory(StaticData.team!!._id)
                if(response.success == true)
                {
                    lstTournamentHistory = response.data!!
                    StaticData.tournamentHistory = response.myTeamStats
                    adapter = TournamentHistoryAdapter(requireContext(),lstTournamentHistory,"history")
                    var fName = lstTournamentHistory.map{
                        it.tournament_id!!.tournamentName!!
                    }
                    var tCode = lstTournamentHistory.map{
                        it.tournament_id!!.tournamentCode!!
                    }
                    var team1 = lstTournamentHistory.map{
                        it.team1!!.teamName!!
                    }
                    var team2 = lstTournamentHistory.map{
                        it.team2!!.teamName!!
                    }

                    var container:MutableList<String> = mutableListOf()
                    container.addAll(team1)
                    container.addAll(team2)

                    lstFutsalNameAndTCode.addAll(fName.toMutableSet().toMutableList())
                    lstFutsalNameAndTCode.addAll(tCode.toMutableSet().toMutableList())
                    lstFutsalNameAndTCode.addAll(container.toMutableSet().toMutableList())
                    lstFutsalNameAndTCode.remove("Team Genexus")


                    withContext(Dispatchers.Main)
                    {
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                        tvRecord.text = "${lstTournamentHistory.size} records"
                        var searchAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,lstFutsalNameAndTCode)
                        tvSearch.setAdapter(searchAdapter)
                        tvSearch.threshold = 1

                        tvWins.text = "Wins: ${response.myTeamStats!!["winCount"]}"
                        tvLoss.text = "Loss: ${response.myTeamStats!!["lossCount"]}"
                        tvDraw.text = "Draw: ${response.myTeamStats!!["drawCount"]}"

                    }


                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        recycler.snackbar("${response.message}")
                    }
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

            var searchRecords = lstTournamentHistory.filter{
                it.tournament_id!!.tournamentName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim()) || it.tournament_id!!.tournamentCode!! == text.toString() || it.team1!!.teamName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim()) || it.team2!!.teamName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim())
            }

            adapter = TournamentHistoryAdapter(requireContext(),searchRecords.toMutableList(),"history")
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

            if(text.toString().length > 0)
            {
                tvRecord.text = "${searchRecords.size} records found"
            }
            else
            {
                tvRecord.text = "${lstTournamentHistory.size} records"
            }
        }
    }

}