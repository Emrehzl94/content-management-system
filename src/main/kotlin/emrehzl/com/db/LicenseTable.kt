package emrehzl.com.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object LicenseTable: Table("licenses") {
    val id = varchar("id", 32)
    val name = varchar("name", 256)
    val startTime = date("start_time")
    val endTime = date("end_time")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
}