package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.DotsPricing

data class DotsPricingResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<DotsPricing>? =null
)
