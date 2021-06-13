package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.response.APIResponse
import com.sagarmishra.futsal.response.AdsResponse
import retrofit2.Response
import retrofit2.http.*

interface AdAPI {
    @GET("ads")
    suspend fun retrieveAds():Response<AdsResponse>

    @FormUrlEncoded
    @POST("insert/invitation")
    suspend fun invite(
        @Header("Authorization") token:String,
        @Field("email") email:String
    ):Response<APIResponse>
}