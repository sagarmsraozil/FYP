package com.sagarmishra.futsal.fragments.verticalNav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.GiveawayRepository
import com.sagarmishra.futsal.adapter.GiveawayWrapperAdapter
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class GiveawayFragment : Fragment() {
    private lateinit var progressCount : TextView
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : GiveawayWrapperAdapter
    var lstCode: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_giveaway, container, false)
        progressCount = view.findViewById(R.id.progressCount)
        recycler = view.findViewById(R.id.recycler)
        initialize()
        return view
    }

    private  fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = GiveawayRepository()
                val response = repo.getOngoing()
                if(response.success == true)
                {
                    withContext(Dispatchers.Main)
                    {
                        progressCount.text = "${response.data} giveaway in progress"
                    }

                }
            }
            catch (ex: Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    recycler.snackbar(ex.toString())
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = GiveawayRepository()
                val response = repo.getDistinctGiveaway()
                if(response.success == true)
                {
                    lstCode = response.data!!

                    adapter  = GiveawayWrapperAdapter(requireActivity(),requireContext(),lstCode)
                    withContext(Dispatchers.Main)
                    {
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext())
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        recycler.snackbar(response.message!!)
                    }
                }
            }
            catch (ex: Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    recycler.snackbar(ex.toString())
                }
            }
        }
    }


}