package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.RequestAPI
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.response.GlobalResponse
import com.sagarmishra.futsal.response.MatchRequestResponse
import com.sagarmishra.futsal.response.PlayerRequestResponse

class RequestRepository():ApiRequest() {
    val requestAPI = RetrofitService.retroService(RequestAPI::class.java)

    suspend fun getUserRequests(tid:String):PlayerRequestResponse
    {
        return apiRequest {
            requestAPI.getUserRequests(tid)
        }
    }

    suspend fun respondToPlayerRequest(respond:String,rid:String):GlobalResponse
    {
        return apiRequest {
            requestAPI.respondToPlayerRequest(RetrofitService.token!!,respond,rid)
        }
    }

    suspend fun myRequests(tid:String):MatchRequestResponse
    {
        return apiRequest {
            requestAPI.myRequestForMatch(tid)
        }
    }

    suspend fun opponentRequests(tid:String):MatchRequestResponse
    {
        return apiRequest {
            requestAPI.requestsToMyTeam(tid)
        }
    }

    suspend fun decisionBattle(rid:String,tid:String,decision:String):GlobalResponse
    {
        return apiRequest {
            requestAPI.battleDecision(RetrofitService.token!!,rid,tid,decision)
        }
    }

}

