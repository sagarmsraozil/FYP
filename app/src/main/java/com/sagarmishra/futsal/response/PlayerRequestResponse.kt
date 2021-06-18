package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.RequestModel

data class PlayerRequestResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<RequestModel>?=null
)
