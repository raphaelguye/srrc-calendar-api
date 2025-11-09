package ch.srrc.events.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * GitHub Release API response model
 */
data class GitHubRelease(
    val id: Long,
    val name: String,
    @JsonProperty("tag_name") val tagName: String,
    val assets: List<GitHubAsset>,
    @JsonProperty("published_at") val publishedAt: String
)

/**
 * GitHub Release Asset model
 */
data class GitHubAsset(
    val id: Long,
    val name: String,
    @JsonProperty("browser_download_url") val browserDownloadUrl: String,
    val size: Long,
    @JsonProperty("content_type") val contentType: String
)
