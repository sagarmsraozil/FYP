package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.response.GiveawayDistinctResponse
import com.sagarmishra.futsal.response.GiveawayResponse
import com.sagarmishra.futsal.response.OngoingGiveawayResponse
import retrofit2.Response
import retrofit2.http.*

interface GiveawayAPI {
    @GET("giveAwayDistinct")
    suspend fun getDistinctGiveaway():Response<GiveawayDistinctResponse>

    @GET("giveawayInstances")
    suspend fun getGiveaway():Response<GiveawayResponse>

    @GET("getOngoingCount")
    suspend fun getOngoingCount():Response<OngoingGiveawayResponse>

    @GET("myParticipantRecord")
    suspend fun getMyParticipantRecord(
        @Header("Authorization") token:String
    ):Response<GiveawayDistinctResponse>


    @POST("participateGiveaway/{giveCode}")
    suspend fun participateGiveAway(
        @Header("Authorization") token:String,
        @Path("giveCode") code:String
    ):Response<GiveawayResponse>
}