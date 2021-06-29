package com.sagarmishra.futsal.fragments.tournament_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.ramotion.foldingcell.FoldingCell
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TournamentRepository
import com.sagarmishra.futsal.adapter.GroupPointAdapter
import com.sagarmishra.futsal.entityapi.TournamentGroup
import com.sagarmishra.futsal.entityapi.TournamentStructure
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round


class RoundsFragment : Fragment(),View.OnClickListener {
    private lateinit var layoutLeague:LinearLayout
    private lateinit var recycler:RecyclerView
    private lateinit var round1:LinearLayout
    private lateinit var tvTeams1:TextView
    private lateinit var round2:LinearLayout
    private lateinit var tvTeams2:TextView
    private lateinit var round3:LinearLayout
    private lateinit var tvTeams3:TextView
    private lateinit var round4:LinearLayout
    private lateinit var tvTeams4:TextView
    private lateinit var round5:LinearLayout
    private lateinit var tvTeams5:TextView
    private lateinit var round6:LinearLayout
    private lateinit var tvTeams6:TextView
    private lateinit var round7:LinearLayout
    private lateinit var tvTeams7:TextView
    private lateinit var round8:LinearLayout
    private lateinit var tvTeams8:TextView
    private lateinit var tvGroup:TextView
    private lateinit var tvLeague:TextView
    private lateinit var adapter:GroupPointAdapter
    private lateinit var tvInfo:TextView
    private lateinit var layoutOverall:LinearLayout

    var roundsDeclaration:MutableMap<String,Boolean> = mutableMapOf()
    var tournamentStructure: MutableList<TournamentStructure> = mutableListOf()
    var layouts:MutableList<LinearLayout> = mutableListOf()
    var pointsData:MutableList<MutableList<TournamentGroup>> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rounds, container, false)
        layoutLeague = view.findViewById(R.id.layoutLeague)
        recycler = view.findViewById(R.id.recycler)
        round1 = view.findViewById(R.id.round1)
        round2 = view.findViewById(R.id.round2)
        round3 = view.findViewById(R.id.round3)
        round4 = view.findViewById(R.id.round4)
        round5 = view.findViewById(R.id.round5)
        round6 = view.findViewById(R.id.round6)
        round7 = view.findViewById(R.id.round7)
        round8 = view.findViewById(R.id.round8)
        tvTeams1 = view.findViewById(R.id.tvTeams1)
        tvTeams2 = view.findViewById(R.id.tvTeams2)
        tvTeams3 = view.findViewById(R.id.tvTeams3)
        tvTeams4 = view.findViewById(R.id.tvTeams4)
        tvTeams5 = view.findViewById(R.id.tvTeams5)
        tvTeams6 = view.findViewById(R.id.tvTeams6)
        tvTeams7 = view.findViewById(R.id.tvTeams7)
        tvTeams8 = view.findViewById(R.id.tvTeams8)
        tvGroup = view.findViewById(R.id.tvGroup)
        tvLeague = view.findViewById(R.id.tvLeague)
        tvInfo = view.findViewById(R.id.tvInfo)
        layoutOverall = view.findViewById(R.id.layoutOverall)

        layouts = mutableListOf(layoutLeague,round1,round2,round3,round4,round5,round6,round7,round8,layoutLeague)

        initialize()
        return view
    }

    private fun checkPoints()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TournamentRepository()
                val response = repo.groupPoints(StaticData.tournamentId)
                if(response.success == true)
                {
                    pointsData = response.data!!

                    withContext(Dispatchers.Main)
                    {
                        adapter = GroupPointAdapter(requireContext(),pointsData)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                        var snapHelper: SnapHelper = PagerSnapHelper()
                        snapHelper.attachToRecyclerView(recycler)
                    }
                }
                else
                {
                    println(response.message!!)
                }
            }
            catch(err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    tvTeams1.snackbar("Server Error!!")
                }
            }
        }
    }

    private fun loadData()
    {
        if(roundsDeclaration.values.contains(true))
        {
            layoutOverall.visibility = View.VISIBLE
            tvInfo.visibility = View.GONE
            var structureData = tournamentStructure[0]

            for(i in roundsDeclaration.keys)
            {
                if(i.startsWith("round"))
                {
                    var index = roundsDeclaration.keys.indexOf(i)
                    if(roundsDeclaration[i] == true)
                    {
                        layouts[index].visibility = View.VISIBLE

                    }
                    else
                    {
                        layouts[index].visibility = View.GONE
                    }
                }

            }

            tvTeams1.text = structureData.round1!!.map {
                it.teamName+"(${it.teamTag})"
            }.joinToString("\n")
            tvTeams2.text = structureData.round2!!.map {
                it.teamName+"(${it.teamTag})"
            }.joinToString("\n")
            tvTeams3.text = structureData.round3!!.map {
                it.teamName+"(${it.teamTag})"
            }.joinToString("\n")
            tvTeams4.text = structureData.round4!!.map {
                it.teamName+"(${it.teamTag})"
            }.joinToString("\n")
            tvTeams5.text= structureData.round5!!.map {
                it.teamName+"(${it.teamTag})"
            }.joinToString("\n")
            tvTeams6.text = structureData.round6!!.map {
                it.teamName+"(${it.teamTag})"
            }.joinToString("\n")
            tvTeams7.text = structureData.round7!!.map {
                it.teamName+"(${it.teamTag})"
            }.joinToString("\n")
            tvTeams8.text= structureData.round8!!.map {
                it.teamName+"(${it.teamTag})"
            }.joinToString("\n")

            if(roundsDeclaration["groupDivision"] == true)
            {
                layoutLeague.visibility = View.VISIBLE
                tvGroup.visibility = View.VISIBLE
                tvLeague.visibility = View.GONE
                checkPoints()
            }
            else if(roundsDeclaration["phase1"] == true)
            {
                layoutLeague.visibility = View.VISIBLE
                tvGroup.visibility = View.GONE
                tvLeague.visibility = View.VISIBLE
                checkPoints()
            }
            else
            {
                layoutLeague.visibility = View.GONE
            }
        }
        else
        {
            layoutOverall.visibility = View.GONE
            tvInfo.visibility = View.VISIBLE
        }
    }



    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TournamentRepository()
                val response = repo.roundsDeclaration(StaticData.tournamentId)
                if(response.success == true)
                {
                    roundsDeclaration = response.dataHolder!!
                    tournamentStructure = response.data!!
                    withContext(Dispatchers.Main)
                    {
                        loadData()
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
                    recycler.snackbar("Server Error!!")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {

        }
    }


}