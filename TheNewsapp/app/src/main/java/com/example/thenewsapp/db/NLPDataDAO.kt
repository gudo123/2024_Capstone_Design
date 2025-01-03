package com.example.thenewsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thenewsapp.models.NLPData

@Dao
interface NLPDataDAO {
    @Insert
    suspend fun insertNLPData(nlpData: NLPData)

    @Query("SELECT * FROM NLPData")
    suspend fun getAllNLPData(): List<NLPData>
}
