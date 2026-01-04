package com.apnadukan.auth.controller

import com.apnadukan.auth.dto.request.*
import com.apnadukan.auth.dto.response.ApiResponse
import com.apnadukan.auth.dto.response.AuthResponse
import com.apnadukan.auth.dto.response.TokenResponse
import com.apnadukan.auth.dto.response.UserResponse
import com.apnadukan.auth.security.JwtTokenProvider
import com.apnadukan.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
	private val authService: AuthService,
	private val jwtTokenProvider: JwtTokenProvider
) {
	@PostMapping("/login")
	fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<TokenResponse>> {
		val response = authService.login(request)
		return ResponseEntity.ok(ApiResponse(success = true, message = "Login successful", data = response))
	}

	@PostMapping("/signup")
	fun signup(@Valid @RequestBody request: SignupRequest): ResponseEntity<ApiResponse<TokenResponse>> {
		val response = authService.signup(request)
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse(success = true, message = "Signup successful", data = response))
	}

	@PostMapping("/verify-otp")
	fun verifyOtp(@Valid @RequestBody request: OtpVerifyRequest): ResponseEntity<ApiResponse<TokenResponse>> {
		val response = authService.verifyOtp(request)
		return ResponseEntity.ok(ApiResponse(success = true, message = "OTP verified successfully", data = response))
	}

	@PostMapping("/refresh-token")
	fun refreshToken(@Valid @RequestBody request: Map<String, String>): ResponseEntity<ApiResponse<TokenResponse>> {
		val refreshToken = request["refreshToken"]
			?: throw IllegalArgumentException("Refresh token is required")
		
		val response = authService.refreshToken(refreshToken)
		return ResponseEntity.ok(ApiResponse(success = true, message = "Token refreshed successfully", data = response))
	}

	@PostMapping("/forgot-password")
	fun forgotPassword(@Valid @RequestBody request: ForgotPasswordRequest): ResponseEntity<ApiResponse<AuthResponse>> {
		val response = authService.forgotPassword(request)
		return ResponseEntity.ok(ApiResponse(success = true, message = "OTP sent successfully", data = response))
	}

	@PostMapping("/reset-password")
	fun resetPassword(@Valid @RequestBody request: ResetPasswordRequest): ResponseEntity<ApiResponse<Unit>> {
		authService.resetPassword(request)
		return ResponseEntity.ok(ApiResponse(success = true, message = "Password reset successfully"))
	}

	@PostMapping("/logout")
	fun logout(): ResponseEntity<ApiResponse<Unit>> {
		val userId = getUserIdFromRequest()
		authService.logout(userId)
		return ResponseEntity.ok(ApiResponse(success = true, message = "Logged out successfully"))
	}

	@GetMapping("/me")
	fun getMe(): ResponseEntity<ApiResponse<UserResponse>> {
		val userId = getUserIdFromRequest()
		val user = authService.getCurrentUser(userId)
		return ResponseEntity.ok(ApiResponse(success = true, message = "Profile fetched successfully", data = user))
	}

	private fun getUserIdFromRequest(): Long {
		return try {
			val request = org.springframework.web.context.request.RequestContextHolder
				.currentRequestAttributes() as org.springframework.web.context.request.ServletRequestAttributes
			val authHeader = request.request.getHeader("Authorization")
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				val token = authHeader.substring(7)
				jwtTokenProvider.getUserIdFromToken(token)
			} else {
				throw IllegalStateException("Authorization header not found")
			}
		} catch (e: Exception) {
			throw IllegalStateException("Unable to extract user ID: ${e.message}")
		}
	}
}

