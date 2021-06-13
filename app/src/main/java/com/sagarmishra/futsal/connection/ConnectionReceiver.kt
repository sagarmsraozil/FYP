package com.sagarmishra.futsal.connection

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.Permissions.AutoLogin
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData

class ConnectionReceiver(val activity: Activity) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val conn = Connection(context)
        val userData = AutoLogin(activity)
        if(conn.getConnection())
        {
            RetrofitService.online = true
            userData.userLogin()
            Toast.makeText(context, "You are online!!", Toast.LENGTH_SHORT).show()
        }
        else
        {
            RetrofitService.online = false
            Toast.makeText(context, "You are offline!!", Toast.LENGTH_SHORT).show()

        }
    }

}