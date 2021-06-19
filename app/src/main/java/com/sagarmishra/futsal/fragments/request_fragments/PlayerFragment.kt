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
import com.sagarmishra.futsal.adapter.PlayerRequestAdapter
import com.sagarmishra.futsal.entityapi.RequestModel
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class PlayerFragment : Fragment() {
    private lateinit var tvRequests:TextView
    private lateinit var recycler:RecyclerView
    var lstRequests:MutableList<RequestModel> = mutableListOf()
    private lateinit var adapter: PlayerRequestAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        tvRequests = view.findViewById(R.id.tvRequests)
        recycler = view.findViewById(R.id.recycler)
        initialize()
        return view
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = RequestRepository()
                val response = repo.getUserRequests(StaticData.team!!._id)
                if(response.success == true)
                {
                    lstRequests = response.data!!
                    withContext(Dispatchers.Main)
                    {
                        adapter = PlayerRequestAdapter(requireActivity(),requireContext(),lstRequests)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext())
                        tvRequests.text = "${lstRequests.size} requests"
                    }
                }
                else
                {
                    println(response.message!!)
                    withContext(Dispatchers.Main)
                    {
                        tvRequests.text = "${lstRequests.size} requests"
                    }

                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    tvRequests.snackbar("Server Error")
                }
            }
        }
    }

}