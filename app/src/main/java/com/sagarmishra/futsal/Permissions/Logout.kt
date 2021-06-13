package com.sagarmishra.futsal.Permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.sagarmishra.futsal.LoginActivity
import com.sagarmishra.futsal.model.StaticData

class Logout(val activity:Activity,val context: Context) {
    fun logout()
    {
        var pref = activity.getSharedPreferences("credentials",Context.MODE_PRIVATE)
        var editor = pref.edit()
//        editor.putString("username","")
//        editor.putString("password","")
//        editor.putString("fn", "")
//        editor.putString("ln", "")
//        editor.putString("gender", "")
//        editor.putString("phone", "")
//        editor.putString("email", "")
//        editor.putString("dob", "")
//        editor.putString("address", "")
        editor.clear()

        editor.apply()
        val intent = Intent(context,LoginActivity::class.java)
        context.startActivity(intent)
        activity.finish()
    }
}