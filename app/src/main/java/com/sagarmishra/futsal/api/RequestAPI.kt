package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.response.GlobalResponse
import com.sagarmishra.futsal.response.MatchRequestResponse
import com.sagarmishra.futsal.response.PlayerRequestResponse
import retrofit2.Response
import retrofit2.http.*

interface RequestAPI {
    @GET("getRequests/{tid}")
    suspend fun getUserRequests(
        @Path("tid") tid:String
    ):Response<PlayerRequestResponse>

    @FormUrlEncoded
    @POST("respondToRequest")
    suspend fun respondToPlayerRequest(
        @Header("Authorization") token:String,
        @Field("respond") respond:String,
        @Field("requestId") id:String
    ):Response<GlobalResponse>

    @GET("checkRequestsByTeam/{tid}")
    suspend fun myRequestForMatch(
        @Path("tid") id:String
    ):Response<MatchRequestResponse>

    @GET("showRequestsToMyTeam/{tid}")
    suspend fun requestsToMyTeam(
        @Path("tid") id:String
    ):Response<MatchRequestResponse>

    @FormUrlEncoded
    @PUT("teamDecision")
    suspend fun battleDecision(
        @Header("Authorization") token:String,
        @Field("rid") rid:String,
        @Field("tid") tid:String,
        @Field("respond") respond:String
    ):Response<GlobalResponse>
}