package com.mentalwell.ai.data.remote.model

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val message: String,
    @SerializedName("conversation_history") val conversationHistory: List<Map<String, String>>,
    @SerializedName("user_id") val userId: String
)

data class ChatResponse(
    val response: String,
    val timestamp: Long,
    @SerializedName("emotion_hint") val emotionHint: String?
)

data class AnalyzeRequest(
    val text: String,
    @SerializedName("user_id") val userId: String
)

data class AnalyzeResponse(
    val emotion: String,
    val confidence: Double,
    @SerializedName("mood_score") val moodScore: Int,
    val suggestions: List<String>
)

data class SuggestRequest(
    val mood: String,
    @SerializedName("recent_emotions") val recentEmotions: List<String>,
    @SerializedName("user_id") val userId: String
)

data class SuggestResponse(
    val suggestions: List<Suggestion>
)

data class Suggestion(
    val title: String,
    val description: String,
    val duration: String,
    val type: String
)

data class TokenRequest(
    @SerializedName("firebase_uid") val firebaseUid: String,
    val email: String
)

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int
)
