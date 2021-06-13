package com.sagarmishra.futsal.response

import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityroom.User

data class LoginResponse(val success:Boolean?=null,val token:String?=null,val message:String?=null,val data: AuthUser?=null)