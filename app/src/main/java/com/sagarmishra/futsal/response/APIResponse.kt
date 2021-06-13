package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.FutsalRating

data class APIResponse(val success:Boolean?=null,val message:String?=null,val data:FutsalRating?=null)