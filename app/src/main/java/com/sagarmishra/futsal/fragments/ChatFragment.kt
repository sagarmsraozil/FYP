package com.sagarmishra.futsal.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.FutsalRepository
import com.sagarmishra.futsal.adapter.CommentFutsalAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.db.FutsalRoomDB
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class ChatFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recycler:RecyclerView
    private lateinit var adapter : CommentFutsalAdapter
    private lateinit var swipe:SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_chat, container, false)
        recycler = view.findViewById(R.id.recycler)
        swipe = view.findViewById(R.id.swipe)
        if(RetrofitService.online == true)
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = FutsalRepository()
                    val response = repo.showFutsals()
                    if(response.success == true)
                    {
                        adapter = CommentFutsalAdapter(requireContext(),response.data!!)
                        withContext(Dispatchers.Main)
                        {

                            recycler.adapter = adapter
                            recycler.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
                            //recycler.layoutManager = LinearLayoutManager(requireContext())
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
                catch (ex:Exception)
                {
                    withContext(Dispatchers.Main)
                    {
                        recycler.snackbar("Server Issue...")
                    }
                }
            }
        }
        else
        {
            CoroutineScope(Dispatchers.IO).launch {
                val instance = FutsalRoomDB.getFutsalDBInstance(requireContext()).getFutsalDAO()
                var lstFutsals = instance.retrieveFutsals()
                adapter = CommentFutsalAdapter(requireContext(),lstFutsals)
                withContext(Dispatchers.Main)
                {
                    recycler.adapter = adapter
                    recycler.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
                }
            }

        }
        swipe.setOnRefreshListener(this)
        return view
    }

    override fun onRefresh() {
        val intent = Intent(requireContext(),MainActivity::class.java)
        intent.putExtra("FRAGMENT_NUMBER",2)
        startActivity(intent)
        requireActivity().finish()
    }

}