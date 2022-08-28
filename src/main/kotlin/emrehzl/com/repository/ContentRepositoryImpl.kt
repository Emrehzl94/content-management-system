package emrehzl.com.repository

import emrehzl.com.db.ContentTable
import emrehzl.com.db.DatabaseFactory.dbQuery
import emrehzl.com.models.Content
import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.reqresobjects.ContentUpdateParams
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
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

    override suspend fun update(params: ContentUpdateParams): Content? {
        val content = dbQuery {
            ContentTable.update({ ContentTable.id.eq(params.id) }) {
                if (params.name != null) {
                    it[name] = params.name
                }
                if (params.year != null) {
                    it[year] = params.year
                }
                if (params.summary != null) {
                    it[summary] = params.summary
                }
                if (params.genre != null) {
                    it[genre] = params.genre
                }
                if (params.posterUrl != null) {
                    it[posterUrl] = params.posterUrl
                }
                if (params.videoUrl != null) {
                    it[videoUrl] = params.videoUrl
                }
            }

            ContentTable.select { ContentTable.id.eq(params.id) }
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