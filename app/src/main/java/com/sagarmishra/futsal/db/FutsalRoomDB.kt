package com.sagarmishra.futsal.db

import android.content.Context
import androidx.room.*
import com.sagarmishra.futsal.converter.BookingConverter
import com.sagarmishra.futsal.converter.Converters
import com.sagarmishra.futsal.dao.*
import com.sagarmishra.futsal.entityapi.*
import com.sagarmishra.futsal.entityroom.User

@Database(
       entities = [(Futsal::class),(FutsalInstances::class),(BookingRoomDB::class),(Booking::class)],
       version = 3,
        exportSchema = false
)
@TypeConverters(Converters::class,BookingConverter::class)
abstract class FutsalRoomDB():RoomDatabase() {

    abstract fun getFutsalDAO():FutsalDAO
    abstract fun getInstancesDAO():InstanceDAO
    abstract fun getBookingDAO(): BookingDAO

    companion object{
        @Volatile
        private var instance : FutsalRoomDB? = null

        fun getFutsalDBInstance(context:Context):FutsalRoomDB{

            if(instance == null)
            {
                synchronized(FutsalRoomDB::class){
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context):FutsalRoomDB
        {
            return Room.databaseBuilder(context.applicationContext,FutsalRoomDB::class.java,"FutsalArenaRoom").fallbackToDestructiveMigration().build()
        }
    }
}