package com.mentalwell.ai.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalwell.ai.data.model.ChatMessage
import com.mentalwell.ai.domain.repository_interface.ChatRepository
import com.mentalwell.ai.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        viewModelScope.launch {
            chatRepository.getChatHistory().collectLatest { history ->
                // Add loading indicator cleanly matching real-time architecture if generating
                val modifiedHistory = history.toMutableList()
                if (_isTyping.value) modifiedHistory.add(ChatMessage(isUser = false, isLoading = true))
                _messages.value = modifiedHistory
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        val userMsg = ChatMessage(text = text, isUser = true)
        val loadingMsg = ChatMessage(isUser = false, isLoading = true)
        
        _messages.update { current ->
            val update = current.filter { !it.isLoading }.toMutableList()
            update.add(userMsg)
            update.add(loadingMsg)
            update
        }
        
        _isTyping.value = true
        _error.value = null

        viewModelScope.launch {
            val contextWindow = _messages.value.filter { !it.isLoading }.takeLast(10)
            when (val r = chatRepository.sendMessage(text, contextWindow)) {
                is Result.Error -> {
                    _error.value = r.exception.localizedMessage ?: "Unknown server response."
                    _messages.update { c -> c.filter { !it.isLoading } }
                }
                else -> {
                    // Success removed by Firestore reactive callback automatically cleanly!
                }
            }
            _isTyping.value = false
        }
    }
    
    fun clearError() { _error.value = null }
}
