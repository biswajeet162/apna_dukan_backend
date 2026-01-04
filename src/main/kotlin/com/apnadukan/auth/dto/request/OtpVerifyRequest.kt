package com.apnadukan.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class OtpVerifyRequest(
	@field:NotBlank(message = "OTP reference ID is required")
	val otpRefId: String,

	@field:NotBlank(message = "OTP is required")
	@field:Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits")
	val otp: String
)

