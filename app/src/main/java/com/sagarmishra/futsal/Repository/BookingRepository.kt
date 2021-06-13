package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.BookingAPI
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.response.APIResponse
import com.sagarmishra.futsal.response.BookingDataResponse
import com.sagarmishra.futsal.response.BookingResponse
import com.sagarmishra.futsal.response.GlobalResponse

class BookingRepository():ApiRequest() {
    val bookingAPI = RetrofitService.retroService(BookingAPI::class.java)

    suspend fun retrieveSingleRecord(id:String):BookingDataResponse
    {
        return apiRequest {
            bookingAPI.retrieveSingleRecord(RetrofitService.token!!,id)
        }
    }

    suspend fun bookFutsal(bookingRecord: Booking): BookingResponse
    {
        return apiRequest {
            bookingAPI.bookFutsal(RetrofitService.token!!,bookingRecord)
        }
    }

    suspend fun retrieveBookings():BookingDataResponse
    {
        return apiRequest {
            bookingAPI.retrieveBookings(RetrofitService.token!!)
        }
    }

    suspend fun deleteBooking(id:String):APIResponse
    {
        return apiRequest {
            bookingAPI.deleteBooking(RetrofitService.token!!,id)
        }
    }

    suspend fun markInstance(id:String):GlobalResponse
    {
        return apiRequest {
            bookingAPI.markInstace(RetrofitService.token!!,id)
        }
    }

    suspend fun removeMark(id:String):GlobalResponse
    {
        return apiRequest {
            bookingAPI.removeMark(RetrofitService.token!!,id)
        }
    }
}