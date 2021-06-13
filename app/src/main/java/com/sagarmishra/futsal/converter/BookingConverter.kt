package com.sagarmishra.futsal.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityapi.FutsalInstances

class BookingConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromFutsalTimeInstance(timeInstance:FutsalInstances):String
        {
            return timeInstance._id
        }

        @TypeConverter
        @JvmStatic
        fun toFutsalTimeInstance(time:String):FutsalInstances
        {
            return time?.let { FutsalInstances(it) }
        }

        @TypeConverter
        @JvmStatic
        fun fromUser(user:AuthUser):String
        {
            return user._id
        }

        @TypeConverter
        @JvmStatic
        fun toUser(user:String):AuthUser
        {
            return user?.let { AuthUser(it) }
        }
    }
}