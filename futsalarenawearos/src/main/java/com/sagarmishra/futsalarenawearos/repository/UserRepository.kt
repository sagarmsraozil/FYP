package com.sagarmishra.futsalarenawearos.repository

import com.sagarmishra.futsalarenawearos.api.ApiRequest
import com.sagarmishra.futsalarenawearos.api.ServiceBuilder
import com.sagarmishra.futsalarenawearos.api.UserAPI
import com.sagarmishra.futsalarenawearos.response.LoginResponse

class UserRepository():ApiRequest()
{
    val userAPI = ServiceBuilder.retroService(UserAPI::class.java)

    suspend fun authenticateUser(username:String,password:String):LoginResponse
    {
        return apiRequest {
            userAPI.authenticateUser(username,password)
        }
    }
}
