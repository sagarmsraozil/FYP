package com.sagarmishra.futsal.fragments.forget_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.adapter.RenewPasswordAdapter
import com.sagarmishra.futsal.fragments.forget_password.screens.ConfirmationFragment
import com.sagarmishra.futsal.fragments.forget_password.screens.EmailFragment
import com.sagarmishra.futsal.fragments.forget_password.screens.RenewPasswordFragment


class ForgetPasswordControllerFragment : Fragment() {
    private lateinit var passwordPager : ViewPager2
    private lateinit var adapter : RenewPasswordAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forget_password_controller, container, false)
        passwordPager = view.findViewById(R.id.password_pager)
        var fragList : MutableList<Fragment> = mutableListOf(
            EmailFragment(),ConfirmationFragment(),RenewPasswordFragment()
        )
        adapter = RenewPasswordAdapter(requireActivity().supportFragmentManager,lifecycle,fragList)
        passwordPager.adapter = adapter
        passwordPager.isUserInputEnabled = false
        return view
    }


}