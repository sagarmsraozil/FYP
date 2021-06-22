package com.sagarmishra.futsal.fragments.verticalNav

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.adapter.UserTitleAdapter
import com.sagarmishra.futsal.adapter.WarningDotAdapter
import com.sagarmishra.futsal.interfaces.RadioRefresh
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text


class UserPointsFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,RadioRefresh {
    private lateinit var tvFutsalPoints:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var adapter : WarningDotAdapter
    private lateinit var swipeRefresh : SwipeRefreshLayout
    private lateinit var tvFutsalPoint2 : TextView
    private lateinit var ivTitles : ImageView
    private lateinit var dialog:Dialog
    var selectedTitle:String = ""
    var titleViewer:MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_points, container, false)
        tvFutsalPoints = view.findViewById(R.id.tvFutsalPoints)
        recycler = view.findViewById(R.id.recycler)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        tvFutsalPoint2 = view.findViewById(R.id.tvFutsalPoints2)
        ivTitles = view.findViewById(R.id.ivTitles)
        initialize()
        swipeRefresh.setOnRefreshListener(this)
        tvFutsalPoints.setOnClickListener(this)
        tvFutsalPoint2.setOnClickListener(this)
        ivTitles.setOnClickListener(this)
        return view
    }

    private fun initialize()
    {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_title_viewer)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val tvTitles:TextView = dialog.findViewById(R.id.tvTitles)
        val ivCross : ImageView = dialog.findViewById(R.id.ivCross)
        val recyclerTitle : RecyclerView = dialog.findViewById(R.id.recycler)
        var btnSelect:Button = dialog.findViewById(R.id.btnSelect)

        if(StaticData.user!!.titles!!.size > 0)
        {
            titleViewer = StaticData.user!!.titles!!.map {
                it
            }.toMutableList()
            titleViewer.add(0,"")
            var adapterTitle:UserTitleAdapter = UserTitleAdapter(requireContext(),titleViewer,this)
            recyclerTitle.adapter = adapterTitle
            recyclerTitle.layoutManager = LinearLayoutManager(requireContext())
            tvTitles.text = "${StaticData.user!!.titles!!.size} titles"
            btnSelect.visibility = View.VISIBLE
        }
        else
        {
            tvTitles.text = "0 titles"
            btnSelect.visibility = View.GONE
        }

        ivCross.setOnClickListener {
            dialog.dismiss()
        }

        btnSelect.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = TeamRepository()
                    val response = repo.selectTitle("none",selectedTitle,"User")
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            StaticData.user!!.activeTitle = selectedTitle
                            btnSelect.snackbar(response.message!!)
                            var intent = Intent(requireContext(),MainActivity::class.java)
                            intent.putExtra("FRAGMENT_NUMBER",7)
                            startActivity(intent)

                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            btnSelect.snackbar(response.message!!)
                        }
                    }
                }
                catch(err:Exception)
                {
                    println(err.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        btnSelect.snackbar("Server Error")
                    }
                }
            }
        }


        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                val repo = UserRepository()
                val response = repo.fetchMyPoint()
                if(response.success == true)
                {
                    withContext(Dispatchers.Main)
                    {
                        tvFutsalPoints.text = response.point.toString()+" FP"
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
                    recycler.snackbar("Server Error")
                }
            }
        }
        adapter = WarningDotAdapter(requireContext(),StaticData.user!!.dots)
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(requireContext(),12,GridLayoutManager.VERTICAL,false)
    }

    override fun onRefresh() {
        val intent = Intent(requireContext(),MainActivity::class.java)
        intent.putExtra("FRAGMENT_NUMBER",7)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.tvFutsalPoints2 ->{
                tvFutsalPoints.visibility = View.VISIBLE
                tvFutsalPoint2.visibility = View.GONE
            }
            R.id.tvFutsalPoints ->{
                tvFutsalPoints.visibility = View.GONE
                tvFutsalPoint2.visibility = View.VISIBLE
            }
            R.id.ivTitles ->{
                dialog.show()
            }
        }
    }

    override fun refreshRadio(position: Int) {
        selectedTitle = titleViewer[position]
    }

}