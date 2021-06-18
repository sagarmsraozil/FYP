package com.sagarmishra.futsal.fragments.request_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R


class PlayerFragment : Fragment() {
    private lateinit var tvRequests:TextView
    private lateinit var recycler:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        tvRequests = view.findViewById(R.id.tvRequests)
        recycler = view.findViewById(R.id.recycler)

        return view
    }

}