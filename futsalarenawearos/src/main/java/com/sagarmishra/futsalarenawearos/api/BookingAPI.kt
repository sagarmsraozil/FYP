package com.sagarmishra.futsalarenawearos.api

import com.sagarmishra.futsalarenawearos.response.BookingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface BookingAPI {
    @GET("getMyBookings")
    suspend fun getBookings(
        @Header("Authorization") token:String
    ):Response<BookingResponse>
}