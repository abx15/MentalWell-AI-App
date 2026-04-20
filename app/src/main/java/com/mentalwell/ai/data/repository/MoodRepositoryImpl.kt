package com.mentalwell.ai.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mentalwell.ai.data.model.MoodLog
import com.mentalwell.ai.domain.repository_interface.MoodRepository
import com.mentalwell.ai.utils.Constants
import com.mentalwell.ai.utils.Result
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

/**
 * Implementation of [MoodRepository] capturing Firestore capabilities.
 */
class MoodRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MoodRepository {

    override suspend fun saveMood(moodLog: MoodLog): Result<Unit> {
        return try {
            val docRef = if (moodLog.id.isBlank()) {
                firestore.collection(Constants.MOOD_LOGS_COLLECTION).document()
            } else {
                firestore.collection(Constants.MOOD_LOGS_COLLECTION).document(moodLog.id)
            }
            
            val logToSave = moodLog.copy(id = docRef.id)
            // Caches automatically because of Firestore defaults
            docRef.set(logToSave).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Failed to save mood.")
        }
    }

    override suspend fun getTodayMood(userId: String): Result<MoodLog?> {
        return try {
            val startOfDay = getStartOfDayTimestamp()
            val querySnapshot = firestore.collection(Constants.MOOD_LOGS_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("date", startOfDay)
                .limit(1)
                .get()
                .await()

            val log = querySnapshot.documents.firstOrNull()?.toObject(MoodLog::class.java)
            Result.Success(log)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Failed to fetch today's mood.")
        }
    }

    override suspend fun getWeeklyMoods(userId: String): Result<List<MoodLog>> {
        return try {
            val sevenDaysAgo = getStartOfDayTimestamp(daysOffset = -7)
            val querySnapshot = firestore.collection(Constants.MOOD_LOGS_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("date", sevenDaysAgo)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .await()

            val logs = querySnapshot.toObjects(MoodLog::class.java)
            Result.Success(logs)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Failed to fetch weekly moods.")
        }
    }

    override suspend fun getMoodHistory(userId: String, limit: Int): Result<List<MoodLog>> {
        return try {
            val querySnapshot = firestore.collection(Constants.MOOD_LOGS_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            val logs = querySnapshot.toObjects(MoodLog::class.java)
            Result.Success(logs)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Failed to fetch mood history.")
        }
    }

    private fun getStartOfDayTimestamp(daysOffset: Int = 0): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (daysOffset != 0) {
                add(Calendar.DAY_OF_YEAR, daysOffset)
            }
        }
        return calendar.timeInMillis
    }
}
