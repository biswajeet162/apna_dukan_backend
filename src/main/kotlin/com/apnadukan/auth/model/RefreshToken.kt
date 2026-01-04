package com.apnadukan.auth.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	val id: UUID = UUID.randomUUID(),

	@Column(nullable = false, unique = true)
	val token: String = UUID.randomUUID().toString(),

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	val user: User = User(),

	@Column(nullable = false)
	val expiresAt: LocalDateTime = LocalDateTime.now(),

	@Column(nullable = false)
	val isRevoked: Boolean = false,

	@Column(nullable = false, updatable = false)
	val createdAt: LocalDateTime = LocalDateTime.now()
)

