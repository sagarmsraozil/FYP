package com.sagarmishra.futsal.fragments.forget_password.screens

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class EmailFragment : Fragment(),View.OnClickListener {
    private lateinit var passwordPager : ViewPager2
    private lateinit var etEmail:TextInputEditText
    private lateinit var btnSendMail : Button
    private lateinit var linearLayout:LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_email, container, false)
        passwordPager = requireActivity().findViewById(R.id.password_pager)
        etEmail = view.findViewById(R.id.etEmail)
        linearLayout = view.findViewById(R.id.linearLayout)
        etEmail.requestFocus()
        btnSendMail = view.findViewById(R.id.btnSendMail)
        btnSendMail.setOnClickListener(this)
        return view
    }

    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etEmail.text))
        {
            etEmail.error = "Insert your Email Address"
            etEmail.requestFocus()
            return false
        }

        else
        {
            return true
        }
    }

    private fun verifyEmail()
    {
        if(validation())
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.forgotPassword(etEmail.text.toString())
                    if(response.success == true)
                    {
                        RetrofitService.resetToken = response.token
                        RetrofitService.resetEmail = response.email
                        withContext(Main)
                        {
                            etEmail.text!!.clear()

                            var snack = Snackbar.make(linearLayout,"${response.message}",
                                Snackbar.LENGTH_LONG)
                            snack.setAction("Cancel",View.OnClickListener {
                                snack.dismiss()
                            })
                            snack.show()
                            passwordPager.currentItem = 1
                        }

                    }
                    else
                    {
                        withContext(Main)
                        {
                            var snack = Snackbar.make(linearLayout,"${response.message}",
                                Snackbar.LENGTH_INDEFINITE)
                            snack.setAction("Cancel",View.OnClickListener {
                                snack.dismiss()
                            })
                            snack.show()
                        }
                    }
                }
                catch (err:Exception)
                {
                    withContext(Dispatchers.Main)
                    {
                        var snack = Snackbar.make(linearLayout,"404 Server Error.\nWe will soon fix it. Thanks for your patience:)",
                            Snackbar.LENGTH_INDEFINITE)
                        snack.setAction("Cancel",View.OnClickListener {
                            snack.dismiss()
                        })
                        snack.show()
                        println(err.printStackTrace())
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnSendMail ->{
                verifyEmail()
            }
        }
    }


}