package com.sagarmishra.futsalarenawearos.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FutsalInstances(
    @PrimaryKey
    val _id:String="",
    val futsal_id:Futsal?=null,
    var date:String?=null,
    var date2:String?=null,
    var available:Boolean?=null,
    var price:Int = 0,
    var superPrice:Int=0,
    var time:String?=null,
    var day:String?=null
)