package ch.srrc.events.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

/**
 * WebClient configuration for HTTP calls
 */
@Configuration
class WebClientConfig {
    
    @Bean
    fun webClient(): WebClient {
        // Configure HttpClient to follow redirects
        val httpClient = HttpClient.create()
            .followRedirect(true)
        
        // Increase buffer size for large JSON responses
        val strategies = ExchangeStrategies.builder()
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // 10MB
            }
            .build()
        
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(strategies)
            .defaultHeader("User-Agent", "SRRC-Calendar-API/1.0")
            .build()
    }
}
