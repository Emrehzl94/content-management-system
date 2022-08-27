package emrehzl.com.repository

import emrehzl.com.db.ContentTable
import emrehzl.com.db.DatabaseFactory.dbQuery
import emrehzl.com.models.Content
import emrehzl.com.reqresobjects.ContentCreateParams
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.*

class ContentRepositoryImpl : ContentRepository {
    override suspend fun create(params: ContentCreateParams): Content? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = ContentTable.insert {
                it[id] = UUID.randomUUID()
                    .toString().replace("-", "")
                it[name] = params.name
                it[year] = params.year
                it[summary] = params.summary
                it[genre] = params.genre
                it[posterUrl] = params.posterUrl
                it[videoUrl] = params.videoUrl
            }
        }
        return rowToContent(statement?.resultedValues?.get(0))
    }

    override suspend fun list(): List<Content?> {
        val contents = dbQuery {
            ContentTable.selectAll().map { rowToContent(it) }.toList()
        }
        return contents
    }

    override suspend fun getById(id: String): Content? {
        val content = dbQuery {
            ContentTable.select { ContentTable.id.eq(id) }
                .map { rowToContent(it) }.singleOrNull()
        }
        return content
    }

    private fun rowToContent(row: ResultRow?): Content? {
        return if (row == null) null
        else Content(
            id = row[ContentTable.id],
            name = row[ContentTable.name],
            year = row[ContentTable.year],
            summary = row[ContentTable.summary],
            genre = row[ContentTable.genre],
            status = row[ContentTable.status],
            posterUrl = row[ContentTable.posterUrl],
            videoUrl = row[ContentTable.videoUrl],
            createdAt = row[ContentTable.createdAt].toString()
        )
    }
}