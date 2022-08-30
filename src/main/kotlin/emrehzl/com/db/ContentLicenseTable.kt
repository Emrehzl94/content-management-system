package emrehzl.com.db

import org.jetbrains.exposed.sql.Table

object ContentLicenseTable: Table("content_license") {
    val content = reference("content_id", ContentTable)
    val license = reference("license_id", LicenseTable)
    override val primaryKey = PrimaryKey(content, license)
}