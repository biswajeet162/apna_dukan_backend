package com.apnadukan.auth.repository

import com.apnadukan.auth.model.Otp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface OtpRepository : JpaRepository<Otp, UUID> {
	fun findByOtpRefId(otpRefId: String): Optional<Otp>
	
	@Modifying
	@Query("UPDATE Otp o SET o.isUsed = true WHERE o.otpRefId = :otpRefId")
	fun markAsUsed(@Param("otpRefId") otpRefId: String)
	
	@Modifying
	@Query("UPDATE Otp o SET o.attempts = o.attempts + 1 WHERE o.otpRefId = :otpRefId")
	fun incrementAttempts(@Param("otpRefId") otpRefId: String)
	
	@Query("SELECT o FROM Otp o WHERE o.mobileNumber = :mobileNumber AND o.isUsed = false AND o.expiresAt > :now ORDER BY o.createdAt DESC")
	fun findActiveOtpsByMobileNumber(
		@Param("mobileNumber") mobileNumber: String,
		@Param("now") now: LocalDateTime
	): List<Otp>
}

