package com.apnadukan.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordService(
	private val passwordEncoder: PasswordEncoder
) {
	fun encodePassword(rawPassword: String): String {
		return passwordEncoder.encode(rawPassword) ?: ""
	}

	fun matches(rawPassword: String, encodedPassword: String): Boolean {
		return passwordEncoder.matches(rawPassword, encodedPassword)
	}
}

