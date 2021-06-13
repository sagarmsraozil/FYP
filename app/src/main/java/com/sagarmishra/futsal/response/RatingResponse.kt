package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.FutsalRating

data class RatingResponse(val success:Boolean?=null,val data:FutsalRating?=null,val message:String?=null)