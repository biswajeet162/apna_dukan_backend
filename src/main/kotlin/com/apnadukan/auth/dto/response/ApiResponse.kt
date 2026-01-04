package com.apnadukan.auth.dto.response

import java.time.LocalDateTime

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val errorCode: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
