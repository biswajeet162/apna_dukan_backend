package com.apnadukan.auth.dto.response

data class AuthResponse(
	val otpRefId: String,
	val expiresInSeconds: Int
)

