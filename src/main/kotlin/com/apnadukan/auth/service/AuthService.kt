package com.apnadukan.auth.service

import com.apnadukan.auth.dto.request.*
import com.apnadukan.auth.exception.*
import com.apnadukan.auth.dto.response.TokenResponse
import com.apnadukan.auth.dto.response.UserResponse
import com.apnadukan.auth.mapper.UserMapper
import com.apnadukan.auth.model.OtpPurpose
import com.apnadukan.auth.model.Role
import com.apnadukan.auth.model.User
import com.apnadukan.auth.repository.RefreshTokenRepository
import com.apnadukan.auth.repository.UserRepository
import com.apnadukan.auth.security.JwtTokenProvider
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
	private val userRepository: UserRepository,
	private val otpService: OtpService,
	private val passwordService: PasswordService,
	private val jwtTokenProvider: JwtTokenProvider,
	private val refreshTokenRepository: RefreshTokenRepository,
	private val userMapper: UserMapper,
	private val eventPublisher: ApplicationEventPublisher
) : UserDetailsService {

	override fun loadUserByUsername(username: String): UserDetails {
		val user = userRepository.findByMobileNumber(username)
			.orElseThrow { UsernameNotFoundException("User not found with mobile number: $username") }
		
		return org.springframework.security.core.userdetails.User(
			user.mobileNumber,
			user.password ?: "",
			user.isActive,
			true,
			true,
			true,
			user.role.name.let { listOf(org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_$it")) }
		)
	}

	fun login(request: LoginRequest): TokenResponse {
		val mobile = request.mobileNumber
		val email = request.email

		val user = if (!mobile.isNullOrEmpty()) {
			userRepository.findByMobileNumber(mobile)
				.orElseThrow { ResourceNotFoundException("User not found") }
		} else if (!email.isNullOrEmpty()) {
			userRepository.findByEmail(email)
				.orElseThrow { ResourceNotFoundException("User not found") }
		} else {
			throw AppException("Mobile number or Email is required", org.springframework.http.HttpStatus.BAD_REQUEST, "MISSING_CREDENTIALS")
		}

		if (!passwordService.matches(request.password, user.password ?: "")) {
			throw InvalidPasswordException()
		}

		if (!user.isActive) {
			throw AccountDisabledException()
		}

		// Generate tokens directly for password login
		val accessToken = jwtTokenProvider.generateAccessToken(user)
		val refreshToken = createRefreshToken(user)

		return TokenResponse(
			userId = user.id,
			isNewUser = false,
			accessToken = accessToken,
			refreshToken = refreshToken.token,
			expiresIn = jwtTokenProvider.getAccessTokenExpirySeconds()
		)
	}

	fun signup(signupRequest: SignupRequest): TokenResponse {
		// Check if user already exists
		if (userRepository.existsByMobileNumber(signupRequest.mobileNumber)) {
			// If user exists and verified, throw error
			val existingUser = userRepository.findByMobileNumber(signupRequest.mobileNumber).get()
			if (existingUser.isMobileVerified) {
				throw UserAlreadyExistsException("User with this mobile number already exists")
			}
			// If not verified, update and activate
			existingUser.name = "${signupRequest.firstName} ${signupRequest.lastName ?: ""}".trim()
			existingUser.email = signupRequest.email
			existingUser.password = passwordService.encodePassword(signupRequest.password)
			existingUser.isMobileVerified = true // Direct verification
			userRepository.save(existingUser)
			
			// Generate tokens
			val accessToken = jwtTokenProvider.generateAccessToken(existingUser)
			val refreshToken = createRefreshToken(existingUser)

			return TokenResponse(
				userId = existingUser.id,
				isNewUser = true, // Technically updating, but new for the user
				accessToken = accessToken,
				refreshToken = refreshToken.token,
				expiresIn = jwtTokenProvider.getAccessTokenExpirySeconds()
			)
		}

		signupRequest.email?.let {
			if (userRepository.existsByEmail(it)) {
				throw UserAlreadyExistsException("Email is already registered")
			}
		}

		// Create new verified user
		val newUser = User(
			mobileNumber = signupRequest.mobileNumber,
			name = "${signupRequest.firstName} ${signupRequest.lastName ?: ""}".trim(),
			email = signupRequest.email,
			password = passwordService.encodePassword(signupRequest.password),
			role = Role.USER,
			isMobileVerified = true, // Direct verification
			isActive = true
		)
		val savedUser = userRepository.save(newUser)

		// Publish user registered event
		eventPublisher.publishEvent(
			com.apnadukan.auth.event.UserRegisteredEvent(
				source = this,
				userId = savedUser.id,
				mobileNumber = savedUser.mobileNumber
			)
		)

		// Generate tokens
		val accessToken = jwtTokenProvider.generateAccessToken(savedUser)
		val refreshToken = createRefreshToken(savedUser)

		return TokenResponse(
			userId = savedUser.id,
			isNewUser = true,
			accessToken = accessToken,
			refreshToken = refreshToken.token,
			expiresIn = jwtTokenProvider.getAccessTokenExpirySeconds()
		)
	}

	@Transactional
	fun verifyOtp(otpVerifyRequest: OtpVerifyRequest): TokenResponse {
		val isValid = otpService.verifyOtp(otpVerifyRequest.otpRefId, otpVerifyRequest.otp)
		
		if (!isValid) {
			throw AppException("Invalid OTP", org.springframework.http.HttpStatus.BAD_REQUEST, "INVALID_OTP")
		}

		// Get OTP to find mobile number and purpose
		val otp = otpService.getOtpByRefId(otpVerifyRequest.otpRefId)

		val user = userRepository.findByMobileNumber(otp.mobileNumber)
			.orElseThrow { ResourceNotFoundException("User not found") }

		var isNewUser = false

		when (otp.purpose) {
			OtpPurpose.SIGNUP -> {
				user.isMobileVerified = true // Mark as verified
				userRepository.save(user)
				
				isNewUser = true
				
				// Publish user registered event
				eventPublisher.publishEvent(
					com.apnadukan.auth.event.UserRegisteredEvent(
						source = this,
						userId = user.id,
						mobileNumber = user.mobileNumber
					)
				)
			}
			OtpPurpose.LOGIN -> {
				// Login via OTP flow (if we still support it, though Password flow is primary now)
			}
			OtpPurpose.PASSWORD_RESET -> {
				// Just verification, token can be used to reset password
			}
			else -> {}
		}

		// Generate tokens
		val accessToken = jwtTokenProvider.generateAccessToken(user)
		val refreshToken = createRefreshToken(user)

		return TokenResponse(
			userId = user.id,
			isNewUser = isNewUser,
			accessToken = accessToken,
			refreshToken = refreshToken.token,
			expiresIn = jwtTokenProvider.getAccessTokenExpirySeconds()
		)
	}

	fun forgotPassword(request: ForgotPasswordRequest): com.apnadukan.auth.dto.response.AuthResponse {
		val mobile = request.mobileNumber
		val email = request.email

		val user = if (!mobile.isNullOrEmpty()) {
			userRepository.findByMobileNumber(mobile)
				.orElseThrow { ResourceNotFoundException("User not found") }
		} else if (!email.isNullOrEmpty()) {
			userRepository.findByEmail(email)
				.orElseThrow { ResourceNotFoundException("User not found") }
		} else {
			throw AppException("Mobile number or Email is required", org.springframework.http.HttpStatus.BAD_REQUEST, "MISSING_CREDENTIALS")
		}

		// Generate OTP for the user's mobile (assuming OTP is always sent to mobile for now)
		val otp = otpService.generateOtp(user.mobileNumber, OtpPurpose.PASSWORD_RESET)

		return com.apnadukan.auth.dto.response.AuthResponse(
			otpRefId = otp.otpRefId,
			expiresInSeconds = otpService.getOtpExpirySeconds()
		)
	}

	fun resetPassword(request: ResetPasswordRequest) {
		// First verify OTP (or assume the frontend verified it and passed a token? 
		// But ResetPasswordRequest has otpRefId and otp. So we verify again here.)
		val isValid = otpService.verifyOtp(request.otpRefId, request.otp)
		if (!isValid) {
			throw AppException("Invalid OTP", org.springframework.http.HttpStatus.BAD_REQUEST, "INVALID_OTP")
		}

		val otp = otpService.getOtpByRefId(request.otpRefId)
		if (otp.purpose != OtpPurpose.PASSWORD_RESET) {
			throw AppException("Invalid OTP purpose", org.springframework.http.HttpStatus.BAD_REQUEST, "INVALID_OTP_PURPOSE")
		}

		val user = userRepository.findByMobileNumber(otp.mobileNumber)
			.orElseThrow { ResourceNotFoundException("User not found") }

		user.password = passwordService.encodePassword(request.newPassword)
		userRepository.save(user)
	}

	@Transactional
	fun refreshToken(refreshToken: String): TokenResponse {
		val token = refreshTokenRepository.findByToken(refreshToken)
			.orElseThrow { UnauthorizedException("Invalid refresh token") }

		if (token.isRevoked) {
			throw UnauthorizedException("Refresh token has been revoked")
		}

		if (token.expiresAt.isBefore(LocalDateTime.now())) {
			throw UnauthorizedException("Refresh token has expired")
		}

		val user = token.user
		val newAccessToken = jwtTokenProvider.generateAccessToken(user)

		return TokenResponse(
			userId = user.id,
			isNewUser = false,
			accessToken = newAccessToken,
			refreshToken = refreshToken,
			expiresIn = jwtTokenProvider.getAccessTokenExpirySeconds()
		)
	}

	@Transactional
	fun logout(userId: Long) {
		val user = userRepository.findById(userId)
			.orElseThrow { ResourceNotFoundException("User not found") }
		
		refreshTokenRepository.revokeAllByUser(user)
	}

	fun getCurrentUser(userId: Long): UserResponse {
		val user = userRepository.findById(userId)
			.orElseThrow { ResourceNotFoundException("User not found") }
		
		return userMapper.toUserResponse(user)
	}

	private fun createRefreshToken(user: User): com.apnadukan.auth.model.RefreshToken {
		val refreshToken = com.apnadukan.auth.model.RefreshToken(
			user = user,
			expiresAt = LocalDateTime.now().plusDays(30)
		)
		return refreshTokenRepository.save(refreshToken)
	}
}

