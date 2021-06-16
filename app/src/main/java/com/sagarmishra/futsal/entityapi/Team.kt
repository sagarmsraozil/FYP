package com.sagarmishra.futsal.entityapi

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Team(
    @PrimaryKey
    val _id:String = "",
    val teamName:String?=null,
    val teamTag:String?=null,
    val teamLogo:String?=null,
    val createdAt:String?=null,
    val teamCode:String?=null,
    val teamPlayers:MutableList<AuthUser>? = null,
    val teamOwner:String?=null,
    val teamColeader:String?=null,
    val fancyCreated:String?=null,
    val locatedCity:String?=null,
    val contact:String?=null,
    val teamEmail:String?=null,
    val ageGroup:Int = 0,
    val citizenShipFront:String?=null,
    val citizenShipBack:String?=null,
    val ownerPhoto:String?=null,
    val status:String?=null,
    val titles:MutableList<String>? = null,
    val activeTitle:String?=null
):Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("teamPlayers"),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("titles"),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return teamName!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(teamName)
        parcel.writeString(teamTag)
        parcel.writeString(teamLogo)
        parcel.writeString(createdAt)
        parcel.writeString(teamCode)
        parcel.writeString(teamOwner)
        parcel.writeString(teamColeader)
        parcel.writeString(fancyCreated)
        parcel.writeString(locatedCity)
        parcel.writeString(contact)
        parcel.writeString(teamEmail)
        parcel.writeInt(ageGroup)
        parcel.writeString(citizenShipFront)
        parcel.writeString(citizenShipBack)
        parcel.writeString(ownerPhoto)
        parcel.writeString(status)
        parcel.writeString(activeTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Team> {
        override fun createFromParcel(parcel: Parcel): Team {
            return Team(parcel)
        }

        override fun newArray(size: Int): Array<Team?> {
            return arrayOfNulls(size)
        }
    }
}