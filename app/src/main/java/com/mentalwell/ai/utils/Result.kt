package com.mentalwell.ai.utils

/**
 * A generic sealed class to handle API and local data operation responses.
 * Supported states: Success, Error, Loading.
 * 
 * @param T The type of data being handled.
 */
sealed class Result<out T> {
    
    /**
     * Represents a successful operation.
     * @param data The typed data returned on success.
     */
    data class Success<out T>(val data: T) : Result<T>()
    
    /**
     * Represents a failed operation.
     * @param message A string representing the error message.
     */
    data class Error(val message: String) : Result<Nothing>()
    
    /**
     * Represents an operation currently in progress.
     */
    data object Loading : Result<Nothing>()
}
