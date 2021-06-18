package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.entityapi.MatchMaking

data class MatchMakingResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<MatchMaking>?=null,
    val nearbyFutsals:MutableList<Futsal>?=null
)
