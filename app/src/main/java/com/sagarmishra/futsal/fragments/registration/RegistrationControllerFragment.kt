package com.sagarmishra.futsal.fragments.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.adapter.RegistrationPagerAdapter


class RegistrationControllerFragment : Fragment() {
    private lateinit var vp:ViewPager2
    private lateinit var adapter : RegistrationPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registration_controller, container, false)
        vp = view.findViewById(R.id.vp)
        var lstFragment:MutableList<Fragment> = mutableListOf(
            RegistrationFormFragment(),
            ProfilePictureFragment()
        )
        adapter = RegistrationPagerAdapter(requireActivity().supportFragmentManager,lifecycle,lstFragment)
        vp.adapter = adapter
        vp.isUserInputEnabled=false
        return view
    }


}