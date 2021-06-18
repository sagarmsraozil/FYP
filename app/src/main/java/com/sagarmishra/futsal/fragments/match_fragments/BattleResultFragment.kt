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
import com.sagarmishra.futsal.adapter.BattleResultAdapter
import com.sagarmishra.futsal.model.StaticData


class BattleResultFragment : Fragment() {
    private lateinit var tvSearch:AutoCompleteTextView
    private lateinit var tvResults:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:BattleResultAdapter
    var days:MutableList<String> = mutableListOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_battle_result, container, false)
        tvSearch = view.findViewById(R.id.tvSearch)
        tvResults = view.findViewById(R.id.tvResults)
        recycler = view.findViewById(R.id.recycler)
        initialize()
        searchWork()
        return view
    }

    private fun initialize()
    {
         var battleCode = StaticData.resultBattles!!.map{
                it.battleCode!!
            }
            var awayTeams = StaticData.resultBattles!!.map{
                it.awayTeam!!.teamName!!
            }

            days.addAll(battleCode.toMutableList())
            days.addAll(awayTeams.toMutableList().toMutableSet().toMutableList())


        val dayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,days)
        tvSearch.setAdapter(dayAdapter)
        tvSearch.threshold = 1

         if(StaticData.resultBattles!!.size > 0)
         {
             tvResults.text = "${StaticData.resultBattles!!.size} results"
         }
        else
         {
             tvResults.text = "0 results"
         }

        adapter = BattleResultAdapter(requireContext(),StaticData.resultBattles!!)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

    }

    private fun searchWork()
    {
        tvSearch.doOnTextChanged { text, start, before, count ->
            var searchData = StaticData.resultBattles!!.filter{

                it.date!!.split("-")[1].toLowerCase().trim().startsWith(text.toString().toLowerCase().trim()) || it.battleCode == text.toString() || it.awayTeam!!.teamName!!.toLowerCase().trim().startsWith(text.toString().trim().toLowerCase())
            }
            adapter = BattleResultAdapter(requireContext(),searchData.toMutableList())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
            if(text!!.toString().length > 0)
            {
                tvResults.text = "${searchData.size} results"
            }
            else
            {
                tvResults.text = "${StaticData.resultBattles!!.size} results"
            }
        }
    }


}