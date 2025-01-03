package com.example.thenewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    val UserID: Int,
    val Username: String,
    val Email: String,
    val Password: String
)
