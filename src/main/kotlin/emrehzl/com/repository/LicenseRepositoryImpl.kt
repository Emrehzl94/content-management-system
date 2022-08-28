package emrehzl.com.repository

import emrehzl.com.db.LicenseTable
import emrehzl.com.db.DatabaseFactory
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
                it[id] = UUID.randomUUID()
                    .toString().replace("-", "")
                it[name] = params.name
                it[startTime] = LocalDate.parse(params.startTime)
                it[endTime] = LocalDate.parse(params.endTime)
            }
        }
        return rowToLicense(statement?.resultedValues?.get(0))
    }

    override suspend fun list(): List<License?> {
        val licenses = DatabaseFactory.dbQuery {
            LicenseTable.selectAll().map { rowToLicense(it) }.toList()
        }
        return licenses
    }

    override suspend fun getById(id: String): License? {
        val license = DatabaseFactory.dbQuery {
            LicenseTable.select { LicenseTable.id.eq(id) }
                .map { rowToLicense(it) }.singleOrNull()
        }
        return license
    }

    override suspend fun update(params: LicenseUpdateParams): License? {
        val license = DatabaseFactory.dbQuery {
            LicenseTable.update({ LicenseTable.id.eq(params.id) }) {
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

            LicenseTable.select { LicenseTable.id.eq(params.id) }
                .map { rowToLicense(it) }.singleOrNull()
        }

        return license
    }

    override suspend fun delete(id: String) {
        DatabaseFactory.dbQuery {
            LicenseTable.deleteWhere { LicenseTable.id.eq(id) }
        }
    }

    private fun rowToLicense(row: ResultRow?): License? {
        return if (row == null) null
        else License(
            id = row[LicenseTable.id],
            name = row[LicenseTable.name],
            startTime = row[LicenseTable.startTime].toString(),
            endTime = row[LicenseTable.endTime].toString(),
            createdAt = row[LicenseTable.createdAt].toString()
        )
    }
}