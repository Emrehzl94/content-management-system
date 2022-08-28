package emrehzl.com.reqresobjects

data class ContentUpdateParams(
    val id: String,
    val name: String?,
    val year: Int?,
    val summary: String?,
    val genre: String?,
    val posterUrl: String?,
    val videoUrl: String?
)
