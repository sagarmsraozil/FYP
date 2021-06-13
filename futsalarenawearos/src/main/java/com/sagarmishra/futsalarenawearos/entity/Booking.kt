package com.sagarmishra.futsalarenawearos.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Booking(
    @PrimaryKey
    val _id:String = "",
    val futsalInstance_id:FutsalInstances?=null,
    val user_id:AuthUser?=null,
    val booked_date2:String?=null,
    val bookingCode:String?=null,
    val mobileNumber:String?=null,
    val teamMate_mobileNumber:String?=null,
    val expired:Boolean?=null,
    val booked_time:String?=null,
    var offlineTime:String?=null,
    var offlineDate:String?=null,
    var offlineDay:String?=null,
    var offlinePrice:Int=0,
    var offlineFutsalName:String?=null
)