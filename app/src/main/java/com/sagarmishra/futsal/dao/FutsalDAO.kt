package com.sagarmishra.futsal.dao


import androidx.room.*
import androidx.room.OnConflictStrategy
import com.sagarmishra.futsal.entityapi.Futsal

@Dao
interface FutsalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFutsals(futsals:MutableList<Futsal>)

    @Query("select * from futsal")
    suspend fun retrieveFutsals():MutableList<Futsal>

    @Query("delete from futsal")
    suspend fun deleteAll()

    @Update
    suspend fun updateInstance(futsal:Futsal)
}