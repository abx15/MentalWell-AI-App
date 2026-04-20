package com.mentalwell.ai.domain.usecase

import android.util.Patterns
import com.mentalwell.ai.data.model.User
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.utils.Result
import javax.inject.Inject

/**
 * Use case encapsulating the logic for user sign up and validation.
 */
class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<User> {
        if (name.isBlank()) {
            return Result.Error("Name cannot be empty.")
        }
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error("Invalid email format.")
        }
        if (password.isBlank() || password.length < 6) {
            return Result.Error("Password must be at least 6 characters.")
        }

        return authRepository.signUp(name, email, password)
    }
}
