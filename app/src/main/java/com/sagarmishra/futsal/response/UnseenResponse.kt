package com.sagarmishra.futsal.response

data class UnseenResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableMap<String,Int>? = null
)
