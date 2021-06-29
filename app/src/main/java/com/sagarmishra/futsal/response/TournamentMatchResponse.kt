package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.HistoryRecord
import com.sagarmishra.futsal.entityapi.TournamentMatch

data class TournamentMatchResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<TournamentMatch>?=null,
    val myTeamStats:MutableMap<String,Any>?=null,
    val analysis:MutableList<HistoryRecord>?=null
)
