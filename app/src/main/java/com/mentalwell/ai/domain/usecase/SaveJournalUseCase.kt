package com.mentalwell.ai.domain.usecase

import com.mentalwell.ai.data.local.DataStoreManager
import com.mentalwell.ai.data.model.Journal
import com.mentalwell.ai.data.remote.api.MentalWellApi
import com.mentalwell.ai.data.remote.model.AnalyzeRequest
import com.mentalwell.ai.domain.repository_interface.JournalRepository
import com.mentalwell.ai.utils.Result
import java.util.UUID
import javax.inject.Inject

class SaveJournalUseCase @Inject constructor(
    private val journalRepository: JournalRepository,
    private val api: MentalWellApi,
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke(id: String?, title: String, text: String): Result<String> {
        if (title.trim().isEmpty()) return Result.Error(Exception("Title cannot be empty"))
        if (text.trim().length < 10) return Result.Error(Exception("Journal text must be at least 10 characters"))

        val userId = dataStoreManager.getUserId() ?: return Result.Error(Exception("User not authenticated"))
        val wordCount = text.trim().split("\\s+".toRegex()).size
        
        val journalId = id ?: UUID.randomUUID().toString()
        var journal = Journal(
            id = journalId, userId = userId, title = title.trim(),
            text = text.trim(), wordCount = wordCount,
            date = System.currentTimeMillis(), timestamp = System.currentTimeMillis()
        )

        val saveResult = if (id == null) journalRepository.saveJournal(journal) else {
            val fetchResult = journalRepository.getJournal(id)
            if (fetchResult is Result.Success) {
                journal = journal.copy(
                    aiMood = fetchResult.data.aiMood, aiEmotion = fetchResult.data.aiEmotion,
                    moodScore = fetchResult.data.moodScore, suggestions = fetchResult.data.suggestions,
                    timestamp = fetchResult.data.timestamp
                )
            }
            val updateRes = journalRepository.updateJournal(journal)
            if (updateRes is Result.Success) Result.Success(journalId) else Result.Error(Exception("Update failed"))
        }

        if (saveResult is Result.Error) return saveResult

        try {
            val response = api.analyzeText(AnalyzeRequest(text = journal.text, userId = userId))
            if (response.isSuccessful && response.body() != null) {
                val analyzeResponse = response.body()!!
                journal = journal.copy(
                    aiMood = analyzeResponse.emotion, aiEmotion = analyzeResponse.emotion,
                    moodScore = analyzeResponse.moodScore, suggestions = analyzeResponse.suggestions
                )
                journalRepository.updateJournal(journal)
            }
        } catch (e: Exception) {
            // Fails safe silently defaulting to empty analysis locally successfully saved. 
        }

        return Result.Success(journalId)
    }
}
