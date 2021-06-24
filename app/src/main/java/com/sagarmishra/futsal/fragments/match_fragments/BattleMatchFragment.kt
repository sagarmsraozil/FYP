package com.sagarmishra.futsal.fragments.match_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sagarmishra.futsal.AddBattleActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.UpcomingBattleAdapter
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BattleMatchFragment : Fragment(),View.OnClickListener {
    private lateinit var tvMatches:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var vp:ViewPager2
    private lateinit var adapter:UpcomingBattleAdapter
    private lateinit var tvSearch:AutoCompleteTextView
    private lateinit var btnAdd:Button
    var days:MutableList<String> = mutableListOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_battle_match, container, false)
        tvMatches = view.findViewById(R.id.tvMatches)
        recycler = view.findViewById(R.id.recycler)
        tvSearch = view.findViewById(R.id.tvSearch)
        btnAdd = view.findViewById(R.id.btnAdd)

        vp = requireActivity().findViewById(R.id.vp)
        btnAdd.setOnClickListener(this)
        initialize()


        searchWork()
        return view
    }

    private fun initialize()
    {
        if((StaticData.team!!.teamOwner == StaticData.user!!._id || (StaticData.team!!.teamColeader != null && StaticData.team!!.teamColeader == StaticData.user!!._id)) && StaticData.team!!.status == "Approved")
        {
            btnAdd.visibility = View.VISIBLE
        }
        else
        {
            btnAdd.visibility = View.GONE
        }

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

                    var battleCode = StaticData.upcomingBattles!!.map{
                        it.battleCode!!
                    }
                    var awayTeams = StaticData.upcomingBattles!!.map{
                        it.awayTeam!!.teamName!!
                    }

                    days.addAll(battleCode.toMutableList())
                    days.addAll(awayTeams.toMutableSet().toMutableList())
                    var dayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,days)


                    withContext(Dispatchers.Main)
                    {
                        tvSearch.setAdapter(dayAdapter)
                        tvSearch.threshold = 1
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
                   println(response.message)
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
                it.date!!.split("-")[1].toLowerCase().trim().startsWith(text.toString().trim().toLowerCase())  || it.battleCode == text.toString() || it.awayTeam!!.teamName!!.toLowerCase().trim().startsWith(text.toString().trim().toLowerCase())
            }
            adapter = UpcomingBattleAdapter(requireContext(),searchData.toMutableList())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

            if(text.toString().length > 0)
            {
                tvMatches.text = "${searchData.size} Matches found"
            }
            else
            {
                tvMatches.text = "${StaticData.upcomingBattles!!.size} matches on pending."
            }
        }


    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnAdd ->{
                val intent = Intent(requireContext(),AddBattleActivity::class.java)
                startActivity(intent)
            }
        }
    }


}