package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.TeamStats

data class TeamStatsResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:TeamStats?=null
)
