package com.sagarmishra.futsal.entityapi

data class ChatTwo(
    val _id:String="",
    val sender:String?=null,
    val receiver:String?=null,
    val message:String?=null,
    val dateAndTime:String?=null,
    val receiverStatus:Boolean?=null

)