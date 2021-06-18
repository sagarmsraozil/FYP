package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Team

data class EveryTeamResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<Team>?=null
)
