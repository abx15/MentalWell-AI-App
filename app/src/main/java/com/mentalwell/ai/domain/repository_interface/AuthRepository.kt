package com.mentalwell.ai.domain.repository_interface

import com.mentalwell.ai.data.model.User
import com.mentalwell.ai.utils.Result

/**
 * Interface defining authentication operations.
 */
interface AuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut()
    suspend fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
}
