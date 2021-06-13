package com.sagarmishra.futsal.Permissions

import android.app.Activity
import android.content.Context
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class AutoLogin(val activity: Activity) {
    fun userLogin()
    {
        var pref = activity.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        var username = pref.getString("username","")
        var password = pref.getString("password","")

        if(username != "" && password != "")
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.authenticateUser(username!!,password!!)
                    if(response.success == true)
                    {
                        RetrofitService.token = "Bearer "+response.token
                        StaticData.user = response.data

                    }
                    else
                    {
                        println(response.message)
                    }
                }

                catch (ex:Exception)
                {
                    println(ex.toString())
                }
            }
        }
    }

}