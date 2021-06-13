package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Giveaway(
    @PrimaryKey
    val _id :String = "",
    var futsal_id:Futsal?=null,
    var item:String?=null,
    var quantity:Int=0,
    var giveAwayCode:String?=null,
    var startingFrom:String?=null,
    var endAt:String?=null,
    var resultAt:String?=null,
    var image:String?=null,
    var status:String?=null
) {
}