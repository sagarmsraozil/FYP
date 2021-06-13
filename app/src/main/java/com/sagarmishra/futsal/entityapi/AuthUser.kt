package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthUser(
       @PrimaryKey
       val _id:String="",
       var firstName:String?=null,
       var lastName:String?=null,
       var userName:String?=null,
       var password :String?=null,
       var confirmPassword:String?=null,
       var address:String?=null,
       var mobileNumber:String?=null,
       var dob:String?=null,
       var email:String?=null,
       var gender:String?=null,
       var profilePicture:String?=null
)