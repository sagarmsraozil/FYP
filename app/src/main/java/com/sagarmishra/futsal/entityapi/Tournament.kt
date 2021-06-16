package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tournament(
    @PrimaryKey
    val _id:String = "",
    var maxAgeGroup:Int = 0,
    var minAgeGroup:Int = 0,
    var tournamentName:String?=null,
    var participationCount:Int = 0,
    var participatedTeams:MutableList<Team>?=null,
    var participationStartsFrom:String?=null,
    var participationStarts2:String?=null,
    var participationEndsAt:String?=null,
    var participationEnds2:String?=null,
    var startsFrom:String?=null,
    var startsFrom2:String?=null,
    var endsAt:String?=null,
    var ends2:String?=null,
    var prizePool:MutableList<MutableMap<String,String>>? = null,
    var status:String?=null,
    var sponsors:String?=null,
    var tournamentFormat:String?=null,
    var totalGroups:Int = 0,
    var tournamentBanner:String?=null,
    var organizedBy:String?=null,
    var organizerDetail:String?=null,
    var tournamentCode:String?=null,
    var titleProvided:Boolean?=null
)