package com.sagarmishra.futsal.fragments.forget_password.screens

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import com.sagarmishra.futsal.LoginActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class RenewPasswordFragment : Fragment() {
    private lateinit var passwordPager : ViewPager2
    private lateinit var etPassword : TextInputEditText
    private lateinit var etConfirm : TextInputEditText
    private lateinit var btnReset : Button
    private lateinit var linearLayout:LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_renew_password, container, false)
        passwordPager = requireActivity().findViewById(R.id.password_pager)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirm = view.findViewById(R.id.etConPassword)
        btnReset = view.findViewById(R.id.btnReset)
        linearLayout = view.findViewById(R.id.linearLayout)
        etPassword.requestFocus()
        btnReset.setOnClickListener {
            if(validation())
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = UserRepository()
                        val response = repo.resetPassword(etPassword.text.toString(),etConfirm.text.toString())
                        if(response.success == true)
                        {
                            withContext(Main)
                            {
                                etConfirm.text!!.clear()
                                etPassword.text!!.clear()
                                RetrofitService.resetToken = ""
                                RetrofitService.resetEmail=""
                                alert("Password Changed","${response.message}")
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
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
        return view
    }


    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etPassword.text))
        {
            etPassword.error = "Insert New Password!!"
            etPassword.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etConfirm.text))
        {
            etConfirm.error = "Confirm your new Password!!"
            etConfirm.requestFocus()
            return false
        }
        else if(etPassword.text.toString().length < 6)
        {
            etPassword.error = "Password must be atleast of 6 characters!!"
            etPassword.requestFocus()
            return false
        }

        else
        {
            return true
        }
    }

    private fun alert(title:String,msg:String)
    {
        var builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("Go to Login Page")
        {
                dialog: DialogInterface?, which: Int ->
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        var alertWork : AlertDialog = builder.create()
        alertWork.setCancelable(false)
        alertWork.show()
    }

}