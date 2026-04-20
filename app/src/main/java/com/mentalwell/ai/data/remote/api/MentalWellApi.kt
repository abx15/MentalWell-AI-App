package com.mentalwell.ai.data.remote.api

import com.mentalwell.ai.data.remote.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MentalWellApi {
    @POST("api/auth/token")
    suspend fun getAuthToken(@Body request: TokenRequest): Response<TokenResponse>

    @POST("api/chat")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): Response<ChatResponse>

    @POST("api/analyze")
    suspend fun analyzeText(
        @Body request: AnalyzeRequest
    ): Response<AnalyzeResponse>

    @POST("api/suggest")
    suspend fun getSuggestions(
        @Body request: SuggestRequest
    ): Response<SuggestResponse>
}
