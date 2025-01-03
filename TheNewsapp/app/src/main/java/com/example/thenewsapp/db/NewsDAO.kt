package com.example.thenewsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thenewsapp.models.News

@Dao
interface NewsDAO {
    @Insert
    suspend fun insertNews(news: News)

    @Query("SELECT * FROM News")
    suspend fun getAllNews(): List<News>
}
