package com.sagarmishra.futsal.entityroom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(var first_name:String? = null,var last_name:String? = null,var username:String? =null,var password: String? = null, var email:String? = null) {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0



}