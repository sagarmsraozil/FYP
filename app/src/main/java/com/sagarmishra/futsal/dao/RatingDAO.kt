package com.sagarmishra.futsal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagarmishra.futsal.entityapi.FutsalRating

@Dao
interface RatingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRatings(rating:FutsalRating)

    @Query("select * from FutsalRating as f where f.futsal_id = (:futsalId)")
    suspend fun myRatings(futsalId:String):FutsalRating
}