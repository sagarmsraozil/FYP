package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.api.TournamentAPI
import com.sagarmishra.futsal.response.TournamentMatchResponse

class TournamentRepository():ApiRequest() {
    val tournamentAPI = RetrofitService.retroService(TournamentAPI::class.java)

    suspend fun getTournamentHistory(tid:String):TournamentMatchResponse
    {
        return apiRequest {
            tournamentAPI.getTournamentMatches(tid)
        }
    }

    suspend fun getUpcomingMatches(tid:String):TournamentMatchResponse
    {
        return apiRequest {
            tournamentAPI.upcomingTournamentMatches(tid)
        }
    }
}