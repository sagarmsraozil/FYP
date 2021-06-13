package com.sagarmishra.futsal.fragments.viewpager_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.adapter.ViewPagerAdapter
import com.sagarmishra.futsal.fragments.viewpager_screens.flash_screens.FirstFragment
import com.sagarmishra.futsal.fragments.viewpager_screens.flash_screens.SecondFragment
import com.sagarmishra.futsal.fragments.viewpager_screens.flash_screens.ThirdFragment
import me.relex.circleindicator.CircleIndicator3


class ViewFragment : Fragment() {

    private lateinit var vp : ViewPager2
    private lateinit var indicator : CircleIndicator3
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view, container, false)
        vp = view.findViewById(R.id.vp)
        indicator = view.findViewById(R.id.indicator)
        var lstFragment : MutableList<Fragment> = mutableListOf(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )

        var adapter = ViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle,lstFragment)

        vp.adapter = adapter
        indicator.setViewPager(vp)
        return view
    }


}