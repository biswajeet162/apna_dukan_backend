package com.apnadukan.auth.exception

import org.springframework.http.HttpStatus

open class AppException(
    val msg: String,
    val status: HttpStatus,
    val errorCode: String
) : RuntimeException(msg)

class ResourceNotFoundException(message: String) : AppException(
    msg = message,
    status = HttpStatus.NOT_FOUND,
    errorCode = "RESOURCE_NOT_FOUND"
)

class UserAlreadyExistsException(message: String) : AppException(
    msg = message,
    status = HttpStatus.CONFLICT,
    errorCode = "USER_ALREADY_EXISTS"
)

class InvalidPasswordException(message: String = "Invalid password") : AppException(
    msg = message,
    status = HttpStatus.BAD_REQUEST,
    errorCode = "INVALID_PASSWORD"
)

class AccountDisabledException(message: String = "Account is disabled") : AppException(
    msg = message,
    status = HttpStatus.FORBIDDEN,
    errorCode = "ACCOUNT_DISABLED"
)

class UnauthorizedException(message: String) : AppException(
    msg = message,
    status = HttpStatus.UNAUTHORIZED,
    errorCode = "UNAUTHORIZED"
)
