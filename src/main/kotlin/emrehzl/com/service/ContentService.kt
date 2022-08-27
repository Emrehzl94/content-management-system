package emrehzl.com.service

import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.utils.BaseResponse

interface ContentService {
    suspend fun create(params: ContentCreateParams): BaseResponse<Any>
    suspend fun list(): BaseResponse<Any>
}