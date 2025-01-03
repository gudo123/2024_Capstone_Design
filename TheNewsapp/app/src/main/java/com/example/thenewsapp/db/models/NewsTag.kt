package com.example.thenewsapp.models

import androidx.room.Entity

@Entity(primaryKeys = ["NewsID", "TagID"])
data class NewsTag(
    val NewsID: Int,
    val TagID: Int
)
