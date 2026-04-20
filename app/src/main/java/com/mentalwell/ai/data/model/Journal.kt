package com.mentalwell.ai.data.model

import java.util.UUID

data class Journal(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val title: String = "",
    val text: String = "",
    val aiMood: String? = null,
    val aiEmotion: String? = null,
    val moodScore: Int? = null,
    val suggestions: List<String> = emptyList(),
    val date: Long = System.currentTimeMillis(),
    val timestamp: Long = System.currentTimeMillis(),
    val wordCount: Int = 0
)
