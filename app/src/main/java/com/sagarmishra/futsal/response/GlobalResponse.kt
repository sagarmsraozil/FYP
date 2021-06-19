package com.sagarmishra.futsal.response

data class GlobalResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val point:Int?=null,
    val error:MutableMap<String,String>? =null
)
