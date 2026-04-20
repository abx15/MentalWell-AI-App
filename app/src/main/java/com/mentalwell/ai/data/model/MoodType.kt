package com.mentalwell.ai.data.model

/**
 * Enum representing available mood states with associated UI details and scoring.
 */
enum class MoodType(val emoji: String, val label: String, val color: Long, val score: Int) {
    HAPPY("😊", "Happy", 0xFF4CAF50, 5),
    NEUTRAL("😐", "Neutral", 0xFF9E9E9E, 3),
    SAD("😢", "Sad", 0xFF2196F3, 2),
    ANGRY("😠", "Angry", 0xFFF44336, 1),
    ANXIOUS("😰", "Anxious", 0xFFFF9800, 2)
}
