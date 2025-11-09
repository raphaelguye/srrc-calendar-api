package ch.srrc.events.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Event data class representing an SRRC event
 */
data class Event(
    val id: String,
    val title: String,
    val dateDisplay: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val organizer: String,
    val description: String,
    val url: String,
    val isUpcoming: Boolean
) {
    companion object {
        /**
         * Transform raw event data from scraper into clean Event object
         */
        fun fromRawEvent(raw: RawEvent): Event {
            val id = raw.eventId.ifEmpty { 
                generateId(raw.title, raw.startDate) 
            }
            
            val dateDisplay = buildDateDisplay(
                raw.dateDisplay, 
                raw.month, 
                raw.weekday
            )
            
            val isUpcoming = isEventUpcoming(raw.startDate)
            
            return Event(
                id = id,
                title = raw.title,
                dateDisplay = dateDisplay,
                startDate = raw.startDate,
                endDate = raw.endDate,
                location = raw.location,
                organizer = raw.organizer,
                description = raw.description,
                url = raw.url,
                isUpcoming = isUpcoming
            )
        }
        
        private fun generateId(title: String, startDate: String): String {
            val sanitizedTitle = title.replace(Regex("[^a-zA-Z0-9]"), "-").lowercase()
            val datePrefix = startDate.take(10).replace("-", "")
            return "$datePrefix-$sanitizedTitle"
        }
        
        private fun buildDateDisplay(day: String, month: String, weekday: String): String {
            return "$day $month ($weekday)"
        }
        
        private fun isEventUpcoming(startDate: String): Boolean {
            return try {
                val eventDate = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME)
                eventDate.isAfter(LocalDateTime.now())
            } catch (e: Exception) {
                false
            }
        }
    }
}

/**
 * Raw event data structure from the scraper JSON
 */
data class RawEvent(
    @JsonProperty("date_display") val dateDisplay: String,
    @JsonProperty("month") val month: String,
    @JsonProperty("weekday") val weekday: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("url") val url: String,
    @JsonProperty("event_id") val eventId: String,
    @JsonProperty("location") val location: String,
    @JsonProperty("start_date") val startDate: String,
    @JsonProperty("end_date") val endDate: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("organizer") val organizer: String
)
