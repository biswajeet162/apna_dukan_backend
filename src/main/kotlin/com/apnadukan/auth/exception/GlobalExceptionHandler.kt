package com.apnadukan.auth.exception

import com.apnadukan.auth.dto.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.bind.MethodArgumentNotValidException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AppException::class)
    fun handleAppException(ex: AppException, request: WebRequest): ResponseEntity<ApiResponse<Unit>> {
        val response = ApiResponse<Unit>(
            success = false,
            message = ex.msg,
            errorCode = ex.errorCode
        )
        return ResponseEntity(response, ex.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        val message = ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        val response = ApiResponse<Unit>(
            success = false,
            message = "Validation failed: $message",
            errorCode = "VALIDATION_FAILED"
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<ApiResponse<Unit>> {
        val response = ApiResponse<Unit>(
            success = false,
            message = ex.message ?: "An unexpected error occurred",
            errorCode = "INTERNAL_SERVER_ERROR"
        )
        return ResponseEntity.internalServerError().body(response)
    }
}
