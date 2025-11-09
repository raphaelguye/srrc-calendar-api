package ch.srrc.events.controller

import ch.srrc.events.model.ApiResponse
import ch.srrc.events.model.Event
import ch.srrc.events.service.EventService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for SRRC Events API
 */
@RestController
@RequestMapping("/api/v1")
class EventController(
    private val eventService: EventService
) {
    private val logger = LoggerFactory.getLogger(EventController::class.java)
    
    /**
     * GET /api/v1/events
     * Returns all events
     */
    @GetMapping("/events")
    fun getAllEvents(): ResponseEntity<ApiResponse<List<Event>>> {
        logger.info("GET /api/v1/events - Fetching all events")
        
        return try {
            val events = eventService.getAllEvents()
            ResponseEntity.ok(
                ApiResponse.success(
                    data = events,
                    message = "Successfully retrieved ${events.size} events"
                )
            )
        } catch (e: Exception) {
            logger.error("Error fetching all events: ${e.message}", e)
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch events: ${e.message}"))
        }
    }
    
    /**
     * GET /api/v1/events/upcoming
     * Returns only upcoming events
     */
    @GetMapping("/events/upcoming")
    fun getUpcomingEvents(): ResponseEntity<ApiResponse<List<Event>>> {
        logger.info("GET /api/v1/events/upcoming - Fetching upcoming events")
        
        return try {
            val events = eventService.getUpcomingEvents()
            ResponseEntity.ok(
                ApiResponse.success(
                    data = events,
                    message = "Successfully retrieved ${events.size} upcoming events"
                )
            )
        } catch (e: Exception) {
            logger.error("Error fetching upcoming events: ${e.message}", e)
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch upcoming events: ${e.message}"))
        }
    }
    
    /**
     * GET /api/v1/health
     * Health check endpoint
     */
    @GetMapping("/health")
    fun health(): ResponseEntity<ApiResponse<Map<String, Any>>> {
        logger.debug("GET /api/v1/health - Health check")
        
        return try {
            val cacheInfo = eventService.getCacheInfo()
            val healthData = mapOf(
                "status" to "UP",
                "service" to "SRRC Calendar API",
                "cache" to cacheInfo
            )
            
            ResponseEntity.ok(
                ApiResponse.success(
                    data = healthData,
                    message = "Service is healthy"
                )
            )
        } catch (e: Exception) {
            logger.error("Health check failed: ${e.message}", e)
            ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error("Service unhealthy: ${e.message}"))
        }
    }
    
    /**
     * POST /api/v1/events/refresh
     * Manual refresh trigger (for admin use)
     */
    @PostMapping("/events/refresh")
    fun refreshEvents(): ResponseEntity<ApiResponse<String>> {
        logger.info("POST /api/v1/events/refresh - Manual refresh triggered")
        
        return try {
            eventService.forceRefresh()
            ResponseEntity.ok(
                ApiResponse.success(
                    data = "Refresh initiated",
                    message = "Events refresh completed successfully"
                )
            )
        } catch (e: Exception) {
            logger.error("Error during manual refresh: ${e.message}", e)
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to refresh events: ${e.message}"))
        }
    }
}
