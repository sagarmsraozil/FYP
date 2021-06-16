package com.sagarmishra.futsal.entityapi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TierBadge(
    @PrimaryKey
    val _id:String = "",
    val tierName:String?=null,
    val tierLogo:String?=null,
    val tierLevel:String?=null,
    val tierNomenclature:String?=null,
    val tierNumber:Int = 0

)