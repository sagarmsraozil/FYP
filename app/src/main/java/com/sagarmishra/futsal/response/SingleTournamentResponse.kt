package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Tournament

data class SingleTournamentResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<Tournament>?=null,
    val authorize:MutableList<Boolean>?=null,
    val remaining:Int = 0
)
