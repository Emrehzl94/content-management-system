package emrehzl.com.service

import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams
import emrehzl.com.utils.BaseResponse

interface LicenseService {
    suspend fun create(params: LicenseCreateParams): BaseResponse<Any>
    suspend fun list(): BaseResponse<Any>
    suspend fun getById(id: String?): BaseResponse<Any>
    suspend fun update(params: LicenseUpdateParams): BaseResponse<Any>
    suspend fun delete(id: String?): BaseResponse<Any>
}