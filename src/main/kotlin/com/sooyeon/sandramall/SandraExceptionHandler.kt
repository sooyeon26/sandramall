package com.sooyeon.sandramall

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@ControllerAdvice
@RestController
class SandraExceptionHandler{

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler
    fun handleSandraException(e: SandraException): ApiResponse{
        logger.error("API error", e)
        return ApiResponse.error(e.message)
    }

    @ExceptionHandler
    fun handleException(e: Exception): ApiResponse{
        logger.error("API error", e)
        return ApiResponse.error("unknown error")
    }
}