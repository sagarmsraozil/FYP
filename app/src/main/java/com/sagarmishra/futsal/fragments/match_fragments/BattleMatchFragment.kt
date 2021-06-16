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
import androidx.viewpager2.widget.ViewPager2
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.UpcomingBattleAdapter
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BattleMatchFragment : Fragment() {
    private lateinit var tvMatches:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var vp:ViewPager2
    private lateinit var adapter:UpcomingBattleAdapter
    private lateinit var tvSearch:AutoCompleteTextView
    var days:MutableList<String> = mutableListOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_battle_match, container, false)
        tvMatches = view.findViewById(R.id.tvMatches)
        recycler = view.findViewById(R.id.recycler)
        tvSearch = view.findViewById(R.id.tvSearch)
        vp = requireActivity().findViewById(R.id.vp)
        initialize()
        Thread.sleep(2000)
        var battleCode = StaticData.upcomingBattles!!.map{
            it.battleCode!!
        }
        days.addAll(battleCode.toMutableList())
        var dayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,days)
        tvSearch.setAdapter(dayAdapter)
        tvSearch.threshold = 1
        searchWork()
        return view
    }

    private fun initialize()
    {


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TeamRepository()
                val response = repo.fetchBattles(StaticData.team!!._id)
                if(response.success == true)
                {
                    var battles = response.data!!

                    StaticData.deletableBattle = response.deleteable

                    var upcoming = battles.filter{
                        it.status == "Soon"
                    }
                    var result = battles.filter{
                        it.status == "Completed"
                    }
                    StaticData.upcomingBattles = upcoming.toMutableList()
                    StaticData.resultBattles = result.toMutableList()

                    withContext(Dispatchers.Main)
                    {
                        if(StaticData.upcomingBattles!!.size > 0)
                        {
                            tvMatches.text = "${StaticData.upcomingBattles!!.size} Matches on Pending"
                        }

                        adapter = UpcomingBattleAdapter(requireContext(),StaticData.upcomingBattles!!)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }


                }
                else{
                    withContext(Dispatchers.Main)
                    {
                        tvMatches.snackbar("${response.message}")
                    }
                }
            }
            catch(err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    tvMatches.snackbar("Server Error")
                }

            }
        }



    }

    private fun searchWork()
    {
        tvSearch.doOnTextChanged { text, start, before, count ->
            var searchData = StaticData.upcomingBattles!!.filter{
                it.date!!.split("-")[1].toLowerCase().trim().startsWith(text.toString().trim().toLowerCase())  || it.battleCode == text.toString()
            }
            adapter = UpcomingBattleAdapter(requireContext(),searchData.toMutableList())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

            if(text.toString().length > 0)
            {
                tvMatches.text = "${searchData.size} Matches on pending."
            }
            else
            {
                tvMatches.text = "${StaticData.upcomingBattles!!.size} matches on pending."
            }
        }


    }


}