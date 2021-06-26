package com.sagarmishra.futsal.fragments.chatFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.ChatRepository
import com.sagarmishra.futsal.adapter.AvailablePlayersAdapter
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class AvailableFragment : Fragment() {
    private lateinit var etSearch:AutoCompleteTextView
    private lateinit var recycler:RecyclerView
    private lateinit var tvPlayers:TextView
    private lateinit var adapter:AvailablePlayersAdapter
    var players:MutableList<AuthUser> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_available, container, false)
        etSearch = view.findViewById(R.id.etSearch)
        recycler = view.findViewById(R.id.recycler)
        tvPlayers = view.findViewById(R.id.tvPlayers)
        initialize()
        searchWork()
        return view
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = ChatRepository()
                val response = repo.teamRequiredPlayers()
                if(response.success == true)
                {
                    players = response.data!!
                    withContext(Dispatchers.Main)
                    {
                        tvPlayers.text = "${players.size} players."


                        var playerUsernames = players.map {
                            it.userName
                        }.toMutableSet().toMutableList()



                        var searchAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,playerUsernames)
                        etSearch.setAdapter(searchAdapter)
                        etSearch.threshold = 1

                        adapter = AvailablePlayersAdapter(requireContext(),players)
                        recycler.adapter = adapter
                        recycler.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
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
                    etSearch.snackbar("Server Error!!")
                }
            }
        }
    }

    private fun searchWork()
    {
        etSearch.doOnTextChanged { text, start, before, count ->
            var searchData = text.toString().toLowerCase().trim()
            var searched = players.filter {
                it.userName!!.toLowerCase().trim().startsWith(searchData)
            }
            adapter = AvailablePlayersAdapter(requireContext(),searched.toMutableList())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
            if(text.toString().length > 0)
            {
                tvPlayers.text = "${searched.size} players searched"
            }
            else
            {
                tvPlayers.text = "${players.size} players"
            }
        }
    }


}