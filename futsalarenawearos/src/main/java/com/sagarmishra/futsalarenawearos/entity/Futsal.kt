package com.sagarmishra.futsalarenawearos.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Futsal(
    @PrimaryKey
    val _id : String = "",
    var futsalName:String?=null,
    var location:String?=null,
    var size:String?=null,
    var grounds:String?=null,
    var futsalDescription:String?=null,
    var contact:String?=null,
    var futsalImage:String?=null,
    var email:String?=null,
    var openingTime :Int =0,
    var closingTime:Int = 0,
    var morningPrice:Int=0,
    var dayPrice:Int=0,
    var eveningPrice:Int=0,
    var nightPrice:Int=0,
    var latitude:Double = 0.0,
    var longitude:Double = 0.0,
    var superPrice:Int = 0,
    var rating:Int = 0,
    var totalComments:Int = 0
)
