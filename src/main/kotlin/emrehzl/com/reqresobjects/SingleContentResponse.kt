package emrehzl.com.reqresobjects

import emrehzl.com.models.ContentStatus
import emrehzl.com.models.License

data class SingleContentResponse(
    val id: String,
    val name: String,
    val year: Int,
    val summary: String,
    val genre: String,
    val status: ContentStatus,
    val posterUrl: String?,
    val videoUrl: String?,
    val createdAt: String,
    val licenses: List<License>
)
