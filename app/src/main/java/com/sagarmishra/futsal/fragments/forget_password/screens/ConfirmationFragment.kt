package com.sagarmishra.futsal.fragments.forget_password.screens

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityroom.User
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.lang.Exception


class ConfirmationFragment : Fragment(),View.OnClickListener {
    private lateinit var passwordPager: ViewPager2
    private lateinit var etFirst: EditText
    private lateinit var etSecond: EditText
    private lateinit var etThird: EditText
    private lateinit var etFourth: EditText
    private lateinit var btnConfirm: Button
    private lateinit var btnPrevious: Button
    private lateinit var constraint : ConstraintLayout
    private lateinit var tvResend:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_confirmation, container, false)
        passwordPager = requireActivity().findViewById(R.id.password_pager)
        binding(view)
        initialize()
        btnConfirm.setOnClickListener(this)
        btnPrevious.setOnClickListener(this)
        tvResend.setOnClickListener(this)
        return view
    }

    private fun binding(v: View?) {
        etFirst = v!!.findViewById(R.id.etFirst)
        etSecond = v!!.findViewById(R.id.etSecond)
        etThird = v!!.findViewById(R.id.etThird)
        etFourth = v!!.findViewById(R.id.etFourth)
        btnConfirm = v!!.findViewById(R.id.btnConfirm)
        btnPrevious = v!!.findViewById(R.id.btnPrevious)
        constraint = v!!.findViewById(R.id.constraint)
        tvResend = v!!.findViewById(R.id.tvResend)

    }

    private fun initialize() {
        etFirst.doOnTextChanged { text, start, before, count ->
            if (etFirst.text.toString().length > 0) {
                etSecond.requestFocus()
            }
        }
        etSecond.doOnTextChanged { text, start, before, count ->
            if (etSecond.text.toString().length > 0) {
                etThird.requestFocus()
            }
        }
        etThird.doOnTextChanged { text, start, before, count ->

            if (etThird.text.toString().length > 0) {
                etFourth.requestFocus()
            }
        }

    }

    private fun verifyAndProgress()
    {
        if(validation())
        {
            val userCode = etFirst.text.toString()+etSecond.text.toString()+etThird.text.toString()+etFourth.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.verifyReset(userCode)
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            passwordPager.currentItem = 2
                        }

                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            etThird.snackbar("${response.message}")
                        }
                    }
                }
                catch(ex:Exception)
                {
                    println(ex.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        etThird.snackbar("Server Error")
                    }
                }

            }
        }
    }
    private fun msg(txt:String)
    {
        val snk = Snackbar.make(constraint,txt,Snackbar.LENGTH_INDEFINITE);
        snk.setAction("Ok",View.OnClickListener {
            snk.dismiss()
        })
        snk.show()
    }
    private fun validation():Boolean
    {
        var txt = "Please insert every field!!"
        if(TextUtils.isEmpty(etFirst.text))
        {
            msg(txt)
            return false
        }
        else if(TextUtils.isEmpty(etSecond.text))
        {
            msg(txt)
            return false
        }
        else if(TextUtils.isEmpty(etThird.text))
        {
            msg(txt)
            return false
        }
        else if(TextUtils.isEmpty(etFourth.text))
        {
            msg(txt)
            return false
        }
        else
        {
            return true
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnPrevious ->{
                passwordPager.currentItem = 0
            }

            R.id.btnConfirm ->{
                verifyAndProgress()
            }

            R.id.tvResend ->{
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = UserRepository()
                        val response = repo.forgotPassword(RetrofitService.resetEmail!!)
                        if(response.success == true)
                        {
                            RetrofitService.resetToken = response.token;
                            RetrofitService.resetEmail = response.email;
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                tvResend.snackbar("${response.message}")
                            }
                        }
                    }
                    catch (err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            tvResend.snackbar("Server Error")
                        }
                    }
                }
            }
        }
    }
}
