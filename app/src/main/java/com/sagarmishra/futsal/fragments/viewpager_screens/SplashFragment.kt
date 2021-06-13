package com.sagarmishra.futsal.fragments.viewpager_screens

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.LoginActivity
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.connection.CheckConnection
import com.sagarmishra.futsal.connection.Connection
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.*
import java.lang.Exception


class SplashFragment : Fragment() {
    private lateinit var tv2:TextView
    private var username:String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        tv2 = view.findViewById(R.id.textView2)
        val pref = requireActivity().getSharedPreferences("credentials",Context.MODE_PRIVATE)
        username = pref.getString("username","")!!
        val password = pref.getString("password","")
        CheckConnection(requireContext(),requireActivity()).checkRegisteredNetwork()
        CoroutineScope(Dispatchers.Main).launch {
            delay(2350)
            if(!introChecked())
            {
                findNavController().navigate(R.id.action_splashFragment_to_viewFragment)
            }
            else
            {

                if(RetrofitService.online == false)
                {
                    showAlert()
                }
                else
                {
                    if(username !="" && password != "")
                    {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val repo = UserRepository()
                                val response = repo.authenticateUser(username!!,password!!)
                                if(response.success == true)
                                {
                                    RetrofitService.token = "Bearer "+response.token
                                    StaticData.user = response.data

                                    val intent = Intent(context, MainActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                                else
                                {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            }
                            catch (err:Exception)
                            {
                                println(err.printStackTrace())
                                withContext(Dispatchers.Main)
                                {
                                    tv2.snackbar("There is a maintenance break")
                                }

                            }
                        }
                    }
                    else
                    {
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }

                }

            }


        }
        return view
    }

    private fun showAlert()
    {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Futsal Arena")
        builder.setMessage("There is no internet connection in your device.")
        builder.setPositiveButton("Connect"){ dialogInterface: DialogInterface, i: Int ->
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
            requireActivity().finish()
        }
        builder.setNegativeButton("Cancel"){ dialogInterface: DialogInterface, i: Int ->
            if(username != "")
            {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            else
            {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

        }

        val alert = builder.create()
        alert.setCancelable(false)
        alert.show()
    }

    private fun introChecked():Boolean
    {
        var getPref = requireActivity().getSharedPreferences("intro",Context.MODE_PRIVATE)
        var checked = getPref.getBoolean("checked",false)
        return checked
    }

    override fun onDestroy() {
        super.onDestroy()
        CheckConnection(requireContext(),requireActivity()).unregisteredNetwork()
    }


}