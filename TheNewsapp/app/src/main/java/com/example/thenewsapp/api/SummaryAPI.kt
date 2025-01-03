package com.example.thenewsapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SummaryAPI {
    @Headers("Authorization: Bearer YOUR_HUGGINGFACE_API_TOKEN")
    @POST("models/your-model-name")
    suspend fun getSummary(@Body body: SummaryRequest): Response<SummaryResponse>
}

data class SummaryRequest(
    val inputs: String,
    val parameters: Map<String, Any> = mapOf("max_length" to 150)
)

data class SummaryResponse(
    val summary_text: String
)
