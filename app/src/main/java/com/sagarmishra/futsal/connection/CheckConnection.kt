package com.sagarmishra.futsal.connection

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager

class CheckConnection(val context: Context,val activity:Activity) {
    var broadcastReceiver: BroadcastReceiver = ConnectionReceiver(activity)

    fun checkRegisteredNetwork()
    {
        context.registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }


    fun unregisteredNetwork()
    {
        try {
            context.unregisterReceiver(broadcastReceiver)
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }
}