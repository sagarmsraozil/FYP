package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TournamentPerformance(
    @PrimaryKey
    val _id:String="",
    val tournament_id:String?=null,
    val team_id:String?=null,
    val matchPlayed:Int = 0,
    val win:Int = 0,
    val draw:Int = 0,
    val loss:Int = 0,
    val winPercent:Int = 0,
    val drawPercent:Int = 0,
    val lossPercent:Int = 0,
    val goalsScored:Int = 0,
    val goalsConceaded:Int = 0,
    val ratio:Double = 0.0
)
