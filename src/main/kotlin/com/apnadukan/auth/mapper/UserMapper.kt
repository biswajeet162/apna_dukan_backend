package com.apnadukan.auth.mapper

import com.apnadukan.auth.dto.response.UserResponse
import com.apnadukan.auth.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
	fun toUserResponse(user: User): UserResponse {
		return UserResponse(
			userId = user.id,
			name = user.name,
			mobileNumber = user.mobileNumber,
			email = user.email,
			role = user.role.name
		)
	}
}

