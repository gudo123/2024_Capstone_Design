package com.example.thenewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tag")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val TagID: Int,
    val TagName: String
)
