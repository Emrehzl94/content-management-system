package emrehzl.com.repository

import emrehzl.com.db.ContentLicenseTable
import emrehzl.com.db.DatabaseFactory.dbQuery
import emrehzl.com.models.ContentLicense
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.*

class ContentLicenseRepositoryImpl: ContentLicenseRepository {
    override suspend fun add(contentId: String, licenseId: String): ContentLicense? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = ContentLicenseTable.insert {
                it[content] = UUID.fromString(contentId)
                it[license] = UUID.fromString(licenseId)
            }
        }
        return rowToContentLicense(statement?.resultedValues?.get(0))
    }

    override suspend fun add(contentId: String, licenseIds: List<String>): ContentLicense? {
        TODO("Not yet implemented")
    }

    override suspend fun getContentLicenses(contentId: String): List<String> {
        val licenseIds = dbQuery {
            ContentLicenseTable.select { ContentLicenseTable.content.eq(UUID.fromString(contentId)) }
                .map { it[ContentLicenseTable.license].toString() }.toList()
        }
        return licenseIds
    }

    override suspend fun getLicenseContents(licenseId: String): List<String> {
        val contentIds = dbQuery {
            ContentLicenseTable.select { ContentLicenseTable.license.eq(UUID.fromString(licenseId)) }
                .map { it[ContentLicenseTable.content].toString() }.toList()
        }
        return contentIds
    }

    private fun rowToContentLicense(row: ResultRow?): ContentLicense? {
        return if (row == null) null
        else ContentLicense(
            contentId = row[ContentLicenseTable.content].toString(),
            licenseId = row[ContentLicenseTable.license].toString()
        )
    }
}