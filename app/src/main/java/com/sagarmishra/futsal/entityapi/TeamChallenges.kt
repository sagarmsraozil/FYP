package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TeamChallenges(
    @PrimaryKey
    val _id:String="",
    var challenge_id:Challenges?=null,
    var team_id:String?=null,
    var progessPoint:Int = 0,
    var status:String?=null,
    var rewardCollected:Boolean?=null
)
