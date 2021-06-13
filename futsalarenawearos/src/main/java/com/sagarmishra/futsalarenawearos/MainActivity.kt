package com.sagarmishra.futsalarenawearos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.sagarmishra.futsalarenawearos.api.ServiceBuilder

import com.sagarmishra.futsalarenawearos.repository.UserRepository
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        var watchPref = getSharedPreferences("watchCredentials",Context.MODE_PRIVATE)
//        var un = watchPref.getString("username","")
//        var pw = watchPref.getString("password","")
//        if(un!="" && pw!="")
//        {
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                val intent = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(intent)
//                try {
//                    val repo = UserRepository()
//                    val response = repo.authenticateUser(un!!,pw!!)
//                    if(response.success == true)
//                    {
//
//                        ServiceBuilder.token = "Bearer "+response.token
//                        ServiceBuilder.user = response.data
//                        withContext(Dispatchers.Main)
//                        {
//                            NotificationSender(this@MainActivity,"Logged in as ${response.data!!.firstName} ${response.data!!.lastName}","").createHighPriority()
//                            val intent = Intent(this@MainActivity,DashboardActivity::class.java)
//                            startActivity(intent)
//                        }
//
//                    }
//                    else
//                    {
//                        withContext(Dispatchers.Main)
//                        {
//                            val intent = Intent(this@MainActivity,LoginActivity::class.java)
//                            startActivity(intent)
//                        }
//                    }
//                }
//                catch (ex: Exception)
//                {
//                    println(ex.printStackTrace())
//                    withContext(Dispatchers.Main)
//                    {
//                        val intent = Intent(this@MainActivity,LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            }
        }


        // Enables Always-on
        setAmbientEnabled()
    }
}