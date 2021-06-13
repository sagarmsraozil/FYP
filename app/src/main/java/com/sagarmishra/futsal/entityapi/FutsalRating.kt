package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FutsalRating(
    @PrimaryKey
    val _id:String="",
    var user_id:String="",
    var futsal_id:String="",
    var rating:Int = 0

)