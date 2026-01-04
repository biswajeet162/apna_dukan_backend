package com.apnadukan.auth.dto.response

import java.time.LocalDateTime

data class ErrorResponse(
    val success: Boolean = false,
    val message: String,
    val errorCode: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
