package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Tournament

data class TournamentResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<Tournament>?=null
)
