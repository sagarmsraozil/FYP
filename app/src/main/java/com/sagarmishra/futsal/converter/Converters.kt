package com.sagarmishra.futsal.converter

import androidx.room.TypeConverter
import com.sagarmishra.futsal.entityapi.Futsal

class Converters {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromFutsal(value:Futsal):String
        {
            return value._id
        }

        @TypeConverter
        @JvmStatic
        fun toFutsal(value: String):Futsal
        {
            return value?.let { Futsal(it) }
        }

    }
}