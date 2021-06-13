package com.sagarmishra.futsal.Permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class Permission(val context:Context,val activity:Activity) {
    val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun checkPermissions():Boolean
    {
        var flag = true
        for(i in permissions)
        {
            if(ActivityCompat.checkSelfPermission(context,i) != PackageManager.PERMISSION_GRANTED)
            {
                flag = false
            }
        }
        return flag
    }

    fun requestPermissions()
    {
        ActivityCompat.requestPermissions(activity,permissions,1)
    }
}