package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.TournamentStructure

data class TournamentStructureResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<TournamentStructure>?=null,
    val dataHolder:MutableMap<String,Boolean>? = null
)
