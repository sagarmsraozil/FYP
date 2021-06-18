package com.sagarmishra.futsal.entityapi

data class MatchMaking(
    var opponentWin:Double = 0.0,
    var opponentDraw:Double = 0.0,
    var opponentLoss:Double = 0.0,
    var myWin:Double = 0.0,
    var myDraw:Double = 0.0,
    var myLoss:Double = 0.0,
    var opponent:Team? = null,
    var me:Team?=null,
    var distance:String?=null,
    var status:String?=null,
    var scoreLine:String?=null,
    var opponentStats:TierBadge?=null,
    var ratio:Double=0.0
)