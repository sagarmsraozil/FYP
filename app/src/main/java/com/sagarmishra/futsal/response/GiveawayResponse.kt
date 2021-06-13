package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Giveaway

data class GiveawayResponse(val success:Boolean?=null,val message:String?=null,val data:MutableList<Giveaway>?=null) {
}