package com.sagarmishra.futsal.entityapi

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Futsal(
    @PrimaryKey
    val _id : String = "",
    var futsalName:String?=null,
    var location:String?=null,
    var size:String?=null,
    var grounds:String?=null,
    var futsalDescription:String?=null,
    var contact:String?=null,
    var futsalImage:String?=null,
    var email:String?=null,
    var openingTime :Int =0,
    var closingTime:Int = 0,
    var morningPrice:Int=0,
    var dayPrice:Int=0,
    var eveningPrice:Int=0,
    var nightPrice:Int=0,
    var latitude:Double = 0.0,
    var longitude:Double = 0.0,
    var superPrice:Int = 0,
    var rating:Int = 0,
    var totalComments:Int = 0,
    var futsalCode:String?=null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(futsalName)
        parcel.writeString(location)
        parcel.writeString(size)
        parcel.writeString(grounds)
        parcel.writeString(futsalDescription)
        parcel.writeString(contact)
        parcel.writeString(futsalImage)
        parcel.writeString(email)
        parcel.writeInt(openingTime)
        parcel.writeInt(closingTime)
        parcel.writeInt(morningPrice)
        parcel.writeInt(dayPrice)
        parcel.writeInt(eveningPrice)
        parcel.writeInt(nightPrice)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeInt(superPrice)
        parcel.writeInt(rating)
        parcel.writeInt(totalComments)
        parcel.writeString(futsalCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Futsal> {
        override fun createFromParcel(parcel: Parcel): Futsal {
            return Futsal(parcel)
        }

        override fun newArray(size: Int): Array<Futsal?> {
            return arrayOfNulls(size)
        }
    }


}