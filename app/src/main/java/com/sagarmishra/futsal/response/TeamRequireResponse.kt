package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.AuthUser

data class TeamRequireResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<AuthUser>? = null,
    val playerTeam:MutableMap<String,String>? = null
)
