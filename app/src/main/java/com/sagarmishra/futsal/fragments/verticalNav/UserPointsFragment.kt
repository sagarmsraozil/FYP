package com.sagarmishra.futsal.fragments.verticalNav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.adapter.WarningDotAdapter
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserPointsFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {
    private lateinit var tvFutsalPoints:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var adapter : WarningDotAdapter
    private lateinit var swipeRefresh : SwipeRefreshLayout
    private lateinit var tvFutsalPoint2 : TextView

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
        initialize()
        swipeRefresh.setOnRefreshListener(this)
        tvFutsalPoints.setOnClickListener(this)
        tvFutsalPoint2.setOnClickListener(this)
        return view
    }

    private fun initialize()
    {
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
        }
    }

}