package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.FutsalComment

data class FutsalCommentResponse(val success:Boolean?=null,val message:String?=null,val data:MutableList<FutsalComment>?=null,val id:String?=null,val commentAt:String?=null) {
}