package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Battle

data class BattleResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val deleteable:MutableList<String>? = null,
    var data:MutableList<Battle>? = null
)
