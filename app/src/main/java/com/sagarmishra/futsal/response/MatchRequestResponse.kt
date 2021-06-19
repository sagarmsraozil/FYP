package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.MatchRequest

data class MatchRequestResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<MatchRequest>? = null
)