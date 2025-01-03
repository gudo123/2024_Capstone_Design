package com.example.thenewsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thenewsapp.models.Tag

@Dao
interface TagDAO {
    @Insert
    suspend fun insertTag(tag: Tag)

    @Query("SELECT * FROM Tag")
    suspend fun getAllTags(): List<Tag>
}
