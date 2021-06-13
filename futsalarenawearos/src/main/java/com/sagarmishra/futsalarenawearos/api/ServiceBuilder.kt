package com.sagarmishra.futsalarenawearos.api

import com.sagarmishra.futsalarenawearos.entity.AuthUser
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val BASE_URL = "http://10.0.2.2:90/"
    //private const val BASE_URL = "http://localhost:90/"
//private const val BASE_URL = "http://192.168.1.68:90/"
    var token:String?=null
    var user: AuthUser?=null
    val okhttp = OkHttpClient.Builder()
    val retrofitBuild = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttp.build())

    var retrofitService = retrofitBuild.build()

    fun loadImagePath():String
    {
        var urlSplit = BASE_URL.split("/")
        return urlSplit[0]+"/"+urlSplit[2]+"/"
    }

    fun <T> retroService(service:Class<T>):T
    {
        return retrofitService.create(service)
    }
}