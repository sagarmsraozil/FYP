package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TournamentMatch(
    @PrimaryKey
    val _id:String="",
    val tournament_id:Tournament?=null,
    val team1:Team?=null,
    val team2:Team?=null,
    var gameDate:String?=null,
    var fancyDate:String?=null,
    var time:String?=null,
    var timeHour:String?=null,
    var round:String?=null,
    var goals:MutableList<Int>?=null,
    var status:String?=null,
    var venue:Futsal?=null,
    var matchType:String?=null,
    var analysis:Boolean?=null,
    var win1:Int = 0,
    var win2:Int = 0,
    var loss1:Int = 0,
    var loss2:Int = 0,
    var draw1:Int = 0,
    var draw2:Int = 0,
    var scoreLinePrediction:String?=null,
    var analysisStatus:String?=null,
    var notificationSent:Boolean?=null
)
