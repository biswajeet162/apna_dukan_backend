package com.apnadukan.auth.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,

	@Column(nullable = false, unique = true, length = 15)
	val mobileNumber: String = "",

	@Column(nullable = false)
	var name: String = "",

	@Column(unique = true)
	var email: String? = null,

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	val role: Role = Role.USER,

	@Column(nullable = false)
	var password: String? = null,

	@Column(nullable = false)
	val isActive: Boolean = true,

	@Column(nullable = false)
	val isEmailVerified: Boolean = false,

	@Column(nullable = false)
	var isMobileVerified: Boolean = false,

	@Column(nullable = false, updatable = false)
	val createdAt: LocalDateTime = LocalDateTime.now(),

	@Column(nullable = false)
	var updatedAt: LocalDateTime = LocalDateTime.now()
)

