package com.sagarmishra.futsal.fragments.match_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TournamentRepository
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tournament2, container, false)
        recycler = view.findViewById(R.id.recycler)
        initialize()
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
                    println(lstTournamentMatch)
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