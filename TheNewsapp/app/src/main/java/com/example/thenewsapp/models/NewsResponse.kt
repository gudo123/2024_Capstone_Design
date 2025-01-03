package com.example.thenewsapp.models

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val results: MutableList<Article>  // MutableList로 정의
)
