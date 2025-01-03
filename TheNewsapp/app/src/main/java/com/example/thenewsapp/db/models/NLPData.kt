package com.example.thenewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NLPData")
data class NLPData(
    @PrimaryKey(autoGenerate = true)
    val NLPDataID: Int,
    val CrawlingDataID: Int,
    val Keywords: String,
    val SentimentAnalysis: String
)
