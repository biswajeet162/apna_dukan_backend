package com.apnadukan.auth.event

import org.springframework.context.ApplicationEvent

class PasswordResetEvent(
	source: Any,
	val userId: Long,
	val mobileNumber: String
) : ApplicationEvent(source)

