package ch.srrc.events.service

import ch.srrc.events.model.GitHubRelease
import ch.srrc.events.model.RawEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * Service for fetching events data from GitHub Releases API
 */
@Service
class GitHubService(
    private val webClient: WebClient,
    @Value("\${github.repository}") private val repository: String,
    @Value("\${github.asset-name}") private val assetName: String
) {
    private val logger = LoggerFactory.getLogger(GitHubService::class.java)
    
    companion object {
        private const val GITHUB_API_BASE = "https://api.github.com"
        private const val REQUEST_TIMEOUT_SECONDS = 30L
    }
    
    /**
     * Fetch events from GitHub Releases API
     * Returns list of raw events or throws exception on failure
     */
    fun fetchEvents(): List<RawEvent> {
        logger.info("Fetching events from GitHub repository: $repository")
        
        return try {
            // Step 1: Get latest release info
            val release = getLatestRelease()
            
            // Step 2: Find the events JSON asset
            val asset = release.assets.find { it.name == assetName }
                ?: throw IllegalStateException("Asset '$assetName' not found in release")
            
            logger.info("Found asset: ${asset.name} (${asset.size} bytes)")
            
            // Step 3: Download and parse events JSON
            val events = downloadEventsJson(asset.browserDownloadUrl)
            
            logger.info("Successfully fetched ${events.size} events from GitHub")
            events
            
        } catch (e: Exception) {
            logger.error("Failed to fetch events from GitHub: ${e.message}", e)
            throw RuntimeException("Failed to fetch events from GitHub: ${e.message}", e)
        }
    }
    
    /**
     * Get latest release from GitHub repository
     */
    private fun getLatestRelease(): GitHubRelease {
        val url = "$GITHUB_API_BASE/repos/$repository/releases/latest"
        
        logger.debug("Fetching latest release from: $url")
        
        return webClient.get()
            .uri(url)
            .header("Accept", "application/vnd.github+json")
            .retrieve()
            .bodyToMono<GitHubRelease>()
            .timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
            .doOnError { error ->
                logger.error("Failed to fetch release info: ${error.message}")
            }
            .block() ?: throw IllegalStateException("Failed to fetch release information")
    }
    
    /**
     * Download and parse events JSON from asset URL
     */
    private fun downloadEventsJson(downloadUrl: String): List<RawEvent> {
        logger.debug("Downloading events JSON from: $downloadUrl")
        
        return webClient.get()
            .uri(downloadUrl)
            .retrieve()
            .bodyToMono<List<RawEvent>>()
            .timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
            .doOnError { error ->
                logger.error("Failed to download events JSON: ${error.message}")
            }
            .block() ?: emptyList()
    }
}
