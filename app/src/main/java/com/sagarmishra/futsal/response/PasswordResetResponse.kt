package com.sagarmishra.futsal.response

data class PasswordResetResponse(val success:Boolean?=null,val message:String?=null,val token:String?=null,val email:String?=null,val pinCode:String?=null) {
}