package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.FutsalInstances

data class FutsalTimeInstanceResponse(val success:Boolean?=null, val message:String?=null, val data:MutableList<FutsalInstances>?=null,val remnant:MutableList<String>?=null,var markDetails:MutableMap<String,MutableList<String>>?=null,val bookTimes:MutableMap<String,String>? = null)