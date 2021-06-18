package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TeamStats(
    @PrimaryKey
    val _id:String = "",
    val matchPlayed:Int = 0,
    val wins:Int=0,
    val draw:Int=0,
    val loss:Int=0,
    val winPercent:Double=0.0,
    val drawPercent:Double=0.0,
    val lossPercent:Double=0.0,
    val ratio:Double=0.0,
    val goalsScored:Int=0,
    val goalsConceaded:Int=0,
    val season:Int=0,
    val pointsCollected:Int=0,
    val pointInteraction:String?=null,
    val tier:TierBadge?=null,
    val pointTree:MutableList<Int>? = null,
    val team_id:Team?=null
)
