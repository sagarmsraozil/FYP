package com.sagarmishra.futsal.fragments.booking_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.adapter.BookingPagerAdapter


class BookingControllerFragment : Fragment() {
    private lateinit var vp:ViewPager2
    private lateinit var adapter:BookingPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booking_controller, container, false)
        vp = view.findViewById(R.id.vp)
        var lstFragments : MutableList<Fragment> = mutableListOf(
            BookingFormFragment(),
            SuccessBookingFragment()
        )
        adapter = BookingPagerAdapter(requireActivity().supportFragmentManager,lifecycle,lstFragments)
        vp.adapter = adapter
        vp.isUserInputEnabled = false

        return view
    }

}