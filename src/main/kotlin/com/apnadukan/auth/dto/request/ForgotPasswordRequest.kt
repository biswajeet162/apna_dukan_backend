package com.apnadukan.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Email

data class ForgotPasswordRequest(
	@field:Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
	val mobileNumber: String? = null,

	@field:Email(message = "Email must be valid")
	val email: String? = null
)

