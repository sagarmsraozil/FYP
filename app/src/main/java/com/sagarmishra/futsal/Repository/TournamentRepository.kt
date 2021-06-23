package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.api.TournamentAPI
import com.sagarmishra.futsal.response.*

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

    suspend fun fetchTournaments():TournamentResponse
    {
        return apiRequest {
            tournamentAPI.fetchTournaments()
        }
    }

    suspend fun singleTournament(tid:String):SingleTournamentResponse
    {
        return apiRequest {
            tournamentAPI.getTournament(RetrofitService.token!!,tid)
        }
    }



    suspend fun participateInTournament(tid:String):GlobalResponse
    {
        return apiRequest {
            tournamentAPI.paticipateInTournament(RetrofitService.token!!,tid)
        }
    }

    suspend fun removeParticipation(tid:String):GlobalResponse
    {
        return apiRequest {
            tournamentAPI.removeParticipation(RetrofitService.token!!,tid)
        }
    }

    suspend fun tournamentMatches(tid:String):TournamentMatchResponse
    {
        return apiRequest {
            tournamentAPI.tournamentMatches(tid)
        }
    }

    suspend fun teamPerformance(tournamentId:String,teamId:String):TournamentPerformanceResponse
    {
        return apiRequest {
            tournamentAPI.teamPerformance(tournamentId,teamId)
        }
    }
}