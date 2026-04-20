package com.mentalwell.ai.domain.repository_interface

import com.mentalwell.ai.data.model.MoodLog
import com.mentalwell.ai.utils.Result

interface MoodRepository {
    suspend fun saveMood(moodLog: MoodLog): Result<Unit>
    suspend fun getTodayMood(userId: String): Result<MoodLog?>
    suspend fun getWeeklyMoods(userId: String): Result<List<MoodLog>>
    suspend fun getMoodHistory(userId: String, limit: Int): Result<List<MoodLog>>
}
