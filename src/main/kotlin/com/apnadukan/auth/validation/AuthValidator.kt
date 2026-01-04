package com.apnadukan.auth.validation

import org.springframework.stereotype.Component

@Component
class AuthValidator {
	fun isValidMobileNumber(mobileNumber: String): Boolean {
		return mobileNumber.matches(Regex("^[0-9]{10}$"))
	}

	fun isValidEmail(email: String): Boolean {
		return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))
	}

	fun isValidOtp(otp: String): Boolean {
		return otp.matches(Regex("^[0-9]{6}$"))
	}

	fun isValidPassword(password: String): Boolean {
		// At least 8 characters, contains at least one letter and one number
		return password.length >= 8 &&
				password.any { it.isLetter() } &&
				password.any { it.isDigit() }
	}
}

