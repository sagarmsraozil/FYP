package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.AuthUser

data class ActivationResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:AuthUser?=null,
    val error:MutableMap<String,String>? = null
)
