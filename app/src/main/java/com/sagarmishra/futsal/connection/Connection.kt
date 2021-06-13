package com.sagarmishra.futsal.connection

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.provider.SyncStateContract
import android.util.Log
import android.util.Log.d
import com.google.android.gms.common.internal.Constants
import com.sagarmishra.futsal.Repository.UserRepository
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.logging.Logger


@Suppress("Deprecation")
class Connection(val context: Context) {

    fun getConnection():Boolean
    {
        var connection = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) as NetworkInfo
        val mobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) as NetworkInfo
        println(connectivityManager.activeNetworkInfo)
        if((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected()))
        {
            connection = true
        }
        return connection
    }

    fun hasInternetConnected():Boolean
    {
        //"https://www.google.com"
        var connections:Boolean = false
        if(getConnection())
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val connection = java.net.URL("http://192.168.1.68:90/media/1617027659096barca.png").openConnection() as HttpURLConnection
                    connection.setRequestProperty("User-Agent","ConnectionTest")
                    connection.setRequestProperty("Connection","close")
                    connection.connectTimeout = 1000
                    connection.connect()
                    connections = connection.responseCode == 200
                }
                catch(ex:Exception)
                {
                    Log.e("classTag","${ex.toString()}")
                    println(ex.printStackTrace())
                }
            }

        }
        else
        {
            Log.w("classTag","No Network Available")
        }
        Thread.sleep(2000)
        return connections
    }


//    fun checkServer():Boolean
//    {
//        var connection:Boolean = false
//        if(getConnection())
//        {
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    val repo = UserRepository()
//                    val response = repo.authenticateUser("test","test")
//                    connection = true
//                }
//                catch (ex:Exception)
//                {
//                    println(ex.printStackTrace())
//                }
//            }
//        }
//        Thread.sleep(2000)
//        println(connection)
//        return connection
//    }



}