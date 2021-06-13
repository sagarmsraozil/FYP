package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.FutsalAPI
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.response.*

class FutsalRepository():ApiRequest() {
    var futsalAPI = RetrofitService.retroService(FutsalAPI::class.java)

    suspend fun showFutsals():FutsalResponse
    {
        return apiRequest {
            futsalAPI.showFutsals()
        }
    }

    suspend fun retrieveBookingInstance(id:String,day:String,date:String):FutsalTimeInstanceResponse
    {
        return apiRequest {
            futsalAPI.retrieveBookingInstance(id,day,date)
        }
    }

    suspend fun rateFutsal(id:String,rating:Float):APIResponse
    {
        return apiRequest {
            futsalAPI.rateFutsal(RetrofitService.token!!,id,rating)
        }
    }

    suspend fun myRatings(id:String): RatingResponse
    {
        return apiRequest {
            futsalAPI.myRatings(RetrofitService.token!!,id)
        }
    }


    suspend fun checkTimeInstanceAvailability(pid:String):APIResponse
    {
        return apiRequest {
            futsalAPI.checkTimeInstanceAvailability(RetrofitService.token!!,pid)
        }
    }

    suspend fun singleFutsal(id:String):SingleFutsalResponse
    {
        return apiRequest {
            futsalAPI.singleFutsal(id)
        }
    }


}