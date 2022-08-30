package emrehzl.com.db

import emrehzl.com.models.ContentStatus
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object ContentTable: UUIDTable("contents") {
    val name = varchar("name", 256)
    val year = integer("year")
    val summary = text("summary")
    val genre = varchar("genre", 256)
    val status = enumeration<ContentStatus>("status")
        .clientDefault { ContentStatus.InProgress }
    val posterUrl = text("poster_url").nullable()
    val videoUrl = text("video_url").nullable()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
}