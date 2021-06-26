package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Chat(
    @PrimaryKey
    val _id:String = "",
    val sender:AuthUser?=null,
    val receiver:AuthUser?=null,
    val dateAndTime:String?=null,
    val receiverStatus:Boolean?=null,
    val message:String?=null
)