package com.mentalwell.ai.domain.repository_interface

import com.mentalwell.ai.data.model.Journal
import com.mentalwell.ai.utils.Result
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    suspend fun saveJournal(journal: Journal): Result<String>
    suspend fun updateJournal(journal: Journal): Result<Unit>
    suspend fun deleteJournal(id: String): Result<Unit>
    fun getJournals(userId: String): Flow<List<Journal>>
    suspend fun getJournal(id: String): Result<Journal>
}
