package com.mentalwell.ai.domain.repository_interface

import com.mentalwell.ai.data.model.ChatMessage
import com.mentalwell.ai.data.remote.model.ChatResponse
import kotlinx.coroutines.flow.Flow
import com.mentalwell.ai.utils.Result

interface ChatRepository {
    suspend fun sendMessage(message: String, history: List<ChatMessage>): Result<ChatResponse>
    fun getChatHistory(): Flow<List<ChatMessage>>
}
