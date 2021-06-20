package com.sagarmishra.futsal.fragments.team_update

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class TeamDetailsUpdateFragment : Fragment(),View.OnClickListener {
    private lateinit var etAddress:TextInputEditText
    private lateinit var etContact:TextInputEditText
    private lateinit var etAge:TextInputEditText
    private lateinit var etEmail:TextInputEditText
    private lateinit var btnEdit:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team_details_update, container, false)
        binding(view)
        initialize()
        btnEdit.setOnClickListener(this)
        return view
    }

    private fun binding(v:View)
    {
        etAddress = v!!.findViewById(R.id.etAddress)
        etContact = v!!.findViewById(R.id.etContact)
        etAge = v!!.findViewById(R.id.etAge)
        etEmail = v!!.findViewById(R.id.etEmail)
        btnEdit = v!!.findViewById(R.id.btnEdit)
    }


    private fun initialize()
    {
        etAddress.setText(StaticData.team!!.locatedCity)
        etContact.setText(StaticData.team!!.contact)
        etAge.setText(StaticData.team!!.ageGroup.toString())
        etEmail.setText(StaticData.team!!.teamEmail)

    }


    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etAddress.text))
        {
            etAddress.error = "Insert Address"
            etAddress.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etContact.text))
        {
            etContact.error = "Insert Contact"
            etContact.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etAge.text))
        {
            etAge.error = "Insert Age"
            etAge.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etEmail.text))
        {
            etEmail.error = "Enter Email"
            etEmail.requestFocus()
            return false
        }
        else
        {
            return true
        }
    }
    private fun alert(title:String,message:String)
    {
        var alert = AlertDialog.Builder(requireContext())
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("Ok"){ dialogInterface: DialogInterface, i: Int ->

        }

        val alertD = alert.create()
        alertD.setCancelable(false)
        alertD.show()
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnEdit ->{
                if(validation())
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = TeamRepository()
                            val response = repo.detailUpdate(etAddress.text.toString(),etEmail.text.toString(),etContact.text.toString(),etAge.text.toString())
                            if(response.success == true)
                            {
                                withContext(Dispatchers.Main)
                                {
                                    etAddress.snackbar(response.message!!)
                                }
                            }
                            else
                            {
                                withContext(Dispatchers.Main)
                                {
                                    var errorBox = response.error!!.values.toMutableList().joinToString("\n")
                                    alert("Error",errorBox)
                                }
                            }
                        }
                        catch(err:Exception)
                        {
                            println(err.printStackTrace())
                            withContext(Dispatchers.Main)
                            {
                                etAddress.snackbar("Server Error!!")
                            }
                        }
                    }
                }
            }
        }

    }


}