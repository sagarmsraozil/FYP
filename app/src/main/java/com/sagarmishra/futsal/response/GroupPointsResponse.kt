package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.TournamentGroup

data class GroupPointsResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<MutableList<TournamentGroup>>? = null
)
