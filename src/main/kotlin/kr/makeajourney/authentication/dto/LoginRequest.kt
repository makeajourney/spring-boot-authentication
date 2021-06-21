package kr.makeajourney.authentication.dto

data class LoginRequest(
    val username: String,
    val password: String,
)
