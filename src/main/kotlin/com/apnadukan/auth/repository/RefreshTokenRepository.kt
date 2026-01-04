package com.apnadukan.auth.repository

import com.apnadukan.auth.model.RefreshToken
import com.apnadukan.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
	fun findByToken(token: String): Optional<RefreshToken>
	
	fun findByUser(user: User): List<RefreshToken>
	
	@Modifying
	@Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.user = :user")
	fun revokeAllByUser(@Param("user") user: User)
	
	@Modifying
	@Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.token = :token")
	fun revokeByToken(@Param("token") token: String)
}

