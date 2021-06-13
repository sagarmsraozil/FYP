package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FutsalComment(
    @PrimaryKey
    val _id:String = "",
    val futsal_id:String?=null,
    val user_id:AuthUser?=null,
    var comment:String?=null,
    val commentedAt2:String?=null

) {
}