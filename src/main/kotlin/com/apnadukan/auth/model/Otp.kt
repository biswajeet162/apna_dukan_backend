package com.apnadukan.auth.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "otps")
data class Otp(
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	val id: UUID = UUID.randomUUID(),

	@Column(nullable = false, unique = true)
	val otpRefId: String = UUID.randomUUID().toString(),

	@Column(nullable = false, length = 15)
	val mobileNumber: String = "",

	@Column(nullable = false, length = 6)
	val code: String = "",

	@Column(nullable = false)
	val purpose: OtpPurpose = OtpPurpose.LOGIN,

	@Column(nullable = false)
	val expiresAt: LocalDateTime = LocalDateTime.now(),

	@Column(nullable = false)
	val isUsed: Boolean = false,

	@Column(nullable = false)
	val attempts: Int = 0,

	@Column(nullable = false, updatable = false)
	val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class OtpPurpose {
	LOGIN,
	SIGNUP,
	PASSWORD_RESET,
	MOBILE_VERIFICATION
}

