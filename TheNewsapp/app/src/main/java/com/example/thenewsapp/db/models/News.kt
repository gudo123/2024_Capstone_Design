package com.example.thenewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "News")
data class News(
    @PrimaryKey(autoGenerate = true)
    val NewsID: Int,
    val Title: String,
    val Content: String,
    val PublishDate: String,
    val Source: String
)
