package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.TeamStats

data class TopRankerResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<TeamStats>? = null,
    val currentSeason:Int = 0,
    val totalSeasons:MutableList<Int>? =null,
    val ageGroup:MutableList<Int>? = null
)
