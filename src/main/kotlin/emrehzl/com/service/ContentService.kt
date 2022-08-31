package emrehzl.com.service

import emrehzl.com.models.ContentStatus
import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.reqresobjects.ContentUpdateParams
import emrehzl.com.utils.BaseResponse
import emrehzl.com.utils.Response

interface ContentService {
    suspend fun create(params: ContentCreateParams): Response<Any>
    suspend fun list(name: String?, status: ContentStatus?): Response<Any>
    suspend fun getById(id: String?): Response<Any>
    suspend fun update(params: ContentUpdateParams): Response<Any>
    suspend fun delete(id: String?): Response<Any>
    suspend fun addLicenses(contentId: String?, licenseIds: List<String>): Response<Any>
    suspend fun getLicenses(contentId: String?): Response<Any>
}