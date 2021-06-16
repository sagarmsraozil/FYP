package com.sagarmishra.futsal.fragments.match_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    var lstTournamentHistory:MutableList<TournamentMatch> = mutableListOf()
    private lateinit var adapter:TournamentHistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tournament_result, container, false)
        recycler = view.findViewById(R.id.recycler)
        initialize()
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
                    adapter = TournamentHistoryAdapter(requireContext(),lstTournamentHistory)
                    withContext(Dispatchers.Main)
                    {
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
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

}