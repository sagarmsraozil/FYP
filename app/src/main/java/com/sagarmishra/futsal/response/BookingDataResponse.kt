package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.Booking

data class BookingDataResponse(val success:Boolean?=null,val singleData:Booking?=null,val data:MutableList<Booking>?=null,val message:String?=null,val markAndInstance:MutableMap<String,Int>? =null) {
}