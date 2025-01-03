package com.example.thenewsapp.UREcoder

import java.net.URLEncoder
import java.io.UnsupportedEncodingException

class UREcoder {
    // 쿼리를 UTF-8로 인코딩하는 함수
    fun encodeQuery(query: String): String? {
        return try {
            URLEncoder.encode(query, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            null
        }
    }
}
