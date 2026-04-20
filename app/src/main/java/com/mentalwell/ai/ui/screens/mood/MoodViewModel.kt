package com.mentalwell.ai.ui.screens.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalwell.ai.data.model.MoodLog
import com.mentalwell.ai.data.model.MoodType
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.domain.repository_interface.MoodRepository
import com.mentalwell.ai.domain.usecase.SaveMoodUseCase
import com.mentalwell.ai.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for tracking mood history and inputs.
 */
@HiltViewModel
class MoodViewModel @Inject constructor(
    private val saveMoodUseCase: SaveMoodUseCase,
    private val moodRepository: MoodRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _todayMood = MutableStateFlow<MoodLog?>(null)
    val todayMood: StateFlow<MoodLog?> = _todayMood.asStateFlow()

    private val _weeklyMoods = MutableStateFlow<List<MoodLog>>(emptyList())
    val weeklyMoods: StateFlow<List<MoodLog>> = _weeklyMoods.asStateFlow()

    private val _selectedMood = MutableStateFlow<MoodType?>(null)
    val selectedMood: StateFlow<MoodType?> = _selectedMood.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadTodayMood()
        loadWeeklyMoods()
    }

    private fun loadTodayMood() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser() ?: return@launch
            _isLoading.value = true
            when (val result = moodRepository.getTodayMood(user.id)) {
                is Result.Success -> {
                    _todayMood.value = result.data
                    _selectedMood.value = result.data?.mood
                }
                is Result.Error -> _error.value = result.message
                is Result.Loading -> {}
            }
            _isLoading.value = false
        }
    }

    fun loadWeeklyMoods() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser() ?: return@launch
            when (val result = moodRepository.getWeeklyMoods(user.id)) {
                is Result.Success -> _weeklyMoods.value = result.data
                is Result.Error -> _error.value = result.message
                is Result.Loading -> {}
            }
        }
    }

    fun selectMood(moodType: MoodType) {
        _selectedMood.value = moodType
    }

    fun saveMood(note: String?) {
        val currentMood = _selectedMood.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = saveMoodUseCase(currentMood, note)) {
                is Result.Success -> {
                    loadTodayMood()
                    loadWeeklyMoods()
                }
                is Result.Error -> _error.value = result.message
                is Result.Loading -> {}
            }
            _isLoading.value = false
        }
    }

    fun hasSavedTodayMood(): Boolean {
        return _todayMood.value != null
    }
}
