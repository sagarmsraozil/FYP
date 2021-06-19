package com.sagarmishra.futsal.fragments.request_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.RequestRepository
import com.sagarmishra.futsal.adapter.BattleRequestAdapter
import com.sagarmishra.futsal.entityapi.MatchRequest
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class OpponentRequestFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var tvRequests: TextView
    private lateinit var adapter :BattleRequestAdapter
    var lstOpponentRequest:MutableList<MatchRequest> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_opponent_request, container, false)

        recycler = view.findViewById(R.id.recycler)
        tvRequests = view.findViewById(R.id.tvRequests)

        initialize()

        return view
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = RequestRepository()
                val response = repo.opponentRequests(StaticData.team!!._id)
                if(response.success == true)
                {
                    lstOpponentRequest = response.data!!
                    withContext(Dispatchers.Main)
                    {
                        adapter = BattleRequestAdapter(requireActivity(),requireContext(),lstOpponentRequest,"opponentRequests")
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext())
                        tvRequests.text = "${lstOpponentRequest.size} requests"
                    }
                }
                else
                {
                    println(response.message!!)
                    withContext(Dispatchers.Main)
                    {
                        tvRequests.text = "${lstOpponentRequest.size} requests"
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