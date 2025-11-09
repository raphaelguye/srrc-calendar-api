package ch.srrc.events.service

import ch.srrc.events.model.Event
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.annotation.PostConstruct
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Service for managing events with in-memory caching and scheduled refresh
 */
@Service
class EventService(
    private val gitHubService: GitHubService
) {
    private val logger = LoggerFactory.getLogger(EventService::class.java)
    
    // In-memory cache with thread-safe read/write lock
    private var cachedEvents: List<Event> = emptyList()
    private var lastRefreshTime: Instant? = null
    private val lock = ReentrantReadWriteLock()
    
    companion object {
        private const val CACHE_DURATION_HOURS = 1L
    }
    
    /**
     * Initialize cache on application startup
     */
    @PostConstruct
    fun initialize() {
        logger.info("Initializing EventService and loading initial data...")
        refreshEvents()
    }
    
    /**
     * Get all events from cache
     */
    fun getAllEvents(): List<Event> = lock.read {
        logger.debug("Retrieving all events from cache (${cachedEvents.size} events)")
        cachedEvents
    }
    
    /**
     * Get only upcoming events
     */
    fun getUpcomingEvents(): List<Event> = lock.read {
        val upcoming = cachedEvents.filter { it.isUpcoming }
        logger.debug("Retrieving upcoming events from cache (${upcoming.size} events)")
        upcoming
    }
    
    /**
     * Get cache metadata
     */
    fun getCacheInfo(): Map<String, Any> = lock.read {
        mapOf(
            "totalEvents" to cachedEvents.size,
            "upcomingEvents" to cachedEvents.count { it.isUpcoming },
            "lastRefresh" to (lastRefreshTime?.toString() ?: "never"),
            "cacheDurationHours" to CACHE_DURATION_HOURS
        )
    }
    
    /**
     * Refresh events from GitHub API
     * Scheduled to run every hour (3600000 ms)
     */
    @Scheduled(fixedRate = 3600000, initialDelay = 3600000)
    fun refreshEvents() {
        logger.info("Refreshing events from GitHub API...")
        
        try {
            // Fetch raw events from GitHub
            val rawEvents = gitHubService.fetchEvents()
            
            // Transform to Event objects
            val events = rawEvents.map { Event.fromRawEvent(it) }
            
            // Update cache
            lock.write {
                cachedEvents = events
                lastRefreshTime = Instant.now()
            }
            
            val upcomingCount = events.count { it.isUpcoming }
            logger.info("Successfully refreshed ${events.size} events ($upcomingCount upcoming)")
            
        } catch (e: Exception) {
            logger.error("Failed to refresh events: ${e.message}", e)
            // Keep existing cache on failure
        }
    }
    
    /**
     * Manual refresh endpoint (can be triggered via admin API if needed)
     */
    fun forceRefresh() {
        logger.info("Manual refresh triggered")
        refreshEvents()
    }
}
