package ch.srrc.events.exception

import ch.srrc.events.model.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Global exception handler for consistent error responses
 */
@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    
    /**
     * Handle all uncaught exceptions
     */
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.error("Unhandled exception: ${ex.message}", ex)
        
        val response = ApiResponse.error<Nothing>(
            error = "An unexpected error occurred: ${ex.message}"
        )
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response)
    }
    
    /**
     * Handle illegal state exceptions
     */
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        ex: IllegalStateException,
        request: WebRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.error("Illegal state: ${ex.message}", ex)
        
        val response = ApiResponse.error<Nothing>(
            error = ex.message ?: "Invalid state"
        )
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }
    
    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.error("Illegal argument: ${ex.message}", ex)
        
        val response = ApiResponse.error<Nothing>(
            error = ex.message ?: "Invalid argument"
        )
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }
}
