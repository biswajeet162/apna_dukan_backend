package com.apnadukan.auth.event

import com.apnadukan.auth.model.OtpPurpose
import org.springframework.context.ApplicationEvent

class OtpGeneratedEvent(
	source: Any,
	val mobileNumber: String,
	val otpCode: String,
	val otpRefId: String,
	val purpose: OtpPurpose
) : ApplicationEvent(source)

