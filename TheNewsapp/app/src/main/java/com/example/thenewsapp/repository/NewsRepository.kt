package com.example.thenewsapp.repository

import com.example.thenewsapp.api.RetrofitInstance
import com.example.thenewsapp.db.ArticleDatabase
import com.example.thenewsapp.models.Article
import com.example.thenewsapp.api.SummaryRequest
import com.example.thenewsapp.models.NewsResponse
import com.example.thenewsapp.util.Constants
import retrofit2.Response

class NewsRepository(val db: ArticleDatabase) {

    // 최신 헤드라인 가져오기 (Newsdata.io의 latest 엔드포인트)
    suspend fun getHeadlines(countryCode: String = "kr", language: String = "ko"): Response<NewsResponse> {
        return try {
            // API_KEY를 명시적으로 전달
            RetrofitInstance.newsApi.getHeadlines(countryCode, Constants.API_KEY)
        } catch (e: Exception) {
            println("Error fetching headlines: ${e.message}")
            throw e
        }
    }

    // 뉴스 검색 (Newsdata.io API에 맞춤)
    suspend fun searchNews(searchQuery: String, language: String = "ko", countryCode: String = "kr"): Response<NewsResponse> {
        return try {
            // API_KEY를 명시적으로 전달
            RetrofitInstance.newsApi.searchForNews(searchQuery, countryCode, Constants.API_KEY)
        } catch (e: Exception) {
            println("Error searching news: ${e.message}")
            throw e
        }
    }

    // 뉴스 요약 요청
    suspend fun getSummary(summaryRequest: SummaryRequest) = try {
        RetrofitInstance.summaryApi.getSummary(summaryRequest)
    } catch (e: Exception) {
        // 에러 핸들링
        println("Error fetching summary: ${e.message}")
        throw e
    }

    // 기사 저장/업데이트
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    // 즐겨찾기된 뉴스 가져오기
    fun getFavouriteNews() = db.getArticleDao().getAllArticles()

    // 기사 삭제
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}
