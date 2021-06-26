package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DotsPricing(
    @PrimaryKey
    val _id:String="",
    val user_id:AuthUser?=null,
    val futsalInstance_id:FutsalInstances?=null,
    val price:Int = 0,
    val status:Boolean?=null
)
