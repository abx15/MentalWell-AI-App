package com.mentalwell.ai.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mentalwell.ai.data.local.DataStoreManager
import com.mentalwell.ai.data.model.ChatMessage
import com.mentalwell.ai.data.remote.api.MentalWellApi
import com.mentalwell.ai.data.remote.model.ChatRequest
import com.mentalwell.ai.data.remote.model.ChatResponse
import com.mentalwell.ai.domain.repository_interface.ChatRepository
import com.mentalwell.ai.utils.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: MentalWellApi,
    private val firestore: FirebaseFirestore,
    private val dataStoreManager: DataStoreManager
) : ChatRepository {

    override suspend fun sendMessage(message: String, history: List<ChatMessage>): Result<ChatResponse> {
        return try {
            val userId = dataStoreManager.getUserId() ?: return Result.Error(Exception("User not authenticated"))
            val remoteHistory = history.map { 
                mapOf("role" to if(it.isUser) "user" else "model", "content" to it.text) 
            }
            val request = ChatRequest(message, remoteHistory, userId)
            val response = api.sendMessage(request)

            if (response.isSuccessful && response.body() != null) {
                val chatResponse = response.body()!!
                val chatDoc = hashMapOf(
                    "userId" to userId,
                    "message" to message,
                    "response" to chatResponse.response,
                    "timestamp" to chatResponse.timestamp,
                    "emotionHint" to chatResponse.emotionHint
                )
                firestore.collection("chats").add(chatDoc).await()
                Result.Success(chatResponse)
            } else {
                Result.Error(Exception(response.message().ifEmpty { "Failed retrieving intelligence" }))
            }
        } catch (e: IOException) {
            Result.Error(Exception("No Internet Connection. Please connect to Wi-Fi/Cellular."))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override fun getChatHistory() = callbackFlow {
        val userId = dataStoreManager.getUserId()
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val listener = firestore.collection("chats").whereEqualTo("userId", userId).orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val messages = mutableListOf<ChatMessage>()
                    for (doc in snapshot.documents) {
                        val reqMsg = doc.getString("message") ?: ""
                        val resMsg = doc.getString("response") ?: ""
                        val ts = doc.getLong("timestamp") ?: 0L
                        val emotion = doc.getString("emotionHint")
                        messages.add(ChatMessage(text = reqMsg, isUser = true, timestamp = ts - 1))
                        messages.add(ChatMessage(text = resMsg, isUser = false, timestamp = ts, emotionHint = emotion))
                    }
                    trySend(messages)
                }
            }
        awaitClose { listener.remove() }
    }
}
