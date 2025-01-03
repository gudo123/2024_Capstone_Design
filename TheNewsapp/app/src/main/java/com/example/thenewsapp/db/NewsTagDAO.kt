package com.example.thenewsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thenewsapp.models.NewsTag

@Dao
interface NewsTagDAO {
    @Insert
    suspend fun insertNewsTag(newsTag: NewsTag)

    @Query("SELECT * FROM NewsTag")
    suspend fun getAllNewsTags(): List<NewsTag>
}
