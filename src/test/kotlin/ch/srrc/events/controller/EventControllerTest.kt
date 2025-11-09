package ch.srrc.events.controller

import ch.srrc.events.model.Event
import ch.srrc.events.service.EventService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(EventController::class)
class EventControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var eventService: EventService

    @Test
    fun `should return all events`() {
        // Given
        val mockEvents = listOf(
            Event(
                id = "test-1",
                title = "Test Event",
                dateDisplay = "07 nov. (jeu.)",
                startDate = "2024-11-07T10:00:00",
                endDate = "2024-11-07T18:00:00",
                location = "Test City",
                organizer = "Test Org",
                description = "Test description",
                url = "https://test.com",
                isUpcoming = true
            )
        )
        `when`(eventService.getAllEvents()).thenReturn(mockEvents)

        // When & Then
        mockMvc.perform(get("/api/v1/events"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data[0].id").value("test-1"))
            .andExpect(jsonPath("$.data[0].title").value("Test Event"))
    }

    @Test
    fun `should return upcoming events only`() {
        // Given
        val mockEvents = listOf(
            Event(
                id = "test-1",
                title = "Upcoming Event",
                dateDisplay = "07 nov. (jeu.)",
                startDate = "2024-11-07T10:00:00",
                endDate = "2024-11-07T18:00:00",
                location = "Test City",
                organizer = "Test Org",
                description = "Test description",
                url = "https://test.com",
                isUpcoming = true
            )
        )
        `when`(eventService.getUpcomingEvents()).thenReturn(mockEvents)

        // When & Then
        mockMvc.perform(get("/api/v1/events/upcoming"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data[0].isUpcoming").value(true))
    }

    @Test
    fun `should return health status`() {
        // Given
        val cacheInfo = mapOf(
            "totalEvents" to 10,
            "upcomingEvents" to 5,
            "lastRefresh" to "2024-11-07T19:00:00Z",
            "cacheDurationHours" to 1
        )
        `when`(eventService.getCacheInfo()).thenReturn(cacheInfo)

        // When & Then
        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.status").value("UP"))
            .andExpect(jsonPath("$.data.service").value("SRRC Calendar API"))
            .andExpect(jsonPath("$.data.cache.totalEvents").value(10))
    }
}
