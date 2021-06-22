package com.sagarmishra.futsal.fragments.challenges_and_titles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.ChallengeAdapter
import com.sagarmishra.futsal.entityapi.TeamChallenges
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class ChallengeFragment : Fragment() {
    private lateinit var tvChallenges:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:ChallengeAdapter
    var lstChallenges:MutableList<TeamChallenges> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_challenge, container, false)
        tvChallenges = view.findViewById(R.id.tvChallenges)
        recycler = view.findViewById(R.id.recycler)
        initialize()
        return view
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TeamRepository()
                val response = repo.teamChallenges()
                if(response.success == true)
                {
                    lstChallenges = response.data!!
                    withContext(Dispatchers.Main)
                    {
                        adapter = ChallengeAdapter(requireContext(),lstChallenges)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext())
                        tvChallenges.text = "${lstChallenges.size} challenges"

                    }
                }
                else
                {
                    println(response.message!!)
                    withContext(Dispatchers.Main)
                    {
                        tvChallenges.text = "${lstChallenges.size} challenges"

                    }
                }
            }
            catch (err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    recycler.snackbar("Server Error")
                }
            }
        }
    }

}