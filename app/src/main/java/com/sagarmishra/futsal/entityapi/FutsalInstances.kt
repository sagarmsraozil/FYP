package com.sagarmishra.futsal.entityapi

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FutsalInstances(
    @PrimaryKey
    val _id:String="",
    val futsal_id:Futsal?=null,
    var date:String?=null,
    var date2:String?=null,
    var available:Boolean?=null,
    var price:Int = 0,
    var superPrice:Int=0,
    var time:String?=null,
    var day:String?=null,
    var timeHour:Int = 0
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable(Futsal::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeParcelable(futsal_id, flags)
        parcel.writeString(date)
        parcel.writeString(date2)
        parcel.writeValue(available)
        parcel.writeInt(price)
        parcel.writeInt(superPrice)
        parcel.writeString(time)
        parcel.writeString(day)
        parcel.writeInt(timeHour)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FutsalInstances> {
        override fun createFromParcel(parcel: Parcel): FutsalInstances {
            return FutsalInstances(parcel)
        }

        override fun newArray(size: Int): Array<FutsalInstances?> {
            return arrayOfNulls(size)
        }
    }

}