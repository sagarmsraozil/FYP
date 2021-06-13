package com.sagarmishra.futsal.dao

import androidx.room.*
import com.sagarmishra.futsal.entityapi.FutsalInstances

@Dao
interface InstanceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFutsalTimeInstance(instances:MutableList<FutsalInstances>)

    @Query("select * from FutsalInstances as f where f.day = (:day) and f.date2 = (:date) and f.futsal_id = (:futsal_id)")
    suspend fun retrieveData(day:String,date:String,futsal_id:String):MutableList<FutsalInstances>

    @Query("delete from FutsalInstances where date2 < (:today)")
    suspend fun deleteAll(today:String)
}