package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Futsal

data class SingleFutsalResponse(val success:Boolean?=null,val message:String?=null,val data: Futsal?=null) {
}