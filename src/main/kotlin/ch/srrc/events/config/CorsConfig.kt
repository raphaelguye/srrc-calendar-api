package ch.srrc.events.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * CORS configuration to allow web application integration
 */
@Configuration
class CorsConfig {
    
    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        
        // Allow all origins for POC (restrict in production)
        config.allowedOriginPatterns = listOf("*")
        
        // Allow common HTTP methods
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
        
        // Allow common headers
        config.allowedHeaders = listOf("*")
        
        // Allow credentials
        config.allowCredentials = true
        
        // Cache preflight response for 1 hour
        config.maxAge = 3600L
        
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        
        return CorsFilter(source)
    }
}
