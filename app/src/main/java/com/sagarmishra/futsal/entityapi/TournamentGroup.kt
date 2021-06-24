package com.sagarmishra.futsal.entityapi

data class TournamentGroup(
    val _id: String = "",
    val tournament_id: String? = null,
    val team: Team? = null,
    val matchPlayed: Int = 0,
    val win: Int = 0,
    val loss:Int = 0,
    val draw:Int = 0,
    val goalDifference:Int = 0,
    val Points:Int = 0,
    val group:String?=null
)
