package com.sagarmishra.futsal.entityapi

data class HistoryRecord(
    var match:String?=null,
    var team1:MutableList<Any>?=null,
    var team2:MutableList<Any>?=null,
    var matches:Int = 0
)
