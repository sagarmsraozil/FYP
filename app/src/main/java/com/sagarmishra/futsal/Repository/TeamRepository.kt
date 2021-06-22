package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.api.TeamAPI
import com.sagarmishra.futsal.entityapi.Battle
import com.sagarmishra.futsal.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class TeamRepository():ApiRequest() {
    val teamAPI = RetrofitService.retroService(TeamAPI::class.java)

    suspend fun createTeam(teamName:RequestBody,teamTag:RequestBody,locatedCity:RequestBody,ageGroup:RequestBody,contact:RequestBody,teamEmail:RequestBody,citizenShipBack:MultipartBody.Part,citizenShipFront:MultipartBody.Part,ownerPhoto:MultipartBody.Part):GlobalResponse
    {
        return apiRequest {
            teamAPI.createATeam(RetrofitService.token!!,teamName,teamTag,locatedCity,ageGroup,contact,teamEmail,citizenShipBack,citizenShipFront,ownerPhoto)
        }
    }


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

    suspend fun addBattle(battle:Battle):GlobalResponse
    {
        return apiRequest {
            teamAPI.setABattle(RetrofitService.token!!,battle)
        }
    }

    suspend fun detailUpdate(address:String,email:String,contact:String,age:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.detailsUpdate(RetrofitService.token!!,address,email,contact,age)
        }
    }

    suspend fun uploadLogo(body:MultipartBody.Part,tid:RequestBody):GlobalResponse
    {
        return apiRequest {
            teamAPI.uploadLogo(RetrofitService.token!!,body,tid)
        }
    }

    suspend fun selectTitle(tid:String,selectedTitle:String,changeFor:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.selectTitle(
                RetrofitService.token!!,
                tid,selectedTitle,changeFor
            )
        }
    }

    suspend fun teamChallenges():TeamChallengeResponse
    {
        return apiRequest {
            teamAPI.teamChallenges(RetrofitService.token!!)
        }
    }

    suspend fun addTitleFromChallenge(cid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.addTitleFromChallenge(RetrofitService.token!!,cid)
        }
    }

    suspend fun topRankers():TopRankerResponse
    {
        return apiRequest {
            teamAPI.topRankers(RetrofitService.token!!)
        }
    }

    suspend fun collectTitle(tid:String):GlobalResponse
    {
        return apiRequest {
            teamAPI.addTitle(RetrofitService.token!!,tid)
        }
    }

}