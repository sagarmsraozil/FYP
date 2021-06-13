package com.sagarmishra.futsal.entityapi

import androidx.room.*

@Entity
data class BookingRoomDB(
    @PrimaryKey
    val _id:String = "",
    val futsal_id:String?=null,
    val user_id:String?=null,
    val futsalName:String?=null,
    val price:Int = 0,
    val date:String?=null,
    val day:String?=null,
    val time:String?=null,
    val expired:Boolean?=null
)