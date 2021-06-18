package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RequestModel(
    @PrimaryKey
    val _id:String="",
    val team_id:String?=null,
    val user_id:AuthUser?=null
)
