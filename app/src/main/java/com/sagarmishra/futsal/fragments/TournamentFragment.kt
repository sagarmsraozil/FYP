package com.sagarmishra.futsal.fragments

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
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.LoginActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.AdsRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TournamentFragment : Fragment() {
    private lateinit var etEm : TextInputEditText
    private lateinit var btnInvite : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tournament, container, false)
        etEm = view.findViewById(R.id.etEm)
        btnInvite = view.findViewById(R.id.btnInvite)
        btnInvite.setOnClickListener {
            if(TextUtils.isEmpty(etEm.text))
            {
                etEm.error = "Provide email address"
                etEm.requestFocus()
            }
            else
            {
                if(RetrofitService.online == true)
                {
                    alert("Information","Your name and email address would be visible to invited individual.")

                }
                else
                {
                    etEm.snackbar("No Internet Connection!!")
                }
            }
        }
        return view
    }

    private fun alert(title:String,msg:String)
    {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setNeutralButton("Cancel")
        {
                dialog: DialogInterface?, which: Int ->

        }
        builder.setPositiveButton("Confirm")
        { dialogInterface: DialogInterface, i: Int ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = AdsRepository()
                    val response = repo.invite(etEm.text.toString())
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            etEm.text!!.clear()
                            etEm.snackbar("${response.message}")

                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {

                            etEm.snackbar("${response.message}")

                        }
                    }
                }
                catch (ex:Exception)
                {
                    withContext(Dispatchers.Main)
                    {
                        etEm.snackbar("${ex.toString()}")
                        println(ex.printStackTrace())
                    }
                }
            }

        }

        var alertWork : AlertDialog = builder.create()
        alertWork.setCancelable(false)
        alertWork.show()
    }



}