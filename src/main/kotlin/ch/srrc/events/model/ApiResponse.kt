package ch.srrc.events.model

import java.time.Instant

/**
 * Generic API response wrapper
 */
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null,
    val timestamp: String = Instant.now().toString()
) {
    companion object {
        fun <T> success(data: T, message: String = "Success"): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                message = message
            )
        }
        
        fun <T> error(error: String): ApiResponse<T> {
            return ApiResponse(
                success = false,
                error = error
            )
        }
    }
}
