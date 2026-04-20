package com.mentalwell.ai.data.model

/**
 * Data class representing a logged mood instance.
 */
data class MoodLog(
    val id: String = "",
    val userId: String = "",
    val mood: MoodType = MoodType.NEUTRAL,
    val note: String? = null,
    val date: Long = 0L,         // Start of day timestamp for easy querying
    val timestamp: Long = 0L,    // Exact UTC time of the entry
    val moodScore: Int = mood.score
)
