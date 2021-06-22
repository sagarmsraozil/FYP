package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Team

data class TeamResponse(
    val success:Boolean?=null,
    val data:Team?=null,
    val count:Int = 0,
    val tierData:String?=null,
    val message:String?=null,
    val tierName:String?=null
)
