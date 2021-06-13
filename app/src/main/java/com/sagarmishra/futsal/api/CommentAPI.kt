package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.entityapi.FutsalComment
import com.sagarmishra.futsal.response.FutsalCommentResponse
import retrofit2.Response
import retrofit2.http.*

interface CommentAPI {
    @FormUrlEncoded
    @POST("comment/futsal")
    suspend fun addComment(
        @Header("Authorization") token:String,
        @Field("futsal_id") id:String,
        @Field("comment") comment:String
    ):Response<FutsalCommentResponse>

    @FormUrlEncoded
    @POST("retrieveComments")
    suspend fun retrieveComments(
        @Field("futsal_id") id:String
    ):Response<FutsalCommentResponse>

    @FormUrlEncoded
    @POST("deleteComment")
    suspend fun deleteComment(
        @Header("Authorization") token:String,
        @Field("id") id:String
    ):Response<FutsalCommentResponse>

    @PUT("updateComment")
    suspend fun updateComment(
        @Header("Authorization") token:String,
        @Body comment:FutsalComment
    ):Response<FutsalCommentResponse>
}