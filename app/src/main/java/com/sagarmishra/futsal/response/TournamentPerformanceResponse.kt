package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.TournamentPerformance

data class TournamentPerformanceResponse(
    val success:Boolean? = null,
    val message:Boolean? = null,
    val data:TournamentPerformance? = null
)
