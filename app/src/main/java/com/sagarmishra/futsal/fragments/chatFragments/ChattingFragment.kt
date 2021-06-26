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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.ChatRepository
import com.sagarmishra.futsal.adapter.ChatPersonAdapter
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class ChattingFragment : Fragment() {
    private lateinit var etSearch:AutoCompleteTextView
    private lateinit var tvInfo:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:ChatPersonAdapter
    var players:MutableList<AuthUser> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_chatting, container, false)
        etSearch = view.findViewById(R.id.etSearch)
        tvInfo = view.findViewById(R.id.tvInfo)
        recycler = view.findViewById(R.id.recycler)
        initialize()
        searchWork()
        return view
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = ChatRepository()
                val response = repo.myInteractions()
                val response2 = repo.fetchMyUnseen()
                if(response.success == true)
                {
                    players = response.data!!
                    StaticData.playerTeam = response.playerTeam!!
                    withContext(Dispatchers.Main)
                    {
                        if(players.size > 0)
                        {
                            var userNames = players.map {
                                it.userName
                            }.toMutableList()
                            var searchAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,userNames)
                            etSearch.setAdapter(searchAdapter)
                            etSearch.threshold = 1

                            adapter = ChatPersonAdapter(requireContext(),players)
                            recycler.adapter = adapter
                            recycler.layoutManager = LinearLayoutManager(requireContext())

                            tvInfo.visibility = View.GONE
                            etSearch.visibility = View.VISIBLE
                            recycler.visibility = View.VISIBLE
                        }
                        else
                        {
                            tvInfo.visibility = View.VISIBLE
                            etSearch.visibility = View.GONE
                            recycler.visibility = View.GONE
                        }
                    }
                }
                else
                {
                    println(response.message!!)
                }

                if(response2.success == true)
                {
                    StaticData.unseenMessage = response2.data!!
                }
                else
                {
                    println(response2.message!!)
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

    private fun searchWork()
    {
        etSearch.doOnTextChanged { text, start, before, count ->
            var text = text.toString().toLowerCase().trim()

            var searchedData = players.filter {
                it.userName!!.toLowerCase().trim().startsWith(text)
            }.toMutableList()


            adapter = ChatPersonAdapter(requireContext(),searchedData)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

        }
    }


}