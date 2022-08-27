package emrehzl.com.repository

import emrehzl.com.models.Content
import emrehzl.com.reqresobjects.ContentCreateParams

interface ContentRepository {
    suspend fun create(params: ContentCreateParams): Content?
    suspend fun list(): List<Content?>
    suspend fun getById(id: String): Content?
}