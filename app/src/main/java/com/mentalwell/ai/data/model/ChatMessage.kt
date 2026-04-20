package com.mentalwell.ai.data.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String = "",
    val isUser: Boolean = true,
    val timestamp: Long = System.currentTimeMillis(),
    val emotionHint: String? = null,
    val isLoading: Boolean = false
)
