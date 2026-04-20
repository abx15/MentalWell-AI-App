package com.mentalwell.ai.domain.usecase

import android.util.Patterns
import com.mentalwell.ai.data.model.User
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.utils.Result
import javax.inject.Inject

/**
 * Use case encapsulating the logic for user sign in and validation.
 */
class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error("Invalid email format.")
        }
        if (password.isBlank()) {
            return Result.Error("Password cannot be empty.")
        }

        return authRepository.signIn(email, password)
    }
}
