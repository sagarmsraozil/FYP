package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.response.*
import retrofit2.Response
import retrofit2.http.*

interface FutsalAPI {
    @GET("futsal/showall")
    suspend fun showFutsals():Response<FutsalResponse>

    @FormUrlEncoded
    @POST("instance")
    suspend fun retrieveBookingInstance(
        @Field("futsalId") id:String,
        @Field("day") day:String,
        @Field("date") date:String
    ):Response<FutsalTimeInstanceResponse>

    @FormUrlEncoded
    @POST("rate/futsal")
    suspend fun rateFutsal(
        @Header("Authorization") token:String,
        @Field("futsalId") id:String,
        @Field("rating") rating:Float
    ):Response<APIResponse>

    @FormUrlEncoded
    @POST("retrieveRating")
    suspend fun myRatings(
        @Header("Authorization") token:String,
        @Field("futsalId") id:String
    ):Response<RatingResponse>

    @GET("futsal/showFutsal/{pid}")
    suspend fun singleFutsal(
        @Path("pid") id:String
    ):Response<SingleFutsalResponse>

    @POST("futsal/instance/{pid}")
    suspend fun checkTimeInstanceAvailability(
        @Header("Authorization") token:String,
        @Path("pid") id:String
    ):Response<APIResponse>


}