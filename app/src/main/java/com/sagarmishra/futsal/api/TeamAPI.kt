package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.response.BattleResponse
import com.sagarmishra.futsal.response.GlobalResponse
import com.sagarmishra.futsal.response.TeamResponse
import com.sagarmishra.futsal.response.TeamStatsResponse
import retrofit2.Response
import retrofit2.http.*

interface TeamAPI {
    @GET("getMyTeam")
    suspend fun getMyTeam(
        @Header("Authorization") token:String
    ):Response<TeamResponse>

    @GET("teamStats/{tid}")
    suspend fun getTeamStats(
        @Path("tid") id:String
    ):Response<TeamStatsResponse>

    @FormUrlEncoded
    @POST("promoteToColeader")
    suspend fun promoteToColeader(
        @Header("Authorization") token:String,
        @Field("tid") teamId:String,
        @Field("uid") uid:String
    ):Response<GlobalResponse>

    @FormUrlEncoded
    @POST("kickAPlayer")
    suspend fun kickPlayer(
        @Header("Authorization") token:String,
        @Field("tid") teamId:String,
        @Field("uid") uid:String
    ):Response<GlobalResponse>

    @FormUrlEncoded
    @POST("demoteColeader")
    suspend fun demotePlayer(
        @Header("Authorization") token:String,
        @Field("tid") tid:String,
        @Field("uid") uid:String,

    ):Response<GlobalResponse>

    @FormUrlEncoded
    @POST("promoteToLeader")
    suspend fun promoteLeader(
        @Header("Authorization") token:String,
        @Field("uid") uid:String
    ):Response<GlobalResponse>

    @POST("disbandTeam")
    suspend fun disbandTeam(
        @Header("Authorization") token:String
    ):Response<GlobalResponse>

    @POST("leaveTeam")
    suspend fun leaveTeam(
        @Header("Authorization") token:String
    ):Response<GlobalResponse>

    @GET("fetchBattles/{tid}")
    suspend fun fetchBattles(
        @Path("tid") tid:String
    ):Response<BattleResponse>

    @FormUrlEncoded
    @POST("deleteBattle")
    suspend fun deleteBattle(
        @Header("Authorization") token:String,
        @Field("bid") bid:String
    ):Response<GlobalResponse>
}