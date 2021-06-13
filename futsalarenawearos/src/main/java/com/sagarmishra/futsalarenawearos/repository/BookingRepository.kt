package com.sagarmishra.futsalarenawearos.repository

import com.sagarmishra.futsalarenawearos.api.ApiRequest
import com.sagarmishra.futsalarenawearos.api.BookingAPI
import com.sagarmishra.futsalarenawearos.api.ServiceBuilder
import com.sagarmishra.futsalarenawearos.response.BookingResponse

class BookingRepository():ApiRequest() {
    val bookingAPI = ServiceBuilder.retroService(BookingAPI::class.java)

    suspend fun getBookings():BookingResponse
    {
        return apiRequest {
            bookingAPI.getBookings(ServiceBuilder.token!!)
        }
    }
}