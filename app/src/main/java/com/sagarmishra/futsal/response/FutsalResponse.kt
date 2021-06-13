package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Futsal

data class FutsalResponse(val success:Boolean?=null,val message:String?=null,val data:MutableList<Futsal>?=null)