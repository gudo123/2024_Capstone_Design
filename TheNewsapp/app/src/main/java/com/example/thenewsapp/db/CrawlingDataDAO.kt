package com.example.thenewsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thenewsapp.models.CrawlingData

@Dao
interface CrawlingDataDAO {
    @Insert
    suspend fun insertCrawlingData(crawlingData: CrawlingData)

    @Query("SELECT * FROM CrawlingData")
    suspend fun getAllCrawlingData(): List<CrawlingData>
}
