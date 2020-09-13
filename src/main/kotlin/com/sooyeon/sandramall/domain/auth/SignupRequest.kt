package com.sooyeon.sandramall.domain.auth

data class SignupRequest(
    val email: String,
    val name: String,
    val password: String
)