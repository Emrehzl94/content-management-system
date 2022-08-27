package emrehzl.com.models

data class Content(
    val id: String,
    val name: String,
    val year: Int,
    val summary: String,
    val genre: String,
    val status: ContentStatus,
    val posterUrl: String?,
    val videoUrl: String?,
    val createdAt: String
)
