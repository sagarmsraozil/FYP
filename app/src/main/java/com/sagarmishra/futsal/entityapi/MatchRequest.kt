package com.sagarmishra.futsal.entityapi

import androidx.room.Entity

@Entity
data class MatchRequest(
    val _id:String="",
    val homeTeam:Team?=null,
    val awayTeam:Team?=null,
    var fancyDate:String?=null,
    var status:String?=null
)