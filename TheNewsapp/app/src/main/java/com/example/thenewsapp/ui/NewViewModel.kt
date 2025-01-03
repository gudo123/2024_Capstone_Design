package com.example.thenewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.thenewsapp.models.Article
import com.example.thenewsapp.models.NewsResponse
import com.example.thenewsapp.repository.NewsRepository
import com.example.thenewsapp.util.Constants
import com.example.thenewsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.nio.charset.StandardCharsets



class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app) {

    val headlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage = 1
    var headlinesResource: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null
    private var newSearchQuery: String? = null
    private var oldSearchQuery: String? = null
    private var searchJob: Job? = null

    // 기본 헤드라인 요청
    init {
        getHeadlines()
    }

    // 헤드라인 뉴스 가져오기
    fun getHeadlines() = viewModelScope.launch {
        Log.d("NewsViewModel", "Requesting headlines, page: $headlinesPage")
        headlinesInternet()
    }

    // 검색 뉴스 요청
    fun searchNews(searchQuery: String) {
        newSearchQuery = searchQuery

        // 기존 검색 작업이 있으면 취소
        searchJob?.cancel()

        // 새로운 검색 작업 실행
        searchJob = viewModelScope.launch {
            delay(Constants.SEARCH_NEWS_TIME_DELAY)

            // 검색어 디코딩 및 로그 출력
            val decodedQuery = URLDecoder.decode(searchQuery, StandardCharsets.UTF_8.toString())
            Log.d("SearchQuery_DEBUG", "Decoded query: $decodedQuery")

            Log.d("NewsViewModel", "Searching news for query: $searchQuery, page: $searchNewsPage")
            searchNewsInternet(searchQuery)
        }
    }


    // 헤드라인 API 응답 처리
    private fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.results.isNullOrEmpty()) {
                    Log.d("NewsViewModel", "No articles found")
                    return Resource.Error("No articles available")
                }
                headlinesPage++
                if (headlinesResource == null) {
                    headlinesResource = resultResponse
                } else {
                    val oldArticles = headlinesResource?.results
                    val newArticles = resultResponse.results
                    oldArticles?.addAll(newArticles)
                }
                Log.d("NewsViewModel", "Received ${resultResponse.results.size} articles")
                return Resource.Success(headlinesResource ?: resultResponse)
            }
        }
        Log.e("NewsViewModel", "Error fetching headlines: ${response.message()}")
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.results.isNullOrEmpty()) {
                    Log.d("NewsViewModel", "No articles found")
                    return Resource.Error("No articles available")
                }
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.results
                    val newArticles = resultResponse.results
                    oldArticles?.addAll(newArticles)
                }
                Log.d("NewsViewModel", "Received ${resultResponse.results.size} articles for search query")
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        Log.e("NewsViewModel", "Error fetching search news: ${response.message()}")
        return Resource.Error(response.message())
    }

    // 즐겨찾기에 기사 추가
    fun addToFavourites(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    // 즐겨찾기한 기사 가져오기
    fun getFavouriteNews() = newsRepository.getFavouriteNews()

    // 기사 삭제
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    // 인터넷 연결 확인 메서드
    private fun internetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    // 헤드라인 뉴스 가져오기
    private suspend fun headlinesInternet() {
        headlines.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                // 로그를 사용하여 API_KEY 값 확인
                Log.d("API_KEY_DEBUG", "API Key: ${Constants.API_KEY}")

                val response = newsRepository.getHeadlines("kr", Constants.API_KEY)
                Log.d("NewsViewModel", "API Response: ${response.body()?.results?.size} articles received")
                headlines.postValue(handleHeadlinesResponse(response))
            } else {
                headlines.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> headlines.postValue(Resource.Error("Network Failure"))
                else -> headlines.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    // 검색 뉴스 가져오기
    private suspend fun searchNewsInternet(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                // API 키 및 국가 코드 확인
                Log.d("SearchNews_DEBUG", "Searching news with query: $searchQuery, API Key: ${Constants.API_KEY}")

                // 올바른 country와 apiKey 전달
                val response = newsRepository.searchNews(searchQuery, Constants.API_KEY)

                // 응답 결과 로그
                Log.d("NewsViewModel", "API Response: ${response.body()?.results?.size} articles received for search query: $searchQuery")
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

}
