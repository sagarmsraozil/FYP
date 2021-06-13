package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.api.GiveawayAPI
import com.sagarmishra.futsal.response.GiveawayDistinctResponse
import com.sagarmishra.futsal.response.GiveawayResponse
import com.sagarmishra.futsal.response.OngoingGiveawayResponse

class GiveawayRepository():ApiRequest() {
    val giveAPI = RetrofitService.retroService(GiveawayAPI::class.java)

    suspend fun getDistinctGiveaway():GiveawayDistinctResponse
    {
        return apiRequest {
            giveAPI.getDistinctGiveaway()
        }
    }

    suspend fun getGiveaway():GiveawayResponse
    {
        return apiRequest {
            giveAPI.getGiveaway()
        }
    }

    suspend fun getOngoing():OngoingGiveawayResponse
    {
        return apiRequest {
            giveAPI.getOngoingCount()
        }
    }

    suspend fun getMyParticipant():GiveawayDistinctResponse
    {
        return apiRequest {
            giveAPI.getMyParticipantRecord(RetrofitService.token!!)
        }
    }

    suspend fun participateGiveaway(code:String):GiveawayResponse
    {
        return apiRequest {
            giveAPI.participateGiveAway(RetrofitService.token!!,code)
        }
    }
}