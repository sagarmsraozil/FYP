package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.AdAPI
import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.response.APIResponse
import com.sagarmishra.futsal.response.AdsResponse

class AdsRepository():ApiRequest() {
    val adService = RetrofitService.retroService(AdAPI::class.java)

    suspend fun retrieveAds():AdsResponse
    {
        return apiRequest {
            adService.retrieveAds()
        }
    }

    suspend fun invite(email:String):APIResponse
    {
        return apiRequest {
            adService.invite(RetrofitService.token!!,email)
        }
    }
}