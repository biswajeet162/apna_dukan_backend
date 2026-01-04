package com.apnadukan.auth.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
	private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val authHeader = request.getHeader("Authorization")

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			val token = authHeader.substring(7)

			if (jwtTokenProvider.validateToken(token)) {
				val mobileNumber = jwtTokenProvider.getMobileNumberFromToken(token)
				
				// Extract role from token
				val claims = jwtTokenProvider.getClaimsFromToken(token)
				val role = claims["role"] as? String ?: "USER"
				val authorities = listOf(org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_$role"))

				val authentication = UsernamePasswordAuthenticationToken(
					mobileNumber,
					null,
					authorities
				)
				authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

				SecurityContextHolder.getContext().authentication = authentication
			}
		}

		filterChain.doFilter(request, response)
	}
}

