package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.api.TeamAPI
import com.sagarmishra.futsal.response.*

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

    suspend fun teamStatsHistory(tid:String):TeamStatsHistory
    {
        return apiRequest {
            teamAPI.myTeamTierHistory(tid)
        }
    }

    suspend fun findMatchingTeam(tid:String):MatchMakingResponse
    {
        return apiRequest {
            teamAPI.findMatchingTeam(RetrofitService.token!!,tid)
        }
    }

    suspend fun randomMatching(tid:String):MatchMakingResponse
    {
        return apiRequest {
            teamAPI.randomMatching(RetrofitService.token!!,tid)
        }
    }

    suspend fun requestAMatch(hid:String,aid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.requestAMatch(RetrofitService.token!!,hid,aid)
        }
    }

    suspend fun fetchEveryTeams():EveryTeamResponse
    {
        return  apiRequest {
            teamAPI.fetchEveryTeams()
        }
    }

    suspend fun fetchSingleTeam(tid:String):TeamResponse
    {
        return apiRequest {
            teamAPI.fetchSingleTeam(tid)
        }
    }

    suspend fun requestToJoin(tid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.requestToJoin(RetrofitService.token!!,tid)
        }
    }

}