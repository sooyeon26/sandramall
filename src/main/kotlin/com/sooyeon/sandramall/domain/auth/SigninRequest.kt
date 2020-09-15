package com.sooyeon.sandramall.domain.auth

data class SigninRequest(
    val email: String,
    val password: String
)