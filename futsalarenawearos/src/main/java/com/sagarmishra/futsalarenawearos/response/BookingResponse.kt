package com.sagarmishra.futsalarenawearos.response

import com.sagarmishra.futsalarenawearos.entity.Booking

data class BookingResponse(val success:Boolean?=null,val message:String?=null,val data:MutableList<Booking>?=null)
