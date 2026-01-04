package com.apnadukan.auth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
	private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.cors { }
			.csrf { it.disable() }
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.authorizeHttpRequests { authz ->
				authz
					.requestMatchers(
						"/api/v1/auth/login",
						"/api/v1/auth/signup",
						"/api/v1/auth/verify-otp",
						"/api/v1/auth/refresh-token",
						"/api/v1/products/**",
						"/api/v1/categories/**",
						"/h2-console/**",
						"/v3/api-docs/**",
						"/swagger-ui/**",
						"/swagger-ui.html",
						"/actuator/**"
					).permitAll()
					.anyRequest().authenticated()
			}
			.headers { headers ->
				headers.frameOptions { it.sameOrigin() } // Allow H2 console frames
			}
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

		return http.build()
	}

	@Bean
	fun corsConfigurationSource(): CorsConfigurationSource {
		val configuration = CorsConfiguration()
		configuration.allowedOrigins = listOf("*") // For development, allow all. In production, restrict this.
		configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
		configuration.allowedHeaders = listOf("Authorization", "Content-Type", "X-Requested-With", "Accept")
		configuration.allowCredentials = false // Must be false if allowedOrigins is ["*"]
		
		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", configuration)
		return source
	}
}

