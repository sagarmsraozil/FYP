package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Battle(
    @PrimaryKey
    val _id:String="",
    var homeTeam:String?=null,
    var awayTeam:Team?=null,
    var time:String?=null,
    var date:String?=null,
    var description:String?=null,
    var futsal_id:Futsal?=null,
    var status:String?=null,
    var date2:String?=null,
    var timeHour:String?=null,
    var pointGiven:String?=null,
    var tierObserved:Boolean?=null,
    var battleCode:String?=null,
    var pointObtained:String?=null,
    var scoreLine:String?=null
)