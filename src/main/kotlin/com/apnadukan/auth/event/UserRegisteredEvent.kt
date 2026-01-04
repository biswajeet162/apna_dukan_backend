package com.apnadukan.auth.event

import org.springframework.context.ApplicationEvent

class UserRegisteredEvent(
	source: Any,
	val userId: Long,
	val mobileNumber: String
) : ApplicationEvent(source)

