package com.example.thenewsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thenewsapp.models.User

@Dao
interface UserDAO {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>
}
