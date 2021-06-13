package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Ads

data class AdsResponse(val success:Boolean?=null,val message:String?=null,val data:MutableList<Ads>?=null)