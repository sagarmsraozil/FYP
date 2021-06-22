package com.sagarmishra.futsal.fragments.challenges_and_titles

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.TitleSetAdapter
import com.sagarmishra.futsal.interfaces.RadioRefresh
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TitlesFragment : Fragment(),RadioRefresh,View.OnClickListener {
    var titleSelected:String = ""
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:TitleSetAdapter
    private lateinit var tvTitles: TextView
    private lateinit var btnUse:Button
    private lateinit var tvTier:TextView
    private lateinit var tvCollectTitles:TextView
    var titles:MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_titles, container, false)
        recycler = view.findViewById(R.id.recycler)
        tvTitles = view.findViewById(R.id.tvTitles)
        btnUse = view.findViewById(R.id.btnUse)
        tvTier = view.findViewById(R.id.tvTier)
        tvCollectTitles  = view.findViewById(R.id.tvCollectTitles)
        btnUse.setOnClickListener(this)
        tvCollectTitles.setOnClickListener(this)
        tvTitles.text = "${StaticData.team!!.titles!!.size} titles"
        titles = StaticData.team!!.titles!!.map{
            it
        }.toMutableList()
        titles.add(0,"")
        if(StaticData.matchPlayed == "Zero")
        {
            tvTier.text = "Current Tier: ${StaticData.myTier}"
        }
        else
        {
            tvTier.text = "Current Tier: ${StaticData.myTier}(${StaticData.matchPlayed}/5)"
        }


        if(StaticData.team!!.titles!!.size > 0)
        {
            btnUse.visibility = View.VISIBLE
            adapter = TitleSetAdapter(requireActivity(),titles,this)
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireContext())
        }
        else
        {
            btnUse.visibility = View.GONE
        }

        if(StaticData.titleReceiveCount > 0)
        {
            tvCollectTitles.visibility = View.VISIBLE
        }
        else
        {
            tvCollectTitles.visibility = View.GONE
        }

        return view
    }

    override fun refreshRadio(position: Int) {
        titleSelected = titles[position]

    }

    override fun onClick(v: View?) {
       when(v!!.id)
       {
           R.id.btnUse ->{
               CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.selectTitle(StaticData.team!!._id,titleSelected,"Team")
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                btnUse.snackbar(response.message!!)
                                StaticData.team!!.activeTitle = titleSelected
                                adapter.notifyDataSetChanged()
                                var intent = Intent(requireContext(),MainActivity::class.java)
                                intent.putExtra("FRAGMENT_NUMBER",8)
                                startActivity(intent)
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                btnUse.snackbar(response.message!!)
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            btnUse.snackbar("Server Error!!")
                        }
                    }
               }
           }

           R.id.tvCollectTitles ->{
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.collectTitle(StaticData.team!!._id)
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                btnUse.snackbar(response.message!!)
                                var intent = Intent(requireContext(),MainActivity::class.java)
                                intent.putExtra("FRAGMENT_NUMBER",8)
                                startActivity(intent)
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                btnUse.snackbar(response.message!!)
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            btnUse.snackbar("Server Error!!")
                        }
                    }

                }
           }
       }
    }


}