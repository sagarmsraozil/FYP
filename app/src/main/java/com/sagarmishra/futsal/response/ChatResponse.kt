package com.sagarmishra.futsal.response


import com.sagarmishra.futsal.entityapi.Chat

data class ChatResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<Chat>? = null,
    val eyeball:String?=null
)
