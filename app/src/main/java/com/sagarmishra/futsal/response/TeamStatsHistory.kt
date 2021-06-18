package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.TeamStats

data class TeamStatsHistory(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<TeamStats>?=null,
    val season:Int=0,
    val seasons:MutableList<Int>?=null
)
