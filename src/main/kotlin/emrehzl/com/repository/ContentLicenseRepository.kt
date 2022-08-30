package emrehzl.com.repository

import emrehzl.com.models.ContentLicense

interface ContentLicenseRepository {
    suspend fun add(contentId: String, licenseId: String): ContentLicense?
    suspend fun add(contentId: String, licenseIds: List<String>)
    suspend fun getContentLicenses(contentId: String): List<String>
    suspend fun getLicenseContents(licenseId: String): List<String>
}