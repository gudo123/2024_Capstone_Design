package com.example.thenewsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thenewsapp.models.UserTag

@Dao
interface UserTagDAO {
    @Insert
    suspend fun insertUserTag(userTag: UserTag)

    @Query("SELECT * FROM UserTag")
    suspend fun getAllUserTags(): List<UserTag>
}
