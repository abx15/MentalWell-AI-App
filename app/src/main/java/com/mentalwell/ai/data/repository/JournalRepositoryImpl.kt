package com.mentalwell.ai.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mentalwell.ai.data.model.Journal
import com.mentalwell.ai.domain.repository_interface.JournalRepository
import com.mentalwell.ai.utils.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : JournalRepository {

    override suspend fun saveJournal(journal: Journal): Result<String> {
        return try {
            firestore.collection("journals").document(journal.id).set(journal).await()
            Result.Success(journal.id)
        } catch (e: IOException) {
            Result.Error(Exception("No internet connection offline fallback applied"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateJournal(journal: Journal): Result<Unit> {
        return try {
            firestore.collection("journals").document(journal.id).set(journal).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteJournal(id: String): Result<Unit> {
        return try {
            firestore.collection("journals").document(id).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getJournals(userId: String): Flow<List<Journal>> = callbackFlow {
        val listener = firestore.collection("journals")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Journal::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getJournal(id: String): Result<Journal> {
        return try {
            val snapshot = firestore.collection("journals").document(id).get().await()
            val journal = snapshot.toObject(Journal::class.java)
            if (journal != null) Result.Success(journal) else Result.Error(Exception("Not found"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
