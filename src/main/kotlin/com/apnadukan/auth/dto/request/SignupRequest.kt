package com.apnadukan.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class SignupRequest(
	@field:NotBlank(message = "Mobile number is required")
	@field:Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
	val mobileNumber: String,

	@field:NotBlank(message = "First name is required")
	val firstName: String,

	val lastName: String? = null,

	@field:Email(message = "Email must be valid")
	val email: String? = null,

	@field:NotBlank(message = "Password is required")
	@field:Size(min = 6, message = "Password must be at least 6 characters")
	val password: String
)

