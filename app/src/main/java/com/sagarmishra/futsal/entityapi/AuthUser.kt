package com.sagarmishra.futsal.entityapi

import android.os.Parcel
import android.os.Parcelable
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
       var profilePicture:String?=null,
       var account_activation:Boolean = false,
       var dots:Int = 0,
       var activeTitle:String?=null,
       var titles:MutableList<String>? = null
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
              parcel.readString(),
              parcel.readString(),
              parcel.readString(),
              parcel.readByte() != 0.toByte(),
              parcel.readInt(),
              parcel.readString()
       ) {
       }

       override fun writeToParcel(parcel: Parcel, flags: Int) {
              parcel.writeString(_id)
              parcel.writeString(firstName)
              parcel.writeString(lastName)
              parcel.writeString(userName)
              parcel.writeString(password)
              parcel.writeString(confirmPassword)
              parcel.writeString(address)
              parcel.writeString(mobileNumber)
              parcel.writeString(dob)
              parcel.writeString(email)
              parcel.writeString(gender)
              parcel.writeString(profilePicture)
              parcel.writeByte(if (account_activation) 1 else 0)
              parcel.writeInt(dots)
              parcel.writeString(activeTitle)
       }

       override fun describeContents(): Int {
              return 0
       }

       companion object CREATOR : Parcelable.Creator<AuthUser> {
              override fun createFromParcel(parcel: Parcel): AuthUser {
                     return AuthUser(parcel)
              }

              override fun newArray(size: Int): Array<AuthUser?> {
                     return arrayOfNulls(size)
              }
       }
}