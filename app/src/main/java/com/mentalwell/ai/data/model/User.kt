package com.mentalwell.ai.data.model

/**
 * Data class representing a User in the MentalWell AI app.
 */
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val profilePicUrl: String? = null
)
