package com.example.thenewsapp.api

import com.example.thenewsapp.models.NewsResponse
import com.example.thenewsapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    // 최신 뉴스 가져오기 (latest 엔드포인트 사용)
    @GET("latest")
    suspend fun getHeadlines(
        @Query("country") countryCode: String = "kr",
        @Query("apikey") apikey: String = Constants.API_KEY
    ): Response<NewsResponse>


    // 특정 키워드로 뉴스 검색 (검색 시 언어와 페이지 번호 추가)
    @GET("latest")
    suspend fun searchForNews(
        @Query(value = "q", encoded = true) searchQuery: String, // 검색어 (예: "암호화폐")
        @Query("country") countryCode: String = "kr",  // 기본 국가 코드 설정
        @Query("apikey") apiKey: String = Constants.API_KEY  // Constants에서 API_KEY 사용
    ): Response<NewsResponse>
}
