package emrehzl.com.repository

import emrehzl.com.models.Content
import emrehzl.com.models.ContentStatus
import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.reqresobjects.ContentUpdateParams

interface ContentRepository {
    suspend fun create(params: ContentCreateParams): Content?
    suspend fun list(name: String?, status: ContentStatus?): List<Content>
    suspend fun list(): List<Content>
    suspend fun getById(id: String): Content?
    suspend fun update(params: ContentUpdateParams): Content?
    suspend fun updateStatus(contentId: String, status: ContentStatus)
    suspend fun delete(id: String)
    suspend fun hasActiveLicense(id: String): Boolean
}