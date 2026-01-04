package com.apnadukan.auth.service

import com.apnadukan.auth.model.Otp
import com.apnadukan.auth.model.OtpPurpose
import com.apnadukan.auth.repository.OtpRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class OtpService(
	private val otpRepository: OtpRepository,
	private val eventPublisher: ApplicationEventPublisher
) {
	companion object {
		private const val OTP_LENGTH = 6
		private const val OTP_EXPIRY_MINUTES = 5
		private const val MAX_ATTEMPTS = 3
	}

	fun generateOtp(mobileNumber: String, purpose: OtpPurpose): Otp {
		// Generate 6-digit OTP
		val otpCode = (100000..999999).random().toString()
		
		val otp = Otp(
			mobileNumber = mobileNumber,
			code = otpCode,
			purpose = purpose,
			expiresAt = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES.toLong())
		)

		val savedOtp = otpRepository.save(otp)
		
		// Publish event for OTP generation
		eventPublisher.publishEvent(
			com.apnadukan.auth.event.OtpGeneratedEvent(
				source = this,
				mobileNumber = mobileNumber,
				otpCode = otpCode,
				otpRefId = savedOtp.otpRefId,
				purpose = purpose
			)
		)

		return savedOtp
	}

	@Transactional
	fun verifyOtp(otpRefId: String, otpCode: String): Boolean {
		val otp = otpRepository.findByOtpRefId(otpRefId)
			.orElseThrow { IllegalArgumentException("Invalid OTP reference ID") }

		if (otp.isUsed) {
			throw IllegalStateException("OTP has already been used")
		}

		if (otp.expiresAt.isBefore(LocalDateTime.now())) {
			throw IllegalStateException("OTP has expired")
		}

		if (otp.attempts >= MAX_ATTEMPTS) {
			throw IllegalStateException("Maximum OTP verification attempts exceeded")
		}

		otpRepository.incrementAttempts(otpRefId)

		if (otp.code != otpCode) {
			return false
		}

		otpRepository.markAsUsed(otpRefId)
		return true
	}

	fun getOtpExpirySeconds(): Int {
		return OTP_EXPIRY_MINUTES * 60
	}

	fun getOtpByRefId(otpRefId: String): Otp {
		return otpRepository.findByOtpRefId(otpRefId)
			.orElseThrow { IllegalArgumentException("OTP not found") }
	}
}

