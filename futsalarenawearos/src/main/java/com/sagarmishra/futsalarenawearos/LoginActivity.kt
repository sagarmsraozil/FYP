package com.sagarmishra.futsalarenawearos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsalarenawearos.api.ServiceBuilder

import com.sagarmishra.futsalarenawearos.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

@Suppress("Deprecation")
class LoginActivity : WearableActivity(),View.OnClickListener {
    private lateinit var etUsername:TextInputEditText
    private lateinit var etPassword:TextInputEditText
    private lateinit var btnLogin : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding()
        btnLogin.setOnClickListener(this)
        // Enables Always-on
        setAmbientEnabled()
    }

    private fun binding()
    {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
    }

    private fun saveToPref()
    {
        var watchPref = getSharedPreferences("watchCredentials",Context.MODE_PRIVATE)
        var editor = watchPref.edit()
        editor.putString("username",etUsername.text.toString())
        editor.putString("password",etPassword.text.toString())
        editor.apply()
    }

    private fun authenticateUser()
    {
        if(TextUtils.isEmpty(etUsername.text))
        {
            etUsername.error = "Insert Username"
            etUsername.requestFocus()
        }
        else if(TextUtils.isEmpty(etPassword.text))
        {
            etPassword.error = "Insert Password"
            etPassword.requestFocus()
        }
        else
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.authenticateUser(etUsername.text.toString(),etPassword.text.toString())
                    if(response.success == true)
                    {
                        saveToPref()
                        ServiceBuilder.token = "Bearer "+response.token
                        ServiceBuilder.user = response.data
                        withContext(Dispatchers.Main)
                        {

                            val intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                            startActivity(intent)
                        }

                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            etUsername.snackbar("${response.message}")
                        }
                    }
                }
                catch (ex:Exception)
                {
                    println(ex.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        etUsername.snackbar("There is a maintenance break.")
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnLogin ->{
                authenticateUser()
            }
        }
    }
}