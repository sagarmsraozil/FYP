package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.response.APIResponse
import com.sagarmishra.futsal.response.BookingDataResponse
import com.sagarmishra.futsal.response.BookingResponse
import com.sagarmishra.futsal.response.GlobalResponse
import retrofit2.Response
import retrofit2.http.*

interface BookingAPI {

    @POST("booking/futsal")
    suspend fun bookFutsal(
        @Header("Authorization") token:String,
        @Body bookingRecord: Booking
    ):Response<BookingResponse>

    @FormUrlEncoded
    @POST("singleBookingRecord")
    suspend fun retrieveSingleRecord(
        @Header("Authorization") token:String,
        @Field("id")id:String
    ):Response<BookingDataResponse>

    @GET("getMyBookings")
    suspend fun retrieveBookings(
        @Header("Authorization") token:String,
    ):Response<BookingDataResponse>

    @FormUrlEncoded
    @POST("deleteBooking")
    suspend fun deleteBooking(
        @Header("Authorization") token:String,
        @Field("id") id:String
    ):Response<APIResponse>

    @FormUrlEncoded
    @POST("markABookingInstance")
    suspend fun markInstace(
        @Header("Authorization") token:String,
        @Field("instanceId") id:String
    ):Response<GlobalResponse>

    @POST("removeMark/{instanceId}")
    suspend fun removeMark(
        @Header("Authorization") token :String,
        @Path("instanceId") id:String
    ):Response<GlobalResponse>

}