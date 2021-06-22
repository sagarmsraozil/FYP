package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Challenges(
    @PrimaryKey
    val _id:String="",
    var challengeTitle:String?=null,
    var challengeReward:String?=null,
    var challengeProgressionPoint:Int = 0,
    var marker:Boolean?=null,
    var addedAt:String?=null
)