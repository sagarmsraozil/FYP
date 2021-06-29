package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Chat
import com.sagarmishra.futsal.entityapi.ChatTwo

data class SingleChatResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data: Chat?=null
)
