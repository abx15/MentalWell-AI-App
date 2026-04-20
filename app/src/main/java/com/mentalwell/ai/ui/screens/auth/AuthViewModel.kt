package com.mentalwell.ai.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalwell.ai.domain.usecase.SignInUseCase
import com.mentalwell.ai.domain.usecase.SignUpUseCase
import com.mentalwell.ai.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling UI state and business logic of Authentication screens.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val result = signInUseCase(email, password)
            _uiState.value = when (result) {
                is Result.Success -> AuthState.Success
                is Result.Error -> AuthState.Error(result.message)
                is Result.Loading -> AuthState.Loading
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val result = signUpUseCase(name, email, password)
            _uiState.value = when (result) {
                is Result.Success -> AuthState.Success
                is Result.Error -> AuthState.Error(result.message)
                is Result.Loading -> AuthState.Loading
            }
        }
    }
    
    fun resetState() {
        _uiState.value = AuthState.Idle
    }
}

/**
 * State representing the Authentication UI flow.
 */
sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
