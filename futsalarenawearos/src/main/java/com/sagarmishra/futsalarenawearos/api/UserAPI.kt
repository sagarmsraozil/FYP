package com.sagarmishra.futsalarenawearos.api

import com.sagarmishra.futsalarenawearos.response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun authenticateUser(
        @Field("un") username:String,
        @Field("pw") password:String
    ):Response<LoginResponse>
}