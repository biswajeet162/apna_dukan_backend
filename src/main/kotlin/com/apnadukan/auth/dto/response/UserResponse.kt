package com.apnadukan.auth.dto.response

data class UserResponse(
	val userId: Long,
	val name: String,
	val mobileNumber: String,
	val email: String?,
	val role: String
)

