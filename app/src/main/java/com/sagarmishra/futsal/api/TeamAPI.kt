package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.entityapi.Battle
import com.sagarmishra.futsal.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface TeamAPI {
    @Multipart
    @POST("createATeam")
    suspend fun createATeam(
        @Header("Authorization") token:String,
        @Part("teamName") teamName:RequestBody,
        @Part("teamTag") teamTag:RequestBody,
        @Part("locatedCity") locatedCity:RequestBody,
        @Part("ageGroup") ageGroup:RequestBody,
        @Part("contact") contact:RequestBody,
        @Part("teamEmail") teamEmail:RequestBody,
        @Part citizenShipBack:MultipartBody.Part,
        @Part citizenShipFront:MultipartBody.Part,
        @Part ownerPhoto:MultipartBody.Part
    ):Response<GlobalResponse>

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

    @GET("myTeamTierHistory/{tid}")
    suspend fun myTeamTierHistory(
        @Path("tid") tid:String
    ):Response<TeamStatsHistory>

    @FormUrlEncoded
    @POST("findMatchingTeam")
    suspend fun findMatchingTeam(
        @Header("Authorization") token:String,
        @Field("tid") tid:String
    ):Response<MatchMakingResponse>

    @FormUrlEncoded
    @POST("randomMatching")
    suspend fun randomMatching(
        @Header("Authorization") token:String,
        @Field("tid") tid:String
    ):Response<MatchMakingResponse>

    @FormUrlEncoded
    @POST("requestAMatch")
    suspend fun requestAMatch(
        @Header("Authorization") token:String,
        @Field("homeId")hid:String,
        @Field("awayId")aid:String
    ):Response<GlobalResponse>

    @GET("fetchEveryTeams")
    suspend fun fetchEveryTeams(

    ):Response<EveryTeamResponse>

    @GET("fetchReqNeedTeam/{tid}")
    suspend fun fetchSingleTeam(
        @Path("tid") tid:String
    ):Response<TeamResponse>

    @FormUrlEncoded
    @POST("requestToJoin")
    suspend fun requestToJoin(
        @Header("Authorization") token:String,
        @Field("teamId") id:String
    ):Response<GlobalResponse>

    @POST("setABattle")
    suspend fun setABattle(
        @Header("Authorization") token:String,
        @Body battle:Battle
    ):Response<GlobalResponse>

    @FormUrlEncoded
    @POST("teamDetailsUpdateRequest")
    suspend fun detailsUpdate(
        @Header("Authorization") token:String,
        @Field("locatedCity") locatedCity:String,
        @Field("teamEmail") email:String,
        @Field("contact") contact:String,
        @Field("ageGroup") age:String
    ):Response<GlobalResponse>

    @Multipart
    @PUT("uploadTeamLogo")
    suspend fun uploadLogo(
        @Header("Authorization") token:String,
        @Part teamLogo : MultipartBody.Part,
        @Part("tid") tid:RequestBody
    ):Response<GlobalResponse>

}