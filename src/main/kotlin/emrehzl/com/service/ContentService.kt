package emrehzl.com.service

import emrehzl.com.models.ContentStatus
import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.reqresobjects.ContentUpdateParams
import emrehzl.com.utils.BaseResponse

interface ContentService {
    suspend fun create(params: ContentCreateParams): BaseResponse<Any>
    suspend fun list(name: String?, status: ContentStatus?): BaseResponse<Any>
    suspend fun getById(id: String?): BaseResponse<Any>
    suspend fun update(params: ContentUpdateParams): BaseResponse<Any>
    suspend fun delete(id: String?): BaseResponse<Any>
    suspend fun addLicenses(contentId: String?, licenseIds: List<String>): BaseResponse<Any>
    suspend fun getLicenses(contentId: String?): BaseResponse<Any>
}