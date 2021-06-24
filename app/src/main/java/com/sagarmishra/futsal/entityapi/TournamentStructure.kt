package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TournamentStructure(
    @PrimaryKey
    var _id:String="",
    val round1:MutableList<Team>?=null,
    val round2:MutableList<Team>?=null,
    val round3:MutableList<Team>?=null,
    val round4:MutableList<Team>?=null,
    val round5:MutableList<Team>?=null,
    val round6:MutableList<Team>?=null,
    val round7:MutableList<Team>?=null,
    val round8:MutableList<Team>?=null,
    val tournament_id:Tournament?=null
)