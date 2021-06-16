package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.api.TeamAPI
import com.sagarmishra.futsal.response.BattleResponse
import com.sagarmishra.futsal.response.GlobalResponse
import com.sagarmishra.futsal.response.TeamResponse
import com.sagarmishra.futsal.response.TeamStatsResponse

class TeamRepository():ApiRequest() {
    val teamAPI = RetrofitService.retroService(TeamAPI::class.java)

    suspend fun getMyTeam():TeamResponse
    {
        return apiRequest {
            teamAPI.getMyTeam(RetrofitService.token!!)
        }
    }

    suspend fun getTeamStats(tid:String):TeamStatsResponse
    {
        return apiRequest {
            teamAPI.getTeamStats(tid)
        }
    }

    suspend fun promoteToColeader(tid:String,uid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.promoteToColeader(RetrofitService.token!!,tid,uid)
        }
    }

    suspend fun kickPlayer(tid:String,uid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.kickPlayer(RetrofitService.token!!,tid,uid)
        }
    }

    suspend fun promoteToLeader(uid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.promoteLeader(RetrofitService.token!!,uid)
        }
    }

    suspend fun demotePlayer(tid:String,uid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.demotePlayer(RetrofitService.token!!,tid,uid)
        }
    }

    suspend fun disbandTeam():GlobalResponse
    {
        return apiRequest {
            teamAPI.disbandTeam(RetrofitService.token!!)
        }
    }

    suspend fun leaveTeam():GlobalResponse
    {
        return apiRequest {
            teamAPI.leaveTeam(RetrofitService.token!!)
        }
    }

    suspend fun fetchBattles(tid:String):BattleResponse
    {
        return apiRequest {
            teamAPI.fetchBattles(tid)
        }
    }

    suspend fun deleteBattle(bid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.deleteBattle(RetrofitService.token!!,bid)
        }
    }

}