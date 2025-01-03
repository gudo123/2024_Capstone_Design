package com.example.thenewsapp.util

class Constants {
    companion object {
        // Newsdata.io API 키
        const val API_KEY = "pub_5411876b8fdcc3c39677b094216c72387d528"

        // Newsdata.io의 기본 URL
        const val BASE_URL = "https://newsdata.io/api/1/"

        // 검색할 때의 딜레이 시간
        const val SEARCH_NEWS_TIME_DELAY = 500L

        // 페이지당 결과 개수
        const val QUERY_PAGE_SIZE = 10
    }
}
