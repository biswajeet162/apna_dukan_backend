package com.apnadukan.auth.repository

import com.apnadukan.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
	fun findByMobileNumber(mobileNumber: String): Optional<User>
	fun existsByMobileNumber(mobileNumber: String): Boolean
	fun findByEmail(email: String): Optional<User>
	fun existsByEmail(email: String): Boolean
}

