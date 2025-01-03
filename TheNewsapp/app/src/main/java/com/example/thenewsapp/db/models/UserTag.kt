package com.example.thenewsapp.models

import androidx.room.Entity

@Entity(primaryKeys = ["UserID", "TagID"])
data class UserTag(
    val UserID: Int,
    val TagID: Int
)
