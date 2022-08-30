package emrehzl.com.repository

import emrehzl.com.db.LicenseTable
import emrehzl.com.db.DatabaseFactory
import emrehzl.com.db.DatabaseFactory.dbQuery
import emrehzl.com.models.License
import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.time.LocalDate
import java.util.*

class LicenseRepositoryImpl : LicenseRepository {
    override suspend fun create(params: LicenseCreateParams): License? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = LicenseTable.insert {
                it[name] = params.name
                it[startTime] = LocalDate.parse(params.startTime)
                it[endTime] = LocalDate.parse(params.endTime)
            }
        }
        return rowToLicense(statement?.resultedValues?.get(0))
    }

    override suspend fun list(): List<License?> {
        val licenses = dbQuery {
            LicenseTable.selectAll().map { rowToLicense(it) }.toList()
        }
        return licenses
    }

    override suspend fun getById(id: String): License? {
        val license = dbQuery {
            LicenseTable.select { LicenseTable.id.eq(UUID.fromString(id)) }
                .map { rowToLicense(it) }.singleOrNull()
        }
        return license
    }

    override suspend fun getByIds(ids: List<String>): List<License> {
        val licenses = dbQuery {
            LicenseTable.select { LicenseTable.id.inList(ids.map { UUID.fromString(it) }) }
                .map { rowToLicense(it)}.toList()
        }
        return licenses.filterNotNull()
    }

    override suspend fun update(params: LicenseUpdateParams): License? {
        val license = dbQuery {
            LicenseTable.update({ LicenseTable.id.eq(UUID.fromString(params.id)) }) {
                if (params.name != null) {
                    it[name] = params.name
                }
                if (params.startTime != null) {
                    it[startTime] = LocalDate.parse(params.startTime)
                }
                if (params.endTime != null) {
                    it[endTime] = LocalDate.parse(params.endTime)
                }
            }

            LicenseTable.select { LicenseTable.id.eq(UUID.fromString(params.id)) }
                .map { rowToLicense(it) }.singleOrNull()
        }

        return license
    }

    override suspend fun delete(id: String) {
        dbQuery {
            LicenseTable.deleteWhere { LicenseTable.id.eq(UUID.fromString(id)) }
        }
    }

    private fun rowToLicense(row: ResultRow?): License? {
        return if (row == null) null
        else License(
            id = row[LicenseTable.id].toString(),
            name = row[LicenseTable.name],
            startTime = row[LicenseTable.startTime].toString(),
            endTime = row[LicenseTable.endTime].toString(),
            createdAt = row[LicenseTable.createdAt].toString()
        )
    }
}