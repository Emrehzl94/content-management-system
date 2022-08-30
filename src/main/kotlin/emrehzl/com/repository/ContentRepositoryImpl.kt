package emrehzl.com.repository

import emrehzl.com.db.ContentTable
import emrehzl.com.db.DatabaseFactory.dbQuery
import emrehzl.com.models.Content
import emrehzl.com.models.ContentStatus
import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.reqresobjects.ContentUpdateParams
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.*

class ContentRepositoryImpl : ContentRepository {
    override suspend fun create(params: ContentCreateParams): Content? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = ContentTable.insert {
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

    override suspend fun list(name: String?, status: ContentStatus?): List<Content> {
        val query = ContentTable.selectAll()
        val predicates = Op.build { ContentTable.name.like("")}

        if (!name.isNullOrEmpty()) predicates.and { ContentTable.name.lowerCase().like(name.lowercase()) }
        if (status != null) predicates.and { ContentTable.status.eq(status) }

        val contents = dbQuery {
//            ContentTable.select {
//                ContentTable.name.lowerCase().like(nameMut)
//            }
//                .map { rowToContent(it) }
//                .toList()
            query.andWhere { predicates }
                .map { rowToContent(it) }
                .toList()
        }
        return contents.filterNotNull()
    }

    override suspend fun getById(id: String): Content? {
        val content = dbQuery {
            ContentTable.select { ContentTable.id.eq(UUID.fromString(id)) }
                .map { rowToContent(it) }.singleOrNull()
        }
        return content
    }

    override suspend fun update(params: ContentUpdateParams): Content? {
        val content = dbQuery {
            ContentTable.update({ ContentTable.id.eq(UUID.fromString(params.id)) }) {
                if (params.name != null) it[name] = params.name
                if (params.year != null) it[year] = params.year
                if (params.summary != null) it[summary] = params.summary
                if (params.genre != null) it[genre] = params.genre
                if (params.posterUrl != null) it[posterUrl] = params.posterUrl
                if (params.videoUrl != null) it[videoUrl] = params.videoUrl
            }

            ContentTable.select { ContentTable.id.eq(UUID.fromString(params.id)) }
                .map { rowToContent(it) }.singleOrNull()
        }

        return content
    }

    override suspend fun delete(id: String) {
        dbQuery {
            ContentTable.deleteWhere { ContentTable.id.eq(UUID.fromString(id)) }
        }
    }

    private fun rowToContent(row: ResultRow?): Content? {
        return if (row == null) null
        else Content(
            id = row[ContentTable.id].toString(),
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