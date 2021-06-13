package com.sagarmishra.futsal.fragments.edit_details

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.LoginActivity
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChangePasswordFragment : Fragment(),View.OnClickListener {
    private lateinit var etCurrent : TextInputEditText
    private lateinit var etPw : TextInputEditText
    private lateinit var etCpw : TextInputEditText
    private lateinit var btnChange : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)
        binding(view)
        btnChange.setOnClickListener(this)
        return view
    }

    private fun binding(v:View)
    {
        etCurrent = v.findViewById(R.id.etCurrent)
        etPw = v.findViewById(R.id.etPw)
        etCpw = v.findViewById(R.id.etCpw)
        btnChange = v.findViewById(R.id.btnChange)
    }

    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etCurrent.text))
        {
            etCurrent.error = "Insert current password"
            etCurrent.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etPw.text))
        {
            etPw.error = "Insert new password"
            etPw.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etCpw.text))
        {
            etCpw.error = "Insert confirm password"
            etCpw.requestFocus()
            return false
        }
        else if(etPw.text.toString().length < 6)
        {
            etPw.error = "Password length must be atleast of 6 characters"
            etPw.requestFocus()
            return false
        }
        else if(etPw.text.toString() != etCpw.text.toString())
        {
            etCpw.error = "Password Mismatch"
            etCpw.requestFocus()
            return false
        }
        else
        {
            return true
        }

    }
    private fun clearPreferences()
    {
        var pref = requireActivity().getSharedPreferences("credentials",Context.MODE_PRIVATE)
        var editor = pref.edit()
        editor.putString("username","")
        editor.putString("password","")
        editor.putString("fn","")
        editor.putString("ln","")
        editor.putString("gender","")
        editor.putString("phone","")
        editor.putString("email","")
        editor.putString("dob","")
        editor.putString("address","")
        editor.apply()
    }
    private fun alert(title:String,msg:String)
    {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("Ok")
            {
                    dialog: DialogInterface?, which: Int ->
                clearPreferences()
                val intent = Intent(requireContext(), LoginActivity::class.java)

                startActivity(intent)
                requireActivity().finish()

            }






        var alertWork : AlertDialog = builder.create()
        alertWork.setCancelable(false)
        alertWork.show()
    }


    private fun changePassword()
    {
        if(validation())
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.changePassword(etCurrent.text.toString(),etPw.text.toString(),etCpw.text.toString())
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            alert("Password Changed!!","Login with new Password")
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            etPw.snackbar("${response.message}")
                        }
                    }
                }
                catch (ex:Exception)
                {
                    withContext(Dispatchers.Main)
                    {
                        etPw.snackbar("Sorry! We are having some problem!!")
                    }

                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnChange ->{

                if(RetrofitService.online == true)
                {
                    changePassword()
                }
                else
                {
                    btnChange.snackbar("No Internet Connection")
                }
            }
        }
    }

}