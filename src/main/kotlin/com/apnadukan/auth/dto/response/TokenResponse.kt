package com.apnadukan.auth.dto.response

data class TokenResponse(
	val userId: Long,
	val isNewUser: Boolean,
	val accessToken: String,
	val refreshToken: String,
	val expiresIn: Int
)

