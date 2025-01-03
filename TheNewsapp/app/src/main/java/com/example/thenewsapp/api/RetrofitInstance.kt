package com.example.thenewsapp.api

import com.example.thenewsapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // HttpLoggingInterceptor 설정
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient에 Interceptor 추가
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Retrofit 인스턴스 생성 (newsdata.io API 사용)
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)  // BASE_URL을 Constants에서 가져오기
            .client(client)  // 클라이언트에 logging 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // NewsAPI 인터페이스를 Retrofit에 연결
    val newsApi: NewsAPI by lazy {
        retrofit.create(NewsAPI::class.java)
    }

    // Hugging Face API를 위한 별도의 Retrofit 인스턴스
    val summaryApi: SummaryAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://api-inference.huggingface.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SummaryAPI::class.java)
    }
}
