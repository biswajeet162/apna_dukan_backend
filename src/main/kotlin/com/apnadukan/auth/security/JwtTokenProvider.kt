package com.apnadukan.auth.security

import com.apnadukan.auth.model.User
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
	@Value("\${jwt.secret:apnadukan-secret-key-change-in-production-minimum-256-bits}")
	private val jwtSecret: String,
	
	@Value("\${jwt.expiration:86400000}") // 24 hours in milliseconds
	private val jwtExpiration: Long
) {
	private val key: Key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

	fun generateAccessToken(user: User): String {
		val now = Date()
		val expiryDate = Date(now.time + jwtExpiration)

		return Jwts.builder()
			.subject(user.mobileNumber)
			.claim("userId", user.id)
			.claim("role", user.role.name)
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(key as javax.crypto.SecretKey)
			.compact()
	}

	fun validateToken(token: String): Boolean {
		return try {
			Jwts.parser()
				.verifyWith(key as javax.crypto.SecretKey)
				.build()
				.parseSignedClaims(token)
			true
		} catch (e: JwtException) {
			false
		} catch (e: IllegalArgumentException) {
			false
		}
	}

	fun getUserIdFromToken(token: String): Long {
		val claims = Jwts.parser()
			.verifyWith(key as javax.crypto.SecretKey)
			.build()
			.parseSignedClaims(token)
			.payload

		return claims["userId"].toString().toLong()
	}

	fun getMobileNumberFromToken(token: String): String {
		val claims = Jwts.parser()
			.verifyWith(key as javax.crypto.SecretKey)
			.build()
			.parseSignedClaims(token)
			.payload

		return claims.subject
	}

	fun getAccessTokenExpirySeconds(): Int {
		return (jwtExpiration / 1000).toInt()
	}

	fun getClaimsFromToken(token: String): Claims {
		return Jwts.parser()
			.verifyWith(key as javax.crypto.SecretKey)
			.build()
			.parseSignedClaims(token)
			.payload
	}
}

