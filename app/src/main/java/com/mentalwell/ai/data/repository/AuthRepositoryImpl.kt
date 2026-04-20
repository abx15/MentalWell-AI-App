package com.mentalwell.ai.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.mentalwell.ai.data.model.User
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.utils.Constants
import com.mentalwell.ai.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of [AuthRepository] utilizing Firebase Authentication and Firestore.
 */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Error("Account created but user not found.")

            val user = User(
                id = firebaseUser.uid,
                name = name,
                email = email,
                createdAt = System.currentTimeMillis()
            )

            // Save user data to Firestore
            firestore.collection(Constants.USERS_COLLECTION)
                .document(user.id)
                .set(user)
                .await()

            Result.Success(user)
        } catch (e: FirebaseAuthException) {
            Result.Error(e.message ?: "Authentication failed.")
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An unexpected error occurred.")
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Error("Authentication successful but user not found.")

            val document = firestore.collection(Constants.USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val user = document.toObject(User::class.java)
                ?: return Result.Error("User data not found in database.")

            Result.Success(user)
        } catch (e: FirebaseAuthException) {
            Result.Error(e.message ?: "Authentication failed.")
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An unexpected error occurred.")
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return try {
            val document = firestore.collection(Constants.USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
