package com.sagarmishra.futsal.dao

import androidx.room.Dao
import androidx.room.*
import com.sagarmishra.futsal.entityroom.User


@Dao
interface UserDAO {
    @Insert
    suspend fun insertUser(user: User)

    @Query("select * from User as u where username = (:un) and password = (:pw)")
    suspend fun authenticateUser(un:String = "",pw:String = ""):User

}

