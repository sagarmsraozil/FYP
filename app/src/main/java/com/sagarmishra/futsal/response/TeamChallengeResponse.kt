package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.TeamChallenges

data class TeamChallengeResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<TeamChallenges>?=null
)
