package emrehzl.com.service

import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams
import emrehzl.com.utils.BaseResponse
import emrehzl.com.utils.Response

interface LicenseService {
    suspend fun create(params: LicenseCreateParams): Response<Any>
    suspend fun list(): Response<Any>
    suspend fun getById(id: String?): Response<Any>
    suspend fun update(params: LicenseUpdateParams): Response<Any>
    suspend fun delete(id: String?): Response<Any>
}