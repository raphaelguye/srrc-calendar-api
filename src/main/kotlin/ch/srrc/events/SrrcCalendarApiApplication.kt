package ch.srrc.events

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Main application class for SRRC Calendar API
 */
@SpringBootApplication
@EnableScheduling
class SrrcCalendarApiApplication

fun main(args: Array<String>) {
    runApplication<SrrcCalendarApiApplication>(*args)
}
