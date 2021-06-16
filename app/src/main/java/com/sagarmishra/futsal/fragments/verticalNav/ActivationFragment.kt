package com.sagarmishra.futsal.fragments.verticalNav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
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
import java.lang.Exception


class ActivationFragment : Fragment(),View.OnClickListener {
    private lateinit var etOne:EditText
    private lateinit var etTwo:EditText
    private lateinit var etThree:EditText
    private lateinit var etFour:EditText
    private lateinit var btnActivate : Button
    private lateinit var resendCode : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_activation, container, false)
        binding(view)
        typeListener()
        etOne.requestFocus()
        return view
    }

    private fun typeListener()
    {
        btnActivate.setOnClickListener(this)
        resendCode.setOnClickListener(this)
        etOne.doAfterTextChanged {
            if(etOne.text.toString().trim().length > 0)
            {
                etTwo.requestFocus()
            }
        }
        etTwo.doAfterTextChanged {
            if(etTwo.text.toString().trim().length > 0)
            {
                etThree.requestFocus()
            }
        }
        etThree.doAfterTextChanged {
            if(etThree.text.toString().trim().length > 0)
            {
                etFour.requestFocus()
            }
        }
    }

    private fun binding(v:View)
    {
        etOne = v!!.findViewById(R.id.etOne)
        etTwo = v!!.findViewById(R.id.etTwo)
        etThree = v!!.findViewById(R.id.etThree)
        etFour = v!!.findViewById(R.id.etFour)
        btnActivate = v!!.findViewById(R.id.btnActivate)
        resendCode = v!!.findViewById(R.id.resendCode)
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnActivate ->{
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = UserRepository()
                        val response = repo.verifyActivation(etOne.text.toString(),etTwo.text.toString(),etThree.text.toString(),etFour.text.toString(),RetrofitService.resetToken!!)
                        if(response.success == true)
                        {
                            StaticData.user = response.data
                            withContext(Dispatchers.Main)
                            {
                                etOne.snackbar("${response.message}")
                                var intent = Intent(requireContext(),MainActivity::class.java)
                                intent.putExtra("FRAGMENT_NUMBER",0)
                                startActivity(intent)
                            }

                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                etOne.snackbar("${response.error!!["random"]}")
                                etOne.text!!.clear()
                                etTwo.text!!.clear()
                                etThree.text!!.clear()
                                etFour.text!!.clear()
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            etOne.snackbar("Server Error")
                        }
                    }
                }
            }

            R.id.resendCode ->{
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = UserRepository()
                        val response = repo.accountActivation()
                        if(response.success == true)
                        {
                            RetrofitService.resetToken = response.pinCode
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                etOne.snackbar("${response.message}")
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            etOne.snackbar("Server Error")
                        }
                    }
                }
            }
        }
    }


}