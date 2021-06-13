package com.sagarmishra.futsal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.entityapi.BookingRoomDB

@Dao
interface BookingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookingRecords(records:MutableList<BookingRoomDB>)

    @Query("select * from  BookingRoomDB")
    suspend fun retrieveBookings():MutableList<BookingRoomDB>

    @Query("delete from BookingRoomDB")
    suspend fun deleteBookings()

    @Query("delete from BookingRoomDB where _id = (:id)")
    suspend fun deleteBooking(id:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(records: MutableList<Booking>)

    @Query("delete from booking")
    suspend fun deleteAllBookings()

    @Query("select * from booking")
    suspend fun retrieveMyBookings():MutableList<Booking>
}