package ch.srrc.events.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

/**
 * WebClient configuration for HTTP calls
 */
@Configuration
class WebClientConfig {
    
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .codecs { configurer ->
                // Increase buffer size for large JSON responses
                configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // 10MB
            }
            .defaultHeader("User-Agent", "SRRC-Calendar-API/1.0")
            .build()
    }
}
