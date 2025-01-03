package com.example.thenewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CrawlingData")
data class CrawlingData(
    @PrimaryKey(autoGenerate = true)
    val CrawlingDataID: Int,
    val Title: String,
    val Content: String,
    val PublishDate: String,
    val Source: String
)
