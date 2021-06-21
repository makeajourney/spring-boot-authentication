package kr.makeajourney.authentication.dto

data class AuthToken(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String,
    val refreshExpiresIn: Long,
    val tokenType: String,
)
