package com.kanowa.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: UserResponse? = null,
    val token: String? = null
)
