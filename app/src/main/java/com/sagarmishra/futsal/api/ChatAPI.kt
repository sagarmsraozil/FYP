package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.response.ChatResponse
import com.sagarmishra.futsal.response.SingleChatResponse
import com.sagarmishra.futsal.response.TeamRequireResponse
import com.sagarmishra.futsal.response.UnseenResponse
import retrofit2.Response
import retrofit2.http.*

interface ChatAPI {
    @GET("teamRequiredPlayers")
    suspend fun teamRequiredPlayers(
        @Header("Authorization") token:String
    ):Response<TeamRequireResponse>

    @GET("myInteractions")
    suspend fun myInteractions(
        @Header("Authorization") token:String
    ):Response<TeamRequireResponse>

    @GET("fetchMyUnseen")
    suspend fun fetchMyUnseen(
        @Header("Authorization") token:String
    ):Response<UnseenResponse>

    @FormUrlEncoded
    @POST("getChatMessages")
    suspend fun getChatMessages(
        @Header("Authorization") token:String,
        @Field("receiver") rid:String
    ):Response<ChatResponse>

    @FormUrlEncoded
    @POST("sendMessage")
    suspend fun sendMessage(
        @Header("Authorization") token:String,
        @Field("receiverId") rid:String,
        @Field("message") message:String
    ):Response<SingleChatResponse>
}