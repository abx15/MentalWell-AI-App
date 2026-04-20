package com.mentalwell.ai.domain.usecase

import com.mentalwell.ai.data.model.MoodLog
import com.mentalwell.ai.data.model.MoodType
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.domain.repository_interface.MoodRepository
import com.mentalwell.ai.utils.Result
import java.util.Calendar
import javax.inject.Inject

/**
 * UseCase to efficiently store daily mood tracking parameters.
 */
class SaveMoodUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val moodRepository: MoodRepository
) {
    suspend operator fun invoke(moodType: MoodType, note: String?): Result<Unit> {
        val user = authRepository.getCurrentUser()
            ?: return Result.Error("User not logged in.")

        val todayMoodResult = moodRepository.getTodayMood(user.id)
        
        val currentTime = System.currentTimeMillis()
        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val moodLog = if (todayMoodResult is Result.Success && todayMoodResult.data != null) {
            // Update existing today's mood
            todayMoodResult.data.copy(
                mood = moodType,
                note = note ?: todayMoodResult.data.note,
                moodScore = moodType.score,
                timestamp = currentTime
            )
        } else {
            // Create new
            MoodLog(
                userId = user.id,
                mood = moodType,
                note = note,
                date = startOfDay,
                timestamp = currentTime,
                moodScore = moodType.score
            )
        }

        return moodRepository.saveMood(moodLog)
    }
}
