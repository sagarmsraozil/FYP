package com.sagarmishra.futsal.fragments.viewpager_screens.flash_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.sagarmishra.futsal.R


class SecondFragment : Fragment() {

    private lateinit var vp : ViewPager2
    private lateinit var btnNext : Button
    private lateinit var btnPrevious : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        vp = requireActivity().findViewById(R.id.vp)
        btnNext = view.findViewById(R.id.btnNext)
        btnPrevious = view.findViewById(R.id.btnPrevious)
        btnNext.setOnClickListener {
            vp.currentItem = 2
        }

        btnPrevious.setOnClickListener {
            vp.currentItem = 0
        }


        return view
    }


}