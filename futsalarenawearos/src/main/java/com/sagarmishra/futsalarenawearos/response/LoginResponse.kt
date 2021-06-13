package com.sagarmishra.futsalarenawearos.response

import com.sagarmishra.futsalarenawearos.entity.AuthUser

data class LoginResponse(val success:Boolean?=null,val message:String?=null,val token:String?=null,val data:AuthUser?=null)
