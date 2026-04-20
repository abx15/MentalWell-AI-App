package com.mentalwell.ai.ui.screens.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalwell.ai.data.local.DataStoreManager
import com.mentalwell.ai.data.model.Journal
import com.mentalwell.ai.domain.repository_interface.JournalRepository
import com.mentalwell.ai.domain.usecase.SaveJournalUseCase
import com.mentalwell.ai.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class JournalUiState {
    object Idle : JournalUiState()
    object Saving : JournalUiState()
    object Analyzing : JournalUiState()
    object Success : JournalUiState()
    data class Error(val message: String) : JournalUiState()
}

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val saveJournalUseCase: SaveJournalUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> = _journals.asStateFlow()

    private val _uiState = MutableStateFlow<JournalUiState>(JournalUiState.Idle)
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    private val _selectedJournal = MutableStateFlow<Journal?>(null)
    val selectedJournal: StateFlow<Journal?> = _selectedJournal.asStateFlow()

    fun loadJournals() {
        val userId = dataStoreManager.getUserId() ?: return
        viewModelScope.launch {
            journalRepository.getJournals(userId).collectLatest { _journals.value = it }
        }
    }

    fun saveJournal(id: String?, title: String, text: String) {
        _uiState.value = JournalUiState.Analyzing // Matches the "Analyzing your mood..." explicitly
        viewModelScope.launch {
            when (val result = saveJournalUseCase(id, title, text)) {
                is Result.Success -> _uiState.value = JournalUiState.Success
                is Result.Error -> _uiState.value = JournalUiState.Error(result.exception.localizedMessage ?: "Failed")
                else -> {}
            }
        }
    }

    fun deleteJournal(id: String) {
        viewModelScope.launch { journalRepository.deleteJournal(id) }
    }

    fun loadJournalDetails(id: String) {
        viewModelScope.launch {
            when (val res = journalRepository.getJournal(id)) {
                is Result.Success -> _selectedJournal.value = res.data
                is Result.Error -> _uiState.value = JournalUiState.Error("Entry not found")
                else -> {}
            }
        }
    }

    fun clearState() { _uiState.value = JournalUiState.Idle }
}
